package com.bajdcc.LALR1.grammar.runtime

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.RuntimeException.RuntimeError
import com.bajdcc.LALR1.grammar.runtime.RuntimeProcess.Companion.USER_PROC_PIPE_PREFIX
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeFuncObject
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeMap
import com.bajdcc.LALR1.grammar.runtime.service.IRuntimeService
import com.bajdcc.LALR1.grammar.type.TokenTools
import com.bajdcc.LALR1.interpret.module.*
import com.bajdcc.LALR1.interpret.module.std.ModuleStdBase
import com.bajdcc.LALR1.interpret.module.std.ModuleStdShell
import com.bajdcc.LALR1.interpret.module.user.ModuleUserBase
import com.bajdcc.LALR1.interpret.module.user.ModuleUserCParser
import com.bajdcc.LALR1.interpret.module.user.ModuleUserLisp
import com.bajdcc.LALR1.interpret.module.user.ModuleUserWeb
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.HashListMapEx
import com.bajdcc.util.HashListMapEx2
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType
import org.apache.log4j.Logger

import java.io.BufferedReader
import java.io.FileReader
import java.io.InputStream

/**
 * 【虚拟机】运行时自动机
 *
 * @author bajdcc
 */
class RuntimeMachine @Throws(Exception::class)
@JvmOverloads constructor(private val ring: Int = 0, private val process: RuntimeProcess? = null) : IRuntimeStack, IRuntimeStatus, IRuntimeRing3 {

    private var ring3Struct: Ring3Struct? = null

    private var pageMap = HashListMapEx<String, RuntimeCodePage>()
    private var pageRefer = mutableMapOf<String, MutableList<RuntimeCodePage>>()
    private var codeCache = mutableMapOf<String, RuntimeCodePage>()
    private var stkYieldData = mutableListOf<RuntimeObject>()
    private var stkYieldMap = HashListMapEx2<String, Int>()
    private lateinit var currentStack: RuntimeStack
    private var stack = mutableListOf<RuntimeStack>()
    private lateinit var currentPage: RuntimeCodePage
    private var pageName: String = ""
    override var page: String = ""
    private var description: String = ""
    override var pid: Int = -1
    override var parentPid: Int = -1
    private var triesCount: Int = 0
    var isDebug = false

    override val priority: Int
        get() = process!!.getPriority(pid)

    override val service: IRuntimeService
        get() = process!!.service

    override val usrProcs: List<Int>
        get() = process!!.usrProcs

    override val sysProcs: List<Int>
        get() = process!!.sysProcs

    override val allProcs: List<Int>
        get() = process!!.allProcs

    override val procInfo: Array<Any>
        get() {
            val funcName = currentStack.funcSimpleName
            return arrayOf(if (process!!.isBlock(pid)) " " else "*", ring.toString(), pid.toString(), page, funcName, description)
        }

    override val isRing3: Boolean
        get() = ring3Struct != null

    override val handles: Set<Int>
        get() = ring3Struct!!.handles

    override var blockHandle: Int
        get() = ring3Struct!!.blockHandle
        set(id) {
            ring3Struct!!.blockHandle = id
        }

    override val funcArgsCount: Int
        get() = currentStack.funcArgsCount1

    override val ring3: IRuntimeRing3
        get() = this

    override val allDocs: RuntimeArray
        get() {
            val array = RuntimeArray()
            var i = 1L
            listOf(modulesSystem, modulesUser).flatMap { it }.forEach { module ->
                try {
                    module.codePage.info.externFuncList.forEach { arr ->
                        arr.insert(0, RuntimeObject(i++))
                        arr.insert(1, RuntimeObject(module.moduleName))
                        array.add(RuntimeObject(arr))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return array
        }

    private val isEOF: Boolean
        get() = currentStack.reg.execId < 0 || currentStack.reg.execId >= currentPage.insts.size

    /**
     * 向前寻找当前栈的父栈
     * @return 非yield栈
     */
    private val lastStack: Int
        get() {
            val parent = currentStack.parent
            return if (parent != -1) {
                parent
            } else currentStack.level
        }

    enum class Ring3Option {
        /**
         * 自动保存运行日志文件，默认true
         */
        LOG_FILE,
        /**
         * 运行结果自动保留输出管道，默认false
         */
        LOG_PIPE
    }

    internal class Ring3Struct {
        internal var putHandle: Int = 0
        internal var bSaveLogFile: Boolean = false
        internal var bSavePipeFile: Boolean = false
        var handles: MutableSet<Int>
        var blockHandle: Int = 0

        init {
            putHandle = -1
            bSaveLogFile = true
            bSavePipeFile = false
            handles = mutableSetOf()
            blockHandle = -1
        }

        internal fun copyFrom(ring3: Ring3Struct?) {
            bSaveLogFile = ring3!!.bSaveLogFile
            bSavePipeFile = ring3.bSavePipeFile
        }

        internal fun setOptionsBool(option: Ring3Option, flag: Boolean) {
            when (option) {
                RuntimeMachine.Ring3Option.LOG_FILE -> bSaveLogFile = flag
                RuntimeMachine.Ring3Option.LOG_PIPE -> bSavePipeFile = flag
            }
        }

        internal fun isOptionsBool(option: Ring3Option): Boolean {
            return when (option) {
                RuntimeMachine.Ring3Option.LOG_FILE -> bSaveLogFile
                RuntimeMachine.Ring3Option.LOG_PIPE -> bSavePipeFile
            }
        }
    }

    init {
        this.stack.add(RuntimeStack())
        this.refreshStack()
        if (ring < 3) {
            for (module in modulesSystem) {
                process!!.service.fileService.addVfs(module.moduleName, module.moduleCode)
                try {
                    run(module.moduleName, module.codePage)
                } catch (e: SyntaxException) {
                    e.pageName = module.moduleName
                    e.fileName = module.javaClass.simpleName + ".txt"
                    throw e
                }

            }
        } else {
            for (module in modulesUser) {
                process!!.service.fileService.addVfs(module.moduleName, module.moduleCode)
                try {
                    run(module.moduleName, module.codePage)
                } catch (e: SyntaxException) {
                    e.pageName = module.moduleName
                    e.fileName = module.javaClass.simpleName + ".txt"
                    throw e
                }

            }
        }
    }

    @Throws(Exception::class)
    constructor(name: String, ring: Int, id: Int, parentId: Int, process: RuntimeProcess) : this(ring, process) {
        this.page = name
        this.description = if (ring == 3) "user proc" else "none"
        this.pid = id
        this.parentPid = parentId
        if (ring == 3) {
            ring3Struct = Ring3Struct()
            ring3Struct!!.putHandle = process.service.pipeService.create(USER_PROC_PIPE_PREFIX + pid, name)
        }
    }

    private fun refreshStack() {
        currentStack = stack[stack.size - 1]
    }

    /**
     * FORK进程
     * @param machine 被FORK的进程
     */
    @Throws(RuntimeException::class)
    fun copyFrom(machine: RuntimeMachine) {
        assert(machine.ring == 3)
        ring3Struct!!.copyFrom(machine.ring3Struct)
        pageMap = machine.pageMap.copy()
        pageRefer = machine.pageRefer.entries.associate { it.key to it.value.toMutableList() }.toMutableMap()
        codeCache = HashMap(machine.codeCache)
        stkYieldData = ArrayList(machine.stkYieldData)
        stkYieldMap = machine.stkYieldMap.copy()
        stack = machine.stack.map { it.copy() }.toMutableList()
        currentStack = stack[machine.stack.indexOf(machine.currentStack)]
        currentPage = machine.currentPage
        triesCount = machine.triesCount
        store(RuntimeObject(-1L))
        opReturn()
    }

    @Throws(Exception::class)
    fun run(name: String, input: InputStream) {
        run(name, RuntimeCodePage.importFromStream(input))
    }

    @Throws(Exception::class)
    override fun runPage(name: String) {
        val br = BufferedReader(FileReader(name))
        val sb = StringBuilder()
        var line = br.readLine()
        while (line != null) {
            sb.append(line)
            sb.append(System.lineSeparator())
            line = br.readLine()
        }
        br.close()
        logger.debug("Loading file: $name")
        val grammar = Grammar(sb.toString())
        run(name, grammar.codePage)
    }

    @Throws(Exception::class)
    override fun runProcess(name: String): Int {
        val br = BufferedReader(FileReader(name))
        val sb = StringBuilder()
        var line = br.readLine()
        while (line != null) {
            sb.append(line)
            sb.append(System.lineSeparator())
            line = br.readLine()
        }
        br.close()
        logger.debug("Loading file: $name")
        val grammar = Grammar(sb.toString())
        return process!!.createProcess(pid, 0, name, grammar.codePage, 0, null)
    }

    @Throws(Exception::class)
    override fun runProcessX(name: String): Int {
        val page = process!!.getPage(name) ?: return -1
        return process.createProcess(pid, 0, name, page, 0, null)
    }

    @Throws(Exception::class)
    override fun runUsrProcess(name: String): Int {
        val br = BufferedReader(FileReader(name))
        val sb = StringBuilder()
        var line = br.readLine()
        while (line != null) {
            sb.append(line)
            sb.append(System.lineSeparator())
            line = br.readLine()
        }
        br.close()
        val grammar = Grammar(sb.toString())
        return process!!.createProcess(pid, 1, name, grammar.codePage, 0, null)
    }

    @Throws(Exception::class)
    override fun runUsrProcessX(name: String): Int {
        val page = process!!.getPage(name) ?: return -1
        return process.createProcess(pid, 1, name, page, 0, null)
    }

    fun add(name: String, page: RuntimeCodePage) {
        if (pageName == name)
            return
        if (pageMap.contains(name)) {
            warn(RuntimeError.DUP_PAGENAME, "代码页 $pageName 加载 $name")
        }
        pageMap.add(name, page)
        pageRefer[name] = mutableListOf()
        pageRefer[name]!!.add(page)
        page.info.dataMap["desc"] = name
    }

    @Throws(Exception::class)
    fun run(name: String, page: RuntimeCodePage) {
        add(name, page)
        currentPage = page
        currentStack.reg.pageId = name
        currentStack.reg.execId = 0
        switchPage()
        runInsts()
    }

    @Throws(Exception::class)
    private fun runInsts() {
        while (runByStep());
    }

    @Throws(Exception::class)
    fun initStep(name: String, page: RuntimeCodePage, refers: List<RuntimeCodePage>, pc: Int, obj: RuntimeObject?) {
        add(name, page)
        currentPage = page
        currentStack.reg.pageId = name
        currentStack.reg.execId = -1
        switchPage()
        pageRefer[pageName]!!.addAll(refers)
        opOpenFunc()
        if (obj != null) {
            opPushObj(obj)
            opPushArgs()
        }
        opPushPtr(pc)
        opCall()
    }

    @Throws(Exception::class)
    fun runStep(): Int {
        val inst = RuntimeInst.values()[currentInst()]
        if (inst == RuntimeInst.ihalt) {
            if (ring < 3)
                process!!.destroyProcess(pid)
            return 2
        }
        return if (runByStep()) 0 else 1
    }

    @Throws(Exception::class)
    private fun runByStep(): Boolean {
        val inst = RuntimeInst.values()[currentInst()]
        if (inst == RuntimeInst.ihalt) {
            return false
        }
        if (isDebug) {
            System.err.println()
            System.err.print(currentStack.reg.execId.toString() + ": " + inst.toString())
        }
        val op = TokenTools.ins2op(inst)
        nextInst()
        if (op != null) {
            if (!RuntimeTools.calcOp(inst, this)) {
                err(RuntimeError.UNDEFINED_CONVERT, op.desc)
            }
        } else {
            if (!RuntimeTools.calcData(inst, this)) {
                if (!RuntimeTools.calcJump(inst, this)) {
                    err(RuntimeError.WRONG_INST, inst.toString())
                }
            }
        }
        if (isDebug) {
            System.err.println()
            System.err.print(currentStack.toString())
            System.err.print("协程栈：")
            System.err.print(stkYieldData.toString())
            System.err.println()
        }
        return true
    }

    @Throws(RuntimeException::class)
    override fun load(): RuntimeObject {
        if (currentStack.isEmptyStack) {
            err(RuntimeError.NULL_STACK)
        }
        return currentStack.popData()
    }

    @Throws(RuntimeException::class)
    override fun store(obj: RuntimeObject) {
        currentStack.pushData(obj)
    }

    @Throws(RuntimeException::class)
    private fun dequeue(): RuntimeObject {
        if (stkYieldData.isEmpty()) {
            err(RuntimeError.NULL_QUEUE)
        }
        val obj = stkYieldData[stkYieldData.size - 1]
        stkYieldData.removeAt(stkYieldData.size - 1)
        return obj
    }

    private fun enqueue(obj: RuntimeObject) {
        stkYieldData.add(obj)
    }

    @Throws(RuntimeException::class)
    fun top(): RuntimeObject {
        if (currentStack.isEmptyStack) {
            err(RuntimeError.NULL_STACK)
        }
        return currentStack.top()
    }

    @Throws(RuntimeException::class)
    private fun loadInt(): Int {
        val obj = load()
        if (obj.type != RuntimeObjectType.kPtr) {
            err(RuntimeError.WRONG_OPERATOR, RuntimeObjectType.kInt.desc + " " + obj.toString())
        }
        return obj.obj as Int
    }

    @Throws(RuntimeException::class)
    private fun loadString(): String {
        val obj = load()
        if (obj.type != RuntimeObjectType.kString) {
            err(RuntimeError.WRONG_OPERATOR, RuntimeObjectType.kInt.desc + " " + obj.toString())
        }
        return obj.obj.toString()
    }

    @Throws(RuntimeException::class)
    private fun loadBool(): Boolean {
        val obj = load()
        if (obj.type != RuntimeObjectType.kBool) {
            err(RuntimeError.WRONG_OPERATOR, RuntimeObjectType.kBool.desc + " " + obj.toString())
        }
        return obj.obj as Boolean
    }

    @Throws(RuntimeException::class)
    private fun loadBoolRetain(): Boolean {
        val obj = top()
        if (obj.type != RuntimeObjectType.kBool) {
            err(RuntimeError.WRONG_OPERATOR, RuntimeObjectType.kBool.desc + " " + obj.toString())
        }
        return obj.obj as Boolean
    }

    @Throws(RuntimeException::class)
    override fun push() {
        val obj = RuntimeObject(current())
        store(obj)
        next()
    }

    @Throws(RuntimeException::class)
    override fun pop() {
        if (currentStack.isEmptyStack) {
            err(RuntimeError.NULL_STACK)
        }
        currentStack.popData()
    }

    @Throws(RuntimeException::class)
    private fun nextInst() {
        currentStack.reg.execId++
        if (isEOF) {
            err(RuntimeError.WRONG_CODEPAGE)
        }
    }

    @Throws(RuntimeException::class)
    private operator fun next() {
        if (isDebug) {
            System.err.print(" " + current())
        }
        currentStack.reg.execId += 4
        if (isEOF) {
            err(RuntimeError.WRONG_CODEPAGE)
        }
    }

    @Throws(RuntimeException::class)
    override fun err(type: RuntimeError) {
        System.err.println(currentStack)
        throw RuntimeException(type, currentStack.reg.execId, type.message + "\n\n[ CODE ]\n" + currentPage.getDebugInfoByInc(currentStack.reg.execId.toLong()))
    }

    @Throws(RuntimeException::class)
    override fun err(type: RuntimeError, message: String) {
        System.err.println(currentStack)
        throw RuntimeException(type, currentStack.reg.execId, type.message + " " + message + "\n\n[ CODE ]\n" + currentPage.getDebugInfoByInc(currentStack.reg.execId.toLong()))
    }

    @Throws(RuntimeException::class)
    fun errRT(type: RuntimeError, message: String) {
        System.err.println(currentStack)
        throw RuntimeException(type, currentStack.reg.execId, message)
    }

    override fun warn(type: RuntimeError, message: String) {
        logger.warn(String.format("#%03d [%s] %s %s", pid, pageName, type.message, message))
    }

    @Throws(Exception::class)
    override fun createProcess(func: RuntimeFuncObject): Int {
        return process!!.createProcess(pid, 0, func.page, pageMap[func.page], func.addr, null)
    }

    @Throws(Exception::class)
    override fun createProcess(func: RuntimeFuncObject, obj: RuntimeObject): Int {
        return process!!.createProcess(pid, 0, func.page, pageMap[func.page], func.addr, obj)
    }

    @Throws(Exception::class)
    override fun createUsrProcess(func: RuntimeFuncObject): Int {
        return process!!.createProcess(pid, 1, func.page, pageMap[func.page], func.addr, null)
    }

    @Throws(Exception::class)
    override fun createUsrProcess(func: RuntimeFuncObject, obj: RuntimeObject): Int {
        return process!!.createProcess(pid, 1, func.page, pageMap[func.page], func.addr, obj)
    }

    override fun getPageRefers(page: String): List<RuntimeCodePage> {
        return pageRefer.getOrDefault(page, listOf())
    }

    override fun setPriority(priority: Int): Boolean {
        return process!!.setPriority(pid, priority)
    }

    override fun sleep(): Int {
        return 0
    }

    override fun setProcDesc(desc: String) {
        description = desc
    }

    @Throws(Exception::class)
    override fun exec(code: String): Int {
        try {
            val grammar = Grammar(code)
            return process!!.createProcess(pid, 3, page, grammar.codePage, 0, null)
        } catch (e: RegexException) {
            e.printStackTrace()
            errRT(RuntimeError.THROWS_EXCEPTION, e.position.toString() + ", " + e.message)
        } catch (e: SyntaxException) {
            e.printStackTrace()
            errRT(RuntimeError.THROWS_EXCEPTION, String.format("%s %s %s",
                    e.position, e.message, e.info))
        }

        return -1
    }

    @Throws(Exception::class)
    override fun execFile(filename: String, code: String): Int {
        try {
            val page: RuntimeCodePage
            if (codeCache.containsKey(filename))
                page = codeCache[filename]!!
            else
                page = Grammar(code).codePage
            return process!!.createProcess(pid, 3, filename.substring(1), page, 0, null)
        } catch (e: RegexException) {
            e.printStackTrace()
            errRT(RuntimeError.THROWS_EXCEPTION, e.position.toString() + ", " + e.message)
        } catch (e: SyntaxException) {
            e.printStackTrace()
            errRT(RuntimeError.THROWS_EXCEPTION, String.format("%s %s\n%s",
                    e.position, e.message, e.info))
        }

        return -1
    }

    override fun put(text: String) {
        val pipe = process!!.service.pipeService
        for (i in 0 until text.length) {
            pipe.write(ring3Struct!!.putHandle, text[i])
        }
    }

    override fun setOptionsBool(option: Ring3Option, flag: Boolean) {
        ring3Struct!!.setOptionsBool(option, flag)
    }

    override fun isOptionsBool(option: Ring3Option): Boolean {
        return ring3Struct!!.isOptionsBool(option)
    }

    override fun addHandle(id: Int) {
        ring3Struct!!.handles.add(id)
    }

    override fun removeHandle(id: Int) {
        ring3Struct!!.handles.remove(id)
    }

    @Throws(Exception::class)
    override fun fork(): Int {
        return process!!.createProcess(pid, ring, "fork", currentPage, -1, null)
    }

    override fun getFuncArgs(index: Int): RuntimeObject? {
        return currentStack.getFuncArgs(index)
    }

    override fun getRing3(pid: Int): IRuntimeRing3? {
        return process!!.getRing3(pid)
    }

    override fun getProcInfoById(id: Int): Array<Any> {
        return process!!.getProcInfoById(id)
    }

    @Throws(RuntimeException::class)
    private fun switchPage() {
        if (!currentStack.reg.pageId.isEmpty()) {
            currentPage = pageMap[currentStack.reg.pageId]
            pageName = currentPage.info.dataMap["desc"].toString()
        } else {
            err(RuntimeError.WRONG_CODEPAGE)
        }
    }

    @Throws(RuntimeException::class)
    private fun getInst(pc: Int): Int {
        val code = currentPage.insts
        if (pc < 0 || pc >= code.size) {
            err(RuntimeError.WRONG_INST, pc.toString())
        }
        return code[pc].toInt()
    }

    @Throws(RuntimeException::class)
    private fun currentInst(): Int {
        return if (currentStack.reg.execId != -1) {
            getInst(currentStack.reg.execId)
        } else {
            RuntimeInst.ihalt.ordinal
        }
    }

    @Throws(RuntimeException::class)
    private fun current(): Int {
        var op = 0
        var b: Int
        for (i in 0..3) {
            b = getInst(currentStack.reg.execId + i)
            op += (b and 0xFF) shl (8 * i)
        }
        return op
    }

    @Throws(RuntimeException::class)
    private fun fetchFromGlobalData(index: Int): RuntimeObject {
        if (index < 0 || index >= currentPage.data.size) {
            err(RuntimeError.WRONG_OPERATOR, index.toString())
        }
        return RuntimeObject(currentPage.data[index])
    }

    @Throws(RuntimeException::class)
    override fun opLoad() {
        val idx = loadInt()
        val obj = fetchFromGlobalData(idx)
        if (obj.symbol == null)
            obj.symbol = currentPage.data[idx]
        currentStack.pushData(obj)
    }

    @Throws(RuntimeException::class)
    override fun opLoadFunc() {
        val idx = loadInt()
        val func = RuntimeFuncObject(pageName, idx)
        val obj = RuntimeObject(func)
        val envSize = loadInt()
        FOR_LOOP@ for (i in 0 until envSize) {
            var id = loadInt()
            if (id == -1) {
                id = loadInt()
                val name = fetchFromGlobalData(id).string
                var value: RuntimeDebugValue? = currentPage.info.getValueCallByName(name)
                if (value != null) {
                    func.addEnv(id, value.value())
                    continue
                }
                var index = currentPage.info.getAddressOfExportFunc(name)
                if (index != -1) {
                    func.addEnv(id, RuntimeObject(RuntimeFuncObject(pageName, index)))
                    continue
                }
                val refers = pageRefer[currentPage.info.dataMap["desc"].toString()]!!
                for (page in refers) {
                    value = page.info.getValueCallByName(name)
                    if (value != null) {
                        func.addEnv(id, value.value())
                        continue@FOR_LOOP
                    }
                    index = page.info.getAddressOfExportFunc(name)
                    if (index != -1) {
                        func.addEnv(id, RuntimeObject(RuntimeFuncObject(page.info
                                .dataMap["desc"].toString(), index)))
                        continue@FOR_LOOP
                    }
                }
                err(RuntimeError.WRONG_LOAD_EXTERN, name)
            } else {
                func.addEnv(id, currentStack.findVariable(func.page, id))
            }
        }
        currentStack.pushData(obj)
    }

    @Throws(RuntimeException::class)
    override fun opReloadFunc() {
        val idx = loadInt()
        val func = RuntimeFuncObject(currentStack.reg.pageId, currentStack.reg.execId)
        val obj = RuntimeObject(func)
        obj.symbol = currentPage.data[idx]
        currentStack.storeVariableDirect(idx, obj)
    }

    @Throws(RuntimeException::class)
    override fun opStore() {
        val idx = loadInt()
        val obj = load()
        val target = currentStack.findVariable(pageName, idx)
        target.copyFrom(obj)
        store(target)
    }

    @Throws(RuntimeException::class)
    override fun opStoreCopy() {
        val idx = loadInt()
        val obj = load()
        val target = currentStack.findVariable(pageName, idx)
        target.copyFrom(obj)
        store(target.clone())
    }

    @Throws(RuntimeException::class)
    override fun opStoreDirect() {
        val idx = loadInt()
        val obj = load()
        if (obj.symbol == null)
            obj.symbol = currentPage.data[idx]
        currentStack.storeVariableDirect(idx, obj)
        store(obj)
    }

    @Throws(RuntimeException::class)
    override fun opOpenFunc() {
        if (!currentStack.pushFuncData()) {
            err(RuntimeError.STACK_OVERFLOW)
        }
    }

    @Throws(RuntimeException::class)
    override fun opLoadArgs() {
        val idx = current()
        next()
        if (idx < 0 || idx >= currentStack.funcArgsCount) {
            err(RuntimeError.WRONG_ARGINVALID, "${currentStack.funcSimpleName} has >= $idx args bug only got ${currentStack.funcArgsCount} args")
        }
        store(currentStack.loadFuncArgs(idx))
    }

    @Throws(RuntimeException::class)
    override fun opPushArgs() {
        val obj = load()
        if (!currentStack.pushFuncArgs(obj)) {
            err(RuntimeError.ARG_OVERFLOW, obj.toString())
        }
    }

    @Throws(RuntimeException::class)
    override fun opReturn() {
        if (currentStack.isEmptyStack) {
            err(RuntimeError.NULL_STACK)
        }
        if (currentStack.isYield) {
            clearYieldStack()
        }
        currentStack.opReturn(currentStack.reg)
        switchPage()
    }

    @Throws(RuntimeException::class)
    override fun opCall() {
        val address = loadInt()
        currentStack.opCall(address, pageName, currentStack.reg.execId, pageName, currentPage
                .info.getFuncNameByAddress(address))
        currentStack.reg.execId = address
        currentStack.reg.pageId = pageName
    }

    @Throws(RuntimeException::class)
    override fun opPushNull() {
        store(RuntimeObject(null))
    }

    @Throws(RuntimeException::class)
    override fun opPushZero() {
        store(RuntimeObject(0))
    }

    @Throws(RuntimeException::class)
    override fun opPushNan() {
        store(RuntimeObject(null, RuntimeObjectType.kNan))
    }

    @Throws(RuntimeException::class)
    override fun opPushPtr(pc: Int) {
        store(RuntimeObject(pc))
    }

    @Throws(RuntimeException::class)
    override fun opPushObj(obj: RuntimeObject) {
        store(obj)
    }

    @Throws(RuntimeException::class)
    override fun opLoadVar() {
        val idx = loadInt()
        store(RuntimeObject.createObject(currentStack.findVariable(pageName, idx)))
    }

    @Throws(RuntimeException::class)
    override fun opJump() {
        currentStack.reg.execId = current()
    }

    @Throws(RuntimeException::class)
    override fun opJumpBool(bool: Boolean) {
        val tf = loadBool()
        if (tf == bool) {
            currentStack.reg.execId = current()
        } else {
            next()
        }
    }

    @Throws(RuntimeException::class)
    override fun opJumpBoolRetain(bool: Boolean) {
        val tf = loadBoolRetain()
        if (tf == bool) {
            currentStack.reg.execId = current()
        } else {
            next()
        }
    }

    @Throws(RuntimeException::class)
    override fun opJumpZero(bool: Boolean) {
        val `val` = loadInt()
        if (`val` == 0 == bool) {
            currentStack.reg.execId = current()
        } else {
            next()
        }
    }

    @Throws(RuntimeException::class)
    override fun opJumpYield() {
        val hash = RuntimeTools.getYieldHash(lastStack,
                currentStack.funcLevel, pageName, currentStack.reg.execId - 1)
        if (stkYieldMap.contains(hash)) {
            currentStack.reg.execId = current()
        } else {
            next()
        }
    }

    @Throws(RuntimeException::class)
    override fun opJumpNan() {
        val obj = top()
        if (obj.type == RuntimeObjectType.kNan) {
            currentStack.reg.execId = current()
        } else {
            next()
        }
    }

    @Throws(RuntimeException::class)
    override fun opImport() {
        val idx = loadInt()
        val obj = fetchFromGlobalData(idx)
        val name = obj.string
        if (ring == 3 && !name.startsWith("user.")) {
            err(RuntimeError.WRONG_IMPORT, name)
        }
        if (!pageMap.contains(name)) {
            err(RuntimeError.WRONG_IMPORT, name)
        }
        val page = pageMap[name]
        pageRefer[pageName]!!.add(page)
    }

    @Throws(RuntimeException::class)
    override fun opLoadExtern() {
        val idx = loadInt()
        val obj = fetchFromGlobalData(idx)
        val name = obj.string
        var value: RuntimeDebugValue? = currentPage.info.getValueCallByName(name)
        if (value != null) {
            currentStack.pushData(value.value())
            return
        }
        var index = currentPage.info.getAddressOfExportFunc(name)
        if (index != -1) {
            currentStack.pushData(RuntimeObject(RuntimeFuncObject(pageName, index)))
            return
        }
        val refers = pageRefer[currentPage.info.dataMap["desc"].toString()]!!
        for (page in refers) {
            value = page.info.getValueCallByName(name)
            if (value != null) {
                currentStack.pushData(value.value())
                return
            }
            index = page.info.getAddressOfExportFunc(name)
            if (index != -1) {
                currentStack.pushData(RuntimeObject(RuntimeFuncObject(page.info
                        .dataMap.get("desc").toString(), index)))
                return
            }
        }
        err(RuntimeError.WRONG_LOAD_EXTERN, name)
    }

    @Throws(Exception::class)
    override fun opCallExtern(invoke: Boolean) {
        val idx = loadInt()
        var name = ""
        if (invoke) {
            var obj: RuntimeObject? = null
            if (idx == -1) { // call (call exp) (args...)
                obj = load()
            } else {
                var i = currentStack.level
                while (i >= 0 && obj == null) {
                    obj = stack[i].findVariable(pageName, idx)
                    i = currentStack.parent
                }
            }
            when {
                null == obj?.type -> err(RuntimeError.WRONG_LOAD_EXTERN, idx.toString())
                obj.type == RuntimeObjectType.kFunc -> {
                    val func = obj.obj as RuntimeFuncObject
                    val env = func.env
                    for ((id, o) in env) {
                        currentStack.storeClosure(id, o)
                    }
                    currentStack.pushData(obj)
                    val address = func.addr
                    currentStack.opCall(address, func.page, currentStack.reg.execId,
                            pageName, pageMap[func.page].info
                            .getFuncNameByAddress(address))
                    currentStack.reg.execId = address
                    currentStack.reg.pageId = func.page
                    switchPage()
                    pop()
                    return
                }
                obj.type == RuntimeObjectType.kString -> name = obj.string
                else -> err(RuntimeError.WRONG_LOAD_EXTERN, obj.toString())
            }
        } else {
            val obj = fetchFromGlobalData(idx)
            name = obj.string
        }
        val refers = pageRefer[pageName]!!
        for (page in refers) {
            val address = page.info.getAddressOfExportFunc(name)
            if (address != -1) {
                val jmpPage = page.info.dataMap["desc"].toString()
                currentStack.opCall(address, jmpPage, currentStack.reg.execId,
                        currentStack.reg.pageId, name)
                currentStack.reg.execId = address
                currentStack.reg.pageId = jmpPage
                switchPage()
                return
            }
        }
        for (page in refers) {
            val exec = page.info.getExecCallByName(name) ?: continue
            val argsCount = currentStack.funcArgsCount
            val types = exec.type
            if (types.size != argsCount) {
                err(RuntimeError.WRONG_ARGCOUNT, name + " " + argsCount.toString())
            }
            val args = mutableListOf<RuntimeObject>()
            for (i in 0 until argsCount) {
                val type = if (types.size > i) types[i] else RuntimeObjectType.kObject
                val objParam = currentStack.loadFuncArgs(i)
                if (type != RuntimeObjectType.kObject) {
                    val objType = objParam.type
                    if (objType != type) {
                        val token = Token.createFromObject(objParam.obj)
                        val objTokenType = RuntimeObject.toTokenType(type)
                        if (objTokenType === TokenType.ERROR) {
                            err(RuntimeError.WRONG_ARGTYPE, name + " " + objTokenType.desc)
                        }
                        if (!TokenTools.promote(objTokenType, token)) {
                            err(RuntimeError.UNDEFINED_CONVERT, name + " " + token.toString() + " " + objTokenType.desc)
                        } else {
                            objParam.obj = token.obj
                        }
                    }
                }
                args.add(objParam)
            }
            currentStack.opCall(currentStack.reg.execId, currentStack.reg.pageId,
                    currentStack.reg.execId, currentStack.reg.pageId, name)
            val retVal: RuntimeObject?
            try {
                retVal = exec.call!!(args, this)
            } catch (e: RuntimeException) {
                if (e.error == RuntimeError.THROWS_EXCEPTION) {
                    opPushObj(RuntimeObject(e.info))
                    opThrow()
                    return
                }
                throw e
            }

            if (retVal == null) {
                store(RuntimeObject(null))
            } else {
                store(retVal)
            }
            opReturn()
            return
        }
        err(RuntimeError.WRONG_LOAD_EXTERN, name)
    }

    override fun getHelpString(name: String): String {
        val refers = pageRefer[pageName]!!
        refers.forEach { page ->
            val exec = page.info.getExecCallByName(name)
            if (exec != null) {
                return exec.doc
            }
        }
        return "过程不存在"
    }

    @Throws(RuntimeException::class)
    override fun getFuncAddr(name: String): Int {
        val refers = pageRefer[pageName]!!
        refers.forEach { page ->
            val address = page.info.getAddressOfExportFunc(name)
            if (address != -1) {
                return address
            }
        }
        err(RuntimeError.WRONG_FUNCNAME, name)
        return -1
    }

    @Throws(RuntimeException::class)
    override fun opYield(input: Boolean) {
        if (input) {
            enqueue(load())
        } else {
            store(dequeue())
        }
    }

    @Throws(RuntimeException::class)
    override fun opYieldSwitch(forward: Boolean) {
        if (forward) {
            val yldLine = current()
            next()
            val hash = RuntimeTools.getYieldHash(lastStack,
                    currentStack.funcLevel, pageName, yldLine)
            if (!stkYieldMap.contains(hash)) {
                err(RuntimeError.WRONG_COROUTINE, hash)
            }
            val newStack = stack[stkYieldMap[hash]]
            currentStack = newStack
        } else {
            if (currentStack.level == 0) {
                err(RuntimeError.WRONG_COROUTINE)
            }
            if (currentStack.parent == -1) {
                err(RuntimeError.WRONG_COROUTINE)
            }
            currentStack = stack[currentStack.parent]
        }
        switchPage()
    }

    @Throws(RuntimeException::class)
    private fun loadYieldData(): Int {
        val size = stkYieldData.size
        while (!stkYieldData.isEmpty()) {
            opYield(false)
        }
        return size
    }

    @Throws(RuntimeException::class)
    private fun loadYieldArgs(argsSize: Int) {
        for (i in 0 until argsSize) {
            opPushArgs()
        }
    }

    private fun clearYieldStack() {
        for (i in 0 until currentStack.yield) {
            stack.removeAt(stack.size - 1)
            stkYieldMap.pop()
        }
        currentStack.resetYield()
    }

    @Throws(Exception::class)
    override fun opYieldCreateContext() {
        val newStack = RuntimeStack(stack.size)
        newStack.parent = currentStack.level
        val yldLine = current()
        next()
        val hash = RuntimeTools.getYieldHash(currentStack.level,
                currentStack.funcLevel, pageName, yldLine)
        stkYieldMap.add(hash, newStack.level)
        currentStack.addYield()
        stack.add(newStack)
        refreshStack()
        val yieldSize = loadYieldData()
        val type = loadInt()
        opOpenFunc()
        loadYieldArgs(yieldSize - 2)
        when (type) {
            1 -> opCall()
            2 -> opCallExtern(true)
            3 -> opCallExtern(false)
        }
    }

    override fun opYieldDestroyContext() {
        val stk = lastStack
        stack.removeAt(stack.size - 1)
        stkYieldMap.pop()
        currentStack = stack[stk]
        currentStack.popYield()
    }

    @Throws(RuntimeException::class)
    override fun opScope(enter: Boolean) {
        if (currentStack.funcLevel == 0)
            err(RuntimeError.EMPTY_CALLSTACK)
        if (enter) {
            currentStack.enterScope()
        } else {
            currentStack.leaveScope()
        }
    }

    @Throws(RuntimeException::class)
    override fun opArr() {
        val size = current()
        next()
        val arr = RuntimeArray()
        for (i in 0 until size) {
            arr.add(load())
        }
        currentStack.pushData(RuntimeObject(arr))
    }

    @Throws(RuntimeException::class)
    override fun opMap() {
        val size = current()
        next()
        val map = RuntimeMap()
        for (i in 0 until size) {
            map.put(loadString(), load())
        }
        currentStack.pushData(RuntimeObject(map))
    }

    @Throws(RuntimeException::class)
    override fun opIndex() {
        val index = load()
        val obj = load()
        val typeIdx = index.type
        val typeObj = obj.type
        if (typeIdx == RuntimeObjectType.kInt) {
            if (typeObj == RuntimeObjectType.kArray) {
                currentStack.pushData(RuntimeObject(obj.array[index.int]))
                return
            } else if (typeObj == RuntimeObjectType.kString) {
                currentStack.pushData(RuntimeObject(obj.string[index.int]))
                return
            }
        } else if (typeIdx == RuntimeObjectType.kString) {
            if (typeObj == RuntimeObjectType.kMap) {
                currentStack.pushData(RuntimeObject(obj.map[index.obj.toString()]))
                return
            }
        }
        err(RuntimeError.WRONG_ARGTYPE, obj.toString() + ", " + index.toString())
    }

    @Throws(RuntimeException::class)
    override fun opIndexAssign() {
        val index = load()
        val obj = load()
        val exp = load()
        val typeIdx = index.type
        val typeObj = obj.type
        if (typeIdx == RuntimeObjectType.kInt) {
            if (typeObj == RuntimeObjectType.kArray) {
                obj.array[(index.obj as Long).toInt()] = exp
                currentStack.pushData(obj)
                return
            }
        } else if (typeIdx == RuntimeObjectType.kString) {
            if (typeObj == RuntimeObjectType.kMap) {
                obj.map.put(index.obj.toString(), exp)
                currentStack.pushData(obj)
                return
            }
        }
        err(RuntimeError.WRONG_ARGTYPE, obj.toString() + ", " + index.toString())
    }

    @Throws(RuntimeException::class)
    override fun opTry() {
        triesCount++
        val jmp = current()
        if (jmp != -1 && currentStack.trys != -1) {
            err(RuntimeError.DUP_EXCEPTION)
        }
        next()
        currentStack.trys = jmp
    }

    @Throws(RuntimeException::class)
    override fun opThrow() {
        val obj = load()
        if (triesCount <= 0) {
            currentStack.reg.execId--
            if (obj.type != RuntimeObjectType.kNull) {
                err(RuntimeError.THROWS_EXCEPTION, obj.obj.toString())
            } else {
                err(RuntimeError.THROWS_EXCEPTION, "NULL")
            }
        }
        if (currentStack.hasNoTry()) {
            while (currentStack.hasNoTry()) {
                opYieldSwitch(false)
                clearYieldStack()
            }
        }
        if (currentStack.hasCatch()) {
            pop()
            opScope(false)
        }
        while (currentStack.trys == -1) { // redirect to try stack
            currentStack.opReturn(currentStack.reg) // clear stack
        }
        currentStack.reg.execId = currentStack.trys
        store(obj)
        currentStack.resetTry()
        triesCount--
    }

    companion object {

        private val logger = Logger.getLogger("machine")
        private var modulesSystem: List<IInterpreterModule>
        private var modulesUser: List<IInterpreterModule>

        init {
            logger.debug("Loading modules...")
            modulesSystem = mutableListOf(ModuleBase.instance, ModuleMath.instance, ModuleList.instance, ModuleString.instance, ModuleProc.instance, ModuleFunction.instance, ModuleUI.instance, ModuleTask.instance, ModuleRemote.instance, ModuleLisp.instance, ModuleNet.instance, ModuleFile.instance, ModuleClass.instance, ModuleStdBase.instance, ModuleStdShell.instance)

            logger.debug("Loading user modules...")
            modulesUser = mutableListOf(ModuleUserBase.instance, ModuleUserWeb.instance, ModuleUserLisp.instance, ModuleUserCParser.instance)
        }
    }
}
