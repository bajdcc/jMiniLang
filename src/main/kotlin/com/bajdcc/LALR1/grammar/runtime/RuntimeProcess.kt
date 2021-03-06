package com.bajdcc.LALR1.grammar.runtime

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.RuntimeMachine.Ring3Option.LOG_FILE
import com.bajdcc.LALR1.grammar.runtime.RuntimeMachine.Ring3Option.LOG_PIPE
import com.bajdcc.LALR1.grammar.runtime.service.IRuntimeProcessService
import com.bajdcc.LALR1.grammar.runtime.service.RuntimeService
import com.bajdcc.LALR1.grammar.runtime.service.RuntimeUserService
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import javafx.util.Pair
import org.apache.log4j.Logger
import java.io.InputStream
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque

/**
 * 【虚拟机】运行时进程
 *
 * @author bajdcc
 */
class RuntimeProcess @Throws(Exception::class)
constructor(name: String, input: InputStream, private val pageFileMap: Map<String, String>) : IRuntimeProcessService {
    override var pid: Int = 0
        private set
    private var cyclePtr = 0
    var name: String = ""
        private set
    private lateinit var codePage: RuntimeCodePage
    private var arrCodes = mutableMapOf<String, String>()
    private var arrPages = mutableMapOf<String, RuntimeCodePage>()
    private var arrProcess = Array<SchdProcess?>(MAX_PROCESS) { null }
    private var setProcessId = mutableSetOf<Int>()
    private var isWaitingForUI = false
    private var needToExit = false
    private var highSpeed = false
    var service: RuntimeService

    private val stat = SystemStat()

    val usrProcs: List<Int>
        get() = setProcessId.filter { id -> arrProcess[id]!!.ring != 0 }

    val sysProcs: List<Int>
        get() = setProcessId.filter { id -> arrProcess[id]!!.ring == 0 }

    val allProcs: List<Int>
        get() = setProcessId.toList()

    override val speed: Double
        get() = stat.speed

    override val procInfoCache: List<Array<Any>>
        get() = stat.procCache

    override val ring3: IRuntimeRing3
        get() = arrProcess[pid]!!.machine

    internal data class SchdProcess constructor(var machine: RuntimeMachine) {
        var priority: Int = 0
        var sleep: Int = 0
        var ring: Int = 0
        var runnable = false
        var sleeping = false
        var waitingPids: Queue<Int>

        init {
            this.priority = 128
            this.sleep = 0
            this.ring = 0
            this.runnable = true
            this.waitingPids = ArrayDeque()
        }

        constructor(machine: RuntimeMachine, ring: Int) : this(machine) {
            this.ring = ring
        }
    }

    @Throws(Exception::class)
    fun run() {
        var lastTime = System.currentTimeMillis()
        stat.cycle = 0
        stat.ticked = false
        while (schd()) {
            val span = System.currentTimeMillis() - lastTime
            if (span > 1000) {
                stat.speed = 1000.0 * stat.cycle / span
                stat.cycle = 0
                stat.ticked = true
                lastTime = System.currentTimeMillis()
                stat.procCache = setProcessId.toList().sortedBy { it }.map { getProcInfoById(it) }
            }
        }
    }

    init {
        service = RuntimeService(this)
        runMainProcess(name, input)
    }

    fun getPriority(pid: Int): Int {
        return arrProcess[pid]!!.priority
    }

    fun setPriority(pid: Int, priority: Int): Boolean {
        arrProcess[pid]!!.priority = priority
        return true
    }

    fun getProcInfoById(id: Int): Array<Any> {
        return arrProcess[id]!!.machine.procInfo
    }

    @Throws(Exception::class)
    fun getPage(name: String): RuntimeCodePage? {
        val vfs = service.fileService.getVfs(name)
        val c = arrCodes[name]
        if (c != null || vfs != "") {
            val code = if (vfs != "") vfs else c!!
            val p = arrPages[code]
            if (p != null) {
                return p
            } else {
                logger.debug("Loading page: $name")
                try {
                    val grammar = Grammar(code)
                    val page = grammar.codePage
                    service.fileService.addVfs(name, code)
                    arrPages[name] = page
                    return page
                } catch (e: SyntaxException) {
                    val filename = pageFileMap[name]
                    e.fileName = if (filename == null) "Unknown Source" else "$filename.txt"
                    e.pageName = name
                    System.err.println("#PAGE ERROR# --> $name")
                    throw e
                }
            }
        }
        return null
    }

    @Throws(Exception::class)
    private fun schd(): Boolean {
        if (setProcessId.isEmpty())
            return false
        if (needToExit)
            return false
        if (isWaitingForUI) {
            if (!highSpeed)
                Thread.sleep(CLOCK_WAIT_UI.toLong())
            isWaitingForUI = false
            return true
        }
        val pids = setProcessId.toList()
        val pidNum = pids.count { a -> arrProcess[a]!!.ring < 3 }.toLong()
        val pidUserNum = pids.size - pidNum
        var sleep: Long = 0
        var sleepUser: Long = 0
        LABEL_PID@ for (pid in pids) {
            this.pid = pid
            val process = arrProcess[pid]
            if (process != null) {
                if (process.runnable) {
                    if (process.ring < 3) {
                        val cycle = MAX_CYCLE - process.priority
                        for (i in 0 until cycle) {
                            if (process.sleep > 0) {
                                process.sleep--
                                sleep++
                                break
                            }
                            if (process.runnable) {
                                stat.cycle++
                                when (process.machine.runStep()) {
                                    1 -> return false
                                    2 -> break@LABEL_PID
                                }
                            }
                        }
                    } else {
                        val cycle = MAX_CYCLE - process.priority
                        try {
                            for (i in 0 until cycle) {
                                if (process.sleep > 0) {
                                    process.sleep--
                                    sleepUser++ // boost!
                                    break
                                }
                                if (process.runnable) {
                                    stat.cycle++
                                    when (process.machine.runStep()) {
                                        1 -> return false
                                        2 -> {
                                            ring3Kill(pid, "正常退出")
                                            break@LABEL_PID
                                        }
                                    }
                                }
                            }
                        } catch (e: RuntimeException) {
                            val error = e.error!!.message + " " + e.position + ": " + e.info
                            System.err.println(error)
                            e.printStackTrace()
                            ring3Kill(pid, error)
                        }

                    }
                } else if (process.ring < 3) {
                    sleep++
                } else {
                    sleepUser++
                }
            }
        }
        if (sleep == pidNum) { // 都在休眠，等待并减掉休眠时间
            pids.forEach { pid ->
                val process = arrProcess[pid]
                if (process != null && process.runnable) {
                    if (process.sleep < CLOCK_ONCE_SLEEP)
                        process.sleep = 0
                    else
                        process.sleep -= CLOCK_ONCE_SLEEP
                }
            }
            var didNotWakeSleepProcess = true
            if (stat.sleepQueue.isNotEmpty()) {
                val now = System.currentTimeMillis()
                while (stat.sleepQueue.isNotEmpty()) {
                    val recent = stat.sleepQueue.peek()
                    if (now > recent.ms) {
                        if (didNotWakeSleepProcess)
                            didNotWakeSleepProcess = false
                        wakeup(recent.pid, true)
                        stat.sleepQueue.poll()
                    } else {
                        break
                    }
                }
            }
            if (didNotWakeSleepProcess) {
                if (sleepUser == pidUserNum)
                    Thread.sleep(TIME_SLEEP_FULL.toLong())
                else if (stat.ticked) {
                    Thread.sleep(TIME_SLEEP_HALF.toLong())
                    stat.ticked = false
                }
            } else {
                if (stat.ticked)
                    stat.ticked = false
            }
            if (!pipeDeque.isEmpty()) {
                val pair = pipeDeque.poll()
                service.pipeService.writeStringNew(pair.key, pair.value)
            }
            if (!userHandleCallbackQueue.isEmpty()) {
                val pair = userHandleCallbackQueue.poll()!!
                service.userService.signal(pair.key, pair.value)
            }
        }
        return true
    }

    @Throws(Exception::class)
    private fun runMainProcess(name: String, input: InputStream) {
        initMainProcess(name, input)
    }

    fun isBlock(pid: Int): Boolean {
        return setProcessId.contains(pid) && !arrProcess[pid]!!.runnable
    }

    @Throws(Exception::class)
    private fun initMainProcess(name: String, input: InputStream) {
        this.name = name
        this.codePage = RuntimeCodePage.importFromStream(input)
        this.service = RuntimeService(this)
        val machine = RuntimeMachine(name, 0, cyclePtr, -1, this)
        machine.initStep(name, codePage, emptyList(), 0, null)
        setProcessId.add(cyclePtr)
        arrProcess[cyclePtr++] = SchdProcess(machine)
    }

    /**
     * 创建进程
     *
     * @param creatorId 创建者ID
     * @param ring      RING层数
     * @param name      页名
     * @param page      页
     * @param pc        起始指令（fork时为负）
     * @param obj       参数
     * @return 进程ID
     * @throws Exception 运行时异常
     */
    @Throws(Exception::class)
    fun createProcess(creatorId: Int, ring: Int, name: String, page: RuntimeCodePage, pc: Int, obj: RuntimeObject?): Int {
        var newName = name
        if (setProcessId.size >= MAX_PROCESS) {
            arrProcess[creatorId]!!.machine.err(
                    RuntimeException.RuntimeError.PROCESS_OVERFLOW)
        }
        if (arrProcess[creatorId]!!.ring != 0 && ring == 0) {
            arrProcess[creatorId]!!.machine.err(
                    RuntimeException.RuntimeError.ACCESS_FORBIDDEN)
        }
        val pid: Int
        while (true) {
            if (arrProcess[cyclePtr] == null) {
                if (ring == 3) {
                    newName = if (newName.startsWith('$'))
                        newName.substring(1)
                    else
                        USER_PROC_FILE_PREFIX + cyclePtr
                }
                val machine = RuntimeMachine(newName, ring, cyclePtr, creatorId, this)
                if (pc >= 0)
                    machine.initStep(newName, page, arrProcess[creatorId]!!.machine.getPageRefers(newName), pc, obj)
                else
                    machine.copyFrom(arrProcess[creatorId]!!.machine)
                setProcessId.add(cyclePtr)
                pid = cyclePtr
                arrProcess[cyclePtr++] = SchdProcess(machine, ring)
                if (cyclePtr >= MAX_PROCESS) {
                    cyclePtr -= MAX_PROCESS
                }
                break
            }
            cyclePtr++
            if (cyclePtr >= MAX_PROCESS) {
                cyclePtr -= MAX_PROCESS
            }
        }
        logger.debug("RING-" + ring + " process #" + pid + " '" + newName + "' created, " + setProcessId.size + " now.")
        return pid
    }

    fun destroyProcess(processId: Int) {
        arrProcess[processId]!!.waitingPids.forEach { pid ->
            wakeup(pid)
        }
        arrProcess[processId] = null
        setProcessId.remove(processId)
        logger.debug("Process #" + processId + " exit, " + setProcessId.size + " left.")
    }

    override fun block(pid: Int, sleep: Boolean): Boolean {
        if (!setProcessId.contains(pid)) {
            return false
        }
        val p = arrProcess[pid]!!
        if (p.runnable) {
            p.runnable = false
            if (sleep)
                p.sleeping = true
            return true
        }
        logger.warn("Process #$pid is blocked, but previous status was blocked.")
        return false
    }

    override fun wakeup(pid: Int, sleep: Boolean): Boolean {
        if (!setProcessId.contains(pid)) {
            return false
        }
        val p = arrProcess[pid]!!
        if (!p.runnable) {
            p.runnable = true
            if (sleep)
                p.sleeping = false
            return true
        }
        logger.warn("Process #$pid is running, but previous status was running.")
        return false
    }

    override fun sleep(pid: Int, turn: Int): Int {
        var t = turn
        if (!setProcessId.contains(pid)) {
            return -1
        }
        if (t < 0)
            t = 0
        val p = arrProcess[pid]!!
        p.sleep += t
        return p.sleep
    }

    override fun blocks(pid: Int, ms: Int): Long {
        block(pid)
        stat.sleepQueue.add(SleepStruct(pid, System.currentTimeMillis() + ms.toLong()))
        return System.currentTimeMillis() + ms.toLong()
    }

    override fun join(joined: Int, pid: Int): Boolean {
        if (!setProcessId.contains(pid)) {
            return false
        }
        if (!setProcessId.contains(joined))
            return false
        block(pid)
        arrProcess[joined]!!.waitingPids.add(pid)
        return false
    }

    override fun live(pid: Int): Boolean {
        return setProcessId.contains(pid)
    }

    override fun available(): Boolean {
        return setProcessId.size < MAX_PROCESS
    }

    override fun addCodePage(name: String, code: String): Boolean {
        if (arrCodes.containsKey(name)) {
            return false
        }
        service.fileService.addVfs(name, code)
        arrCodes[name] = code
        return true
    }

    override fun waitForUI() {
        isWaitingForUI = true
    }

    override fun setDebug(pid: Int, debug: Boolean) {
        if (setProcessId.contains(pid)) {
            arrProcess[pid]!!.machine.isDebug = debug
        }
    }

    override fun setHighSpeed(mode: Boolean) {
        highSpeed = mode
    }

    private data class SleepStruct(val pid: Int, val ms: Long)

    private inner class SystemStat {
        var speed = 0.toDouble()
        var cycle = 0
        var ticked = false
        var procCache = listOf<Array<Any>>()
        val sleepQueue = PriorityQueue<SleepStruct>(MAX_PROCESS, Comparator.comparingLong { it.ms })
    }

    fun halt() {
        needToExit = true
    }

    override fun ring3Kill(pid: Int, error: String): Int {
        if (pid == -1) {
            arrProcess.asSequence()
                    .mapIndexed { index, schdProcess -> Pair(index, schdProcess) }
                    .filter { it.value?.machine?.ring3?.isRing3 == true }
                    .map { it.key }
                    .forEach { ring3Kill(it, error) }
            return 0
        }
        if (!setProcessId.contains(pid)) {
            return -1
        }
        val ring3 = arrProcess[pid]!!.machine.ring3
        if (!ring3.isRing3) {
            return -2
        }
        val tmp = this.pid
        this.pid = pid
        service.userService.destroy()
        this.pid = tmp
        if (ring3.isOptionsBool(LOG_FILE))
            service.fileService.addVfs(USER_PROC_FILE_PREFIX + pid, error)
        if (!ring3.isOptionsBool(LOG_PIPE))
            service.pipeService.destroyByName(pid, USER_PROC_PIPE_PREFIX + pid)
        val p = arrProcess[pid]!!
        if (p.sleeping)
            stat.sleepQueue.removeIf { it.pid == pid }
        p.waitingPids.forEach { id ->
            wakeup(id)
        }
        setProcessId.remove(pid)
        arrProcess[pid] = null
        logger.debug("RING3 proc #" + pid + " exit, " + setProcessId.size + " left.")
        return 0
    }

    override fun getRing3(pid: Int): IRuntimeRing3? {
        return if (!setProcessId.contains(pid)) null else arrProcess[pid]!!.machine.ring3
    }

    companion object {

        private val pipeDeque = ConcurrentLinkedDeque<Pair<String, String>>()

        private val userHandleCallbackQueue = ConcurrentLinkedDeque<Pair<Int, RuntimeUserService.UserSignal>>()

        fun writePipe(name: String, msg: String) {
            pipeDeque.add(Pair(name, msg))
        }

        fun sendUserSignal(id: Int, type: RuntimeUserService.UserSignal) {
            userHandleCallbackQueue.add(Pair(id, type))
        }

        private val logger = Logger.getLogger("proc")
        private const val MAX_PROCESS = 1000
        private const val MAX_CYCLE = 150
        private const val TIME_SLEEP_FULL = 5
        private const val TIME_SLEEP_HALF = 10
        private const val CLOCK_ONCE_SLEEP = 50
        private const val CLOCK_WAIT_UI = 500
        const val USER_PROC_FILE_PREFIX = "/proc/"
        const val USER_PROC_PIPE_PREFIX = "!USER_PROC#"
    }
}
