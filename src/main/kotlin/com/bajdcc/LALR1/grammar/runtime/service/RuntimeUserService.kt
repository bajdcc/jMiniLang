package com.bajdcc.LALR1.grammar.runtime.service

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject
import com.bajdcc.LALR1.grammar.runtime.RuntimeObjectType
import com.bajdcc.LALR1.grammar.runtime.data.*
import org.apache.log4j.Logger
import sun.reflect.generics.reflectiveObjects.NotImplementedException

import java.util.*

/**
 * 【运行时】运行时用户服务
 *
 * @author bajdcc
 */
class RuntimeUserService(private val service: RuntimeService) : IRuntimeUserService, IRuntimeUserService.IRuntimeUserPipeService, IRuntimeUserService.IRuntimeUserShareService, IRuntimeUserService.IRuntimeUserFileService {

    private val fsNodeRoot = RuntimeFsNode.root()
    private val mapNames = mutableMapOf<String, Int>()
    private val arrUsers = Array<UserStruct?>(MAX_USER) { null }
    private val setUserId = mutableSetOf<Int>()
    private var cyclePtr = 0

    override val pipe: IRuntimeUserService.IRuntimeUserPipeService
        get() = this

    override val share: IRuntimeUserService.IRuntimeUserShareService
        get() = this

    override val file: IRuntimeUserService.IRuntimeUserFileService
        get() = this

    internal enum class UserType(var desc: String) {
        PIPE("管道"),
        SHARE("共享"),
        FILE("文件")
    }

    internal interface IUserPipeHandler {
        /**
         * 读取管道
         * @return 读取的对象
         */
        fun read(): RuntimeObject

        /**
         * 写入管道
         * @param obj 写入的对象
         * @return 是否成功
         */
        fun write(obj: RuntimeObject): Boolean
    }

    internal interface IUserShareHandler {
        /**
         * 获取共享
         * @return 共享对象
         */
        fun get(): RuntimeObject?

        /**
         * 设置共享
         * @param obj 共享对象
         * @return 上次保存的内容
         */
        fun set(obj: RuntimeObject?): RuntimeObject?

        /**
         * 锁定共享
         * @return 是否成功
         */
        fun lock(): Boolean

        /**
         * 解锁共享
         * @return 是否成功
         */
        fun unlock(): Boolean
    }

    internal interface IUserFileHandler {

        /**
         * 查询文件
         * @return 0-不存在，1-文件，2-文件夹
         */
        fun query(): Long

        /**
         * 创建文件
         * @param file 是否创建文件
         * @return 是否成功
         */
        fun create(file: Boolean): Boolean

        /**
         * 删除文件
         * @return 是否成功
         */
        fun delete(): Boolean

        /**
         * 读取文件
         * @return 是否成功
         */
        fun read(): ByteArray?

        /**
         * 写入文件
         * @param data 数据
         * @param overwrite 是否覆盖
         * @param createIfNotExist 是否自动创建
         * @return 0-成功，-1:自动创建失败，-2:文件不存在，-3:目标不是文件
         */
        fun write(data: ByteArray, overwrite: Boolean, createIfNotExist: Boolean): Long
    }

    internal interface IUserHandler {

        val pipe: IUserPipeHandler

        val share: IUserShareHandler

        val file: IUserFileHandler

        fun destroy()

        fun enqueue(pid: Int)

        fun dequeue()

        fun dequeue(pid: Int)
    }

    internal open inner class UserHandler(protected var id: Int, private val waitingPids: Deque<Int> = ArrayDeque<Int>()) : IUserHandler {

        protected val isEmpty: Boolean
            get() = waitingPids.isEmpty()

        override val pipe: IUserPipeHandler
            get() = throw NotImplementedException()

        override val share: IUserShareHandler
            get() = throw NotImplementedException()

        override val file: IUserFileHandler
            get() = throw NotImplementedException()

        override fun destroy() {
            service.processService.ring3.removeHandle(id)
        }

        override fun enqueue(pid: Int) {
            waitingPids.add(pid)
            service.processService.block(pid)
            service.processService.ring3.blockHandle = id
        }

        override fun dequeue() {
            if (waitingPids.isEmpty())
                return
            val pid = waitingPids.poll()
            service.processService.ring3.blockHandle = -1
            service.processService.wakeup(pid)
        }

        override fun dequeue(pid: Int) {
            waitingPids.remove(pid)
        }

        override fun toString(): String {
            return String.format("队列：%s", waitingPids.toString())
        }
    }

    internal inner class UserPipeHandler(id: Int) : UserHandler(id), IUserPipeHandler {
        private val queue: Queue<RuntimeObject>

        override val pipe: IUserPipeHandler
            get() = this

        init {
            this.queue = ArrayDeque()
        }

        override fun read(): RuntimeObject {
            if (queue.isEmpty()) {
                val pid = service.processService.pid
                enqueue(pid)
                return RuntimeObject(false, RuntimeObjectType.kNoop)
            }
            return queue.poll()
        }

        override fun write(obj: RuntimeObject): Boolean {
            queue.add(obj)
            dequeue()
            return true
        }

        override fun toString(): String {
            return String.format("%s 管道：%s", super.toString(), queue.toString())
        }
    }

    internal inner class UserShareHandler(id: Int) : UserHandler(id), IUserShareHandler {

        private var obj: RuntimeObject? = null
        private var mutex = false

        override val share: IUserShareHandler
            get() = this

        override fun get(): RuntimeObject? {
            return obj
        }

        override fun set(obj: RuntimeObject?): RuntimeObject? {
            val tmp = this.obj
            this.obj = obj
            return tmp
        }

        override fun lock(): Boolean {
            if (mutex) {
                val pid = service.processService.pid
                enqueue(pid)
            } else {
                mutex = true
            }
            return true
        }

        override fun unlock(): Boolean {
            return if (mutex) {
                if (isEmpty) {
                    mutex = false
                } else {
                    dequeue()
                }
                true
            } else {
                false
            }
        }

        override fun toString(): String {
            return String.format("%s 共享：%s", super.toString(), obj.toString())
        }
    }

    internal inner class UserFileHandler(id: Int) : UserHandler(id), IUserFileHandler {

        private val path: String
            get() = arrUsers[id]?.name?.trimEnd('/') ?: "UNKNOWN"

        override fun query() = fsNodeRoot.query(path)

        override fun create(file: Boolean) = fsNodeRoot.createNode(path, file) != null

        override fun delete() = fsNodeRoot.deleteNode(path)

        override fun read() = fsNodeRoot.read(path)

        override fun write(data: ByteArray, overwrite: Boolean, createIfNotExist: Boolean) =
                fsNodeRoot.write(path, data, overwrite, createIfNotExist)

        override val file: IUserFileHandler
            get() = this
    }

    internal inner class UserStruct(var name: String, var page: String, var type: UserType, var handler: IUserHandler)

    private fun newId(): Int {
        while (true) {
            if (arrUsers[cyclePtr] == null) {
                val id = cyclePtr
                setUserId.add(id)
                cyclePtr++
                if (cyclePtr >= MAX_USER) {
                    cyclePtr -= MAX_USER
                }
                service.processService.ring3.addHandle(id)
                return id
            }
            cyclePtr++
            if (cyclePtr >= MAX_USER) {
                cyclePtr -= MAX_USER
            }
        }
    }

    override fun create(name: String, page: String): Int {
        if (setUserId.size >= MAX_USER) {
            return -1
        }
        val sp = name.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (sp.size != 2)
            return -2
        if (sp[0] == "pipe") {
            return createPipe(sp[1], page)
        }
        if (sp[0] == "share") {
            return createShare(sp[1], page)
        }
        return if (sp[0] == "file") {
            createFile(sp[1], page)
        } else -3
    }

    override fun read(id: Int): RuntimeObject {
        if (arrUsers[id] == null) {
            return RuntimeObject(true, RuntimeObjectType.kNoop)
        }
        val user = arrUsers[id]!!
        return user.handler.pipe.read()
    }

    override fun write(id: Int, obj: RuntimeObject): Boolean {
        if (arrUsers[id] == null) {
            return false
        }
        val user = arrUsers[id]!!
        return user.handler.pipe.write(obj)
    }

    override fun get(id: Int): RuntimeObject? {
        if (arrUsers[id] == null) {
            return null
        }
        val user = arrUsers[id]!!
        return user.handler.share.get()
    }

    override fun set(id: Int, obj: RuntimeObject?): RuntimeObject? {
        if (arrUsers[id] == null) {
            return null
        }
        val user = arrUsers[id]!!
        return user.handler.share.set(obj)
    }

    override fun lock(id: Int): Boolean {
        if (arrUsers[id] == null) {
            return false
        }
        val user = arrUsers[id]!!
        return user.handler.share.lock()
    }

    override fun unlock(id: Int): Boolean {
        if (arrUsers[id] == null) {
            return false
        }
        val user = arrUsers[id]!!
        return user.handler.share.unlock()
    }

    override fun queryFile(id: Int): Long {
        if (arrUsers[id] == null) {
            return -1L
        }
        val user = arrUsers[id]!!
        return user.handler.file.query()
    }

    override fun createFile(id: Int, file: Boolean): Boolean {
        if (arrUsers[id] == null) {
            return false
        }
        val user = arrUsers[id]!!
        return user.handler.file.create(file)
    }

    override fun deleteFile(id: Int): Boolean {
        if (arrUsers[id] == null) {
            return false
        }
        val user = arrUsers[id]!!
        return user.handler.file.delete()
    }

    override fun readFile(id: Int): ByteArray? {
        if (arrUsers[id] == null) {
            return null
        }
        val user = arrUsers[id]!!
        return user.handler.file.read()
    }

    override fun writeFile(id: Int, data: ByteArray, overwrite: Boolean, createIfNotExist: Boolean): Long {
        if (arrUsers[id] == null) {
            return -1L
        }
        val user = arrUsers[id]!!
        return user.handler.file.write(data, overwrite, createIfNotExist)
    }

    override fun destroy() {
        val handles = service.processService.ring3.handles.toList()
        handles.forEach { handle ->
            destroy(handle)
        }
        val blockHandle = service.processService.ring3.blockHandle
        if (blockHandle != -1) {
            if (setUserId.contains(blockHandle)) {
                val us = arrUsers[blockHandle]!!
                assert(us.type == UserType.PIPE)
                us.handler.dequeue(service.processService.pid)
            }
        }
    }

    override fun destroy(id: Int): Boolean {
        if (arrUsers[id] == null)
            return false
        val user = arrUsers[id]!!
        if (!mapNames.containsKey(user.name))
            return false
        logger.debug("${user.type} '${user.name}' #$id destroyed")
        setUserId.remove(id)
        mapNames.remove(user.name)
        user.handler.destroy()
        arrUsers[id] = null
        return true
    }

    override fun stat(api: Boolean): RuntimeArray {
        val array = RuntimeArray()
        if (api) {
            mapNames.values.sortedBy { it }
                    .forEach { value ->
                        val item = RuntimeArray()
                        item.add(RuntimeObject(value.toLong()))
                        item.add(RuntimeObject(arrUsers[value]!!.type.desc))
                        item.add(RuntimeObject(arrUsers[value]!!.name))
                        item.add(RuntimeObject(arrUsers[value]!!.page))
                        item.add(RuntimeObject(arrUsers[value]!!.handler.toString()))
                        array.add(RuntimeObject(item))
                    }
        } else {
            array.add(RuntimeObject(String.format("   %-5s   %-15s   %-15s   %-20s",
                    "Id", "Name", "Type", "Description")))
            mapNames.values.sortedBy { it }
                    .forEach { value ->
                        array.add(
                                RuntimeObject(String.format("   %-5s   %-15s   %-15s   %-20s",
                                        value.toLong(), arrUsers[value]!!.name,
                                        arrUsers[value]!!.type.toString(), arrUsers[value]!!.handler.toString())))
                    }
        }
        return array
    }

    private fun createPipe(name: String, page: String): Int {
        if (mapNames.containsKey(name)) {
            return mapNames.getOrDefault(name, -11)
        }
        val id = newId()
        logger.debug("${UserType.PIPE} '$name' #$id created")
        mapNames[name] = id
        arrUsers[id] = UserStruct(name, page, UserType.PIPE, UserPipeHandler(id))
        return id
    }

    private fun createShare(name: String, page: String): Int {
        if (mapNames.containsKey(name)) {
            return mapNames.getOrDefault(name, -21)
        }
        val id = newId()
        logger.debug("${UserType.SHARE} '$name' #$id created")
        mapNames[name] = id
        arrUsers[id] = UserStruct(name, page, UserType.SHARE, UserShareHandler(id))
        service.processService.ring3.addHandle(id)
        return id
    }

    private fun createFile(name: String, page: String): Int {
        if (mapNames.containsKey(name)) {
            return mapNames.getOrDefault(name, -31)
        }
        val id = newId()
        logger.debug("${UserType.FILE} '$name' #$id created")
        mapNames[name] = id
        arrUsers[id] = UserStruct(name, page, UserType.FILE, UserFileHandler(id))
        service.processService.ring3.addHandle(id)
        return id
    }

    companion object {

        private const val MAX_USER = 1000
        private val logger = Logger.getLogger("user")
    }
}
