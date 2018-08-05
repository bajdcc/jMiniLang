package com.bajdcc.LALR1.grammar.runtime.service

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray
import org.apache.log4j.Logger

import java.util.*

/**
 * 【运行时】运行时管道服务
 *
 * @author bajdcc
 */
class RuntimePipeService(private val service: RuntimeService) : IRuntimePipeService {
    private val arrPipes = Array<PipeStruct?>(MAX_PIPE) { null }
    private val setPipeId = mutableSetOf<Int>()
    private val mapPipeNames = mutableMapOf<String, Int>()
    private var cyclePtr = 0

    internal data class PipeStruct(val name: String, val page: String, val queue : Queue<Char> = ArrayDeque<Char>(), val waiting_pids: Queue<Int> = ArrayDeque<Int>())

    private fun encodeHandle(handle: Int): Int {
        return handle + OFFSET_PIPE
    }

    private fun decodeHandle(handle: Int): Int {
        return handle - OFFSET_PIPE
    }

    override fun create(name: String, page: String): Int {
        if (setPipeId.size >= MAX_PIPE) {
            return -1
        }
        if (mapPipeNames.containsKey(name)) {
            return encodeHandle(mapPipeNames[name]!!)
        }
        val handle: Int
        while (true) {
            if (arrPipes[cyclePtr] == null) {
                handle = cyclePtr
                setPipeId.add(cyclePtr)
                mapPipeNames[name] = cyclePtr
                arrPipes[cyclePtr++] = PipeStruct(name, page)
                if (cyclePtr >= MAX_PIPE) {
                    cyclePtr -= MAX_PIPE
                }
                break
            }
            cyclePtr++
            if (cyclePtr >= MAX_PIPE) {
                cyclePtr -= MAX_PIPE
            }
        }
        logger.debug("Pipe #$handle '$name' created")
        return encodeHandle(handle)
    }

    override fun destroy(handle: Int): Boolean {
        var h = handle
        h = decodeHandle(h)
        if (!setPipeId.contains(h)) {
            return false
        }
        arrPipes[h]!!.waiting_pids.forEach { pid ->
            service.processService.wakeup(pid)
        }
        logger.debug("Pipe #" + h + " '" + arrPipes[h]!!.name + "' destroyed")
        mapPipeNames.remove(arrPipes[h]!!.name)
        arrPipes[h] = null
        setPipeId.remove(h)
        return true
    }

    override fun destroyByName(pid: Int, name: String): Boolean {
        return if (!mapPipeNames.containsKey(name)) {
            false
        } else destroy(encodeHandle(mapPipeNames[name]!!))
    }

    override fun read(pid: Int, handle: Int): Char {
        var h = handle
        h = decodeHandle(h)
        if (!setPipeId.contains(h)) {
            return '\uffff'
        }
        val ps = arrPipes[h]!!
        if (ps.queue.isEmpty()) { // 阻塞进程
            service.processService.block(pid)
            arrPipes[h]!!.waiting_pids.add(pid) // 放入等待队列
            return '\ufffe'
        }
        return ps.queue.poll()
    }

    override fun readNoBlock(pid: Int, handle: Int): Char {
        var h = handle
        h = decodeHandle(h)
        if (!setPipeId.contains(h)) {
            return '\uffff'
        }
        val ps = arrPipes[h]!!
        return if (ps.queue.isEmpty()) { // 阻塞进程
            '\ufffe'
        } else ps.queue.poll()
    }

    override fun write(handle: Int, ch: Char): Boolean {
        var h = handle
        h = decodeHandle(h)
        if (setPipeId.contains(h)) {
            if (!arrPipes[h]!!.waiting_pids.isEmpty()) {
                val pid = arrPipes[h]!!.waiting_pids.poll()
                if (pid != null)
                    service.processService.wakeup(pid)
            }
            return arrPipes[h]!!.queue.add(ch)
        }
        return false
    }

    override fun writeString(name: String, data: String): Boolean {
        if (!mapPipeNames.containsKey(name)) {
            return false
        }
        val h = mapPipeNames[name]!!
        if (!arrPipes[h]!!.waiting_pids.isEmpty()) {
            val pid = arrPipes[h]!!.waiting_pids.poll()
            if (pid != null)
                service.processService.wakeup(pid)
        }
        data.forEach { arrPipes[h]!!.queue.add(it) }
        return true
    }

    override fun writeStringNew(name: String, data: String) {
        if (!mapPipeNames.containsKey(name)) {
            create(name, "/system/pipe")
        }
        val h = mapPipeNames[name]!!
        if (!arrPipes[h]!!.waiting_pids.isEmpty()) {
            val pid = arrPipes[h]!!.waiting_pids.poll()
            if (pid != null)
                service.processService.wakeup(pid)
        }
        for (i in 0 until data.length) {
            arrPipes[h]!!.queue.add(data[i])
        }
    }

    override fun readAndDestroy(name: String): String? {
        if (!mapPipeNames.containsKey(name)) {
            return null
        }
        val h = mapPipeNames[name]!!
        val ps = arrPipes[h]!!
        if (ps.queue.isEmpty()) {
            destroy(encodeHandle(h))
            return ""
        }
        val sb = StringBuilder()
        while (!ps.queue.isEmpty()) {
            sb.append(ps.queue.poll())
        }
        destroy(encodeHandle(h))
        return sb.toString()
    }

    override fun readAll(name: String): String? {
        if (!mapPipeNames.containsKey(name)) {
            return null
        }
        val h = mapPipeNames[name]!!
        val ps = arrPipes[h]!!
        if (ps.queue.isEmpty()) {
            return ""
        }
        val sb = StringBuilder()
        while (!ps.queue.isEmpty()) {
            sb.append(ps.queue.poll())
        }
        return sb.toString()
    }

    override fun hasData(handle: Int): Boolean {
        var h = handle
        h = decodeHandle(h)
        return setPipeId.contains(h) && !arrPipes[h]!!.queue.isEmpty()
    }

    override fun query(name: String): Boolean {
        return mapPipeNames.containsKey(name)
    }

    override fun size(): Long {
        return setPipeId.size.toLong()
    }

    override fun stat(api: Boolean): RuntimeArray {
        val array = RuntimeArray()
        if (api) {
            mapPipeNames.values.sortedBy { it }
                    .asSequence()
                    .map { Pair(it, arrPipes[it]!!) }
                    .forEach {
                        val item = RuntimeArray()
                        item.add(RuntimeObject(it.first.toString()))
                        item.add(RuntimeObject(it.second.name))
                        item.add(RuntimeObject(it.second.page))
                        item.add(RuntimeObject(it.second.queue.size))
                        item.add(RuntimeObject(it.second.waiting_pids.size))
                        array.add(RuntimeObject(item))
                    }
        } else {
            array.add(RuntimeObject(String.format("   %-5s   %-20s   %-15s   %-15s",
                    "Id", "Name", "Queue", "Waiting")))
            mapPipeNames.values.sortedBy { it }
                    .asSequence()
                    .map { Pair(it, arrPipes[it]!!) }
                    .forEach {
                        array.add(RuntimeObject(String.format("   %-5s   %-20s   %-15d   %-15d",
                                it.first.toString(),
                                it.second.name,
                                it.second.queue.size,
                                it.second.waiting_pids.size)))
                    }
        }
        return array
    }

    companion object {

        private val logger = Logger.getLogger("pipe")
        private const val OFFSET_PIPE = 10000
        private const val MAX_PIPE = 1000
    }
}
