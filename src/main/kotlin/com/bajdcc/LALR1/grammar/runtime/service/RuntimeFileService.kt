package com.bajdcc.LALR1.grammar.runtime.service

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray
import org.apache.log4j.Logger
import java.io.*
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*

/**
 * 【运行时】运行时文件服务
 *
 * @author bajdcc
 */
class RuntimeFileService(private val service: RuntimeService) : IRuntimeFileService {
    private val arrFiles = Array<FileStruct?>(MAX_FILE) { null }
    private val setFileId = mutableSetOf<Int>()
    private val mapFileNames = mutableMapOf<String, Int>()
    private var cyclePtr = 0
    private val mapVfs = mutableMapOf<String, VfsStruct>()

    override val vfsListSize: Long
        get() = mapVfs.size.toLong()

    override fun addVfs(name: String, content: String) {
        mapVfs[VFS_PREFIX + name] = VfsStruct(content.toByteArray(UTF_8), true)
    }

    override fun getVfs(name: String): String {
        return if (mapVfs.containsKey(name)) String(mapVfs[name]!!.data!!) else ""
    }

    private fun encodeHandle(handle: Int): Int {
        return handle + OFFSET_FILE
    }

    private fun decodeHandle(handle: Int): Int {
        return handle - OFFSET_FILE
    }

    override fun create(name: String, mode: Int, encoding: String, page: String): Int {
        if (setFileId.size >= MAX_FILE) {
            return -1
        }
        if (mapFileNames.containsKey(name)) {
            return -1
        }
        val handle: Int
        val fs = FileStruct(name, mode, encoding, mapVfs, page)
        if (fs.status == FileStatus.ERROR) {
            return -1
        }
        while (true) {
            if (arrFiles[cyclePtr] == null) {
                handle = cyclePtr
                setFileId.add(cyclePtr)
                mapFileNames[name] = cyclePtr
                arrFiles[cyclePtr++] = fs
                if (cyclePtr >= MAX_FILE) {
                    cyclePtr -= MAX_FILE
                }
                break
            }
            cyclePtr++
            if (cyclePtr >= MAX_FILE) {
                cyclePtr -= MAX_FILE
            }
        }
        logger.debug("File #$handle '$name' created")
        return encodeHandle(handle)
    }

    override fun destroy(handle: Int): Boolean {
        var h = handle
        h = decodeHandle(h)
        if (!setFileId.contains(h)) {
            return false
        }
        val fs = arrFiles[h]!!
        logger.debug("File #" + h + " '" + fs.name + "' destroyed")
        if (fs.vfs) {
            mapFileNames.remove(fs.name)
            fs.destroy(mapVfs)
            arrFiles[h] = null
            setFileId.remove(h)
        } else {
            mapFileNames.remove(fs.name)
            fs.destroy()
            arrFiles[h] = null
            setFileId.remove(h)
        }
        return true
    }

    internal data class VfsStruct(val data: ByteArray? = null, val readonly: Boolean = false) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as VfsStruct
            if (!Arrays.equals(data, other.data)) return false
            if (readonly != other.readonly) return false
            return true
        }

        override fun hashCode(): Int {
            var result = data?.let { Arrays.hashCode(it) } ?: 0
            result = 31 * result + readonly.hashCode()
            return result
        }
    }

    override fun read(handle: Int): Int {
        var h = handle
        h = decodeHandle(h)
        return if (!setFileId.contains(h)) {
            -1
        } else arrFiles[h]!!.read()
    }

    override fun readString(handle: Int): String? {
        var h = handle
        h = decodeHandle(h)
        return if (!setFileId.contains(h)) {
            null
        } else arrFiles[h]!!.readString()
    }

    override fun write(handle: Int, ch: Char): Boolean {
        var h = handle
        h = decodeHandle(h)
        return setFileId.contains(h) && arrFiles[h]!!.write(ch)
    }

    override fun writeString(handle: Int, str: String): Boolean {
        var h = handle
        h = decodeHandle(h)
        return setFileId.contains(h) && arrFiles[h]!!.writeString(str)
    }

    override fun exists(filename: String): Boolean {
        return when {
            filename.startsWith(VFS_PREFIX) -> mapVfs.containsKey(filename)
            filename.startsWith(RESOURCE_PREFIX) -> javaClass.getResourceAsStream(filename.substring(1)) != null
            else -> mapFileNames.containsKey(filename)
        }
    }

    override fun readAll(filename: String): String? {
        if (filename.startsWith(VFS_PREFIX)) {
            if (mapVfs.containsKey(filename)) {
                return String(mapVfs[filename]!!.data!!, UTF_8)
            }
        }
        return null
    }

    override fun size(): Long {
        return mapFileNames.size.toLong()
    }

    override fun stat(api: Boolean): RuntimeArray {
        val modeString = arrayOf("", "读取", "截断", "追加")
        val statusString = arrayOf("错误", "读取", "写入")
        val array = RuntimeArray()
        if (api) {
            mapFileNames.values.sortedBy { it }
                    .asSequence()
                    .map { a -> arrFiles[a]!! }
                    .forEach { value ->
                        val item = RuntimeArray()
                        item.add(RuntimeObject(value.name))
                        item.add(RuntimeObject(modeString[value.mode]))
                        item.add(RuntimeObject(value.page))
                        item.add(RuntimeObject(statusString[value.status.ordinal]))
                        item.add(RuntimeObject(value.encoding))
                        item.add(RuntimeObject(if (value.vfs) "是" else "否"))
                        array.add(RuntimeObject(item))
                    }
        } else {
            array.add(RuntimeObject(String.format("   %-30s   %-5s   %-10s   %-10s",
                    "Name", "Mode", "Status", "Encoding")))
            mapFileNames.values.sortedBy { it }
                    .map { a -> arrFiles[a]!! }
                    .asSequence()
                    .forEach { value ->
                        array.add(RuntimeObject(String.format("   %-30s   %-5s   %-10s   %-10s",
                                value.name, value.mode, value.status.toString(), value.encoding)))
                    }
        }
        return array
    }

    override fun getVfsList(api: Boolean): RuntimeArray {
        val array = RuntimeArray()
        if (api) {
            mapVfs.entries.sortedBy { it.key }
                    .forEach { value ->
                        val item = RuntimeArray()
                        item.add(RuntimeObject(value.key))
                        item.add(RuntimeObject(value.value.data!!.size))
                        item.add(RuntimeObject(if (value.value.readonly) "是" else "否"))
                        array.add(RuntimeObject(item))
                    }
        } else {
            array.add(RuntimeObject(String.format("   %-25s   %-5s   %-5s",
                    "Name", "Size", "Readonly")))
            mapVfs.entries.sortedBy { it.key }
                    .forEach { value ->
                        array.add(RuntimeObject(String.format("   %-25s   %-15s   %-5s",
                                value.key, value.value.data!!.size, value.value.readonly)))
                    }
        }
        return array
    }

    override fun readAndDestroy(name: String): String? {
        if (name.startsWith(VFS_PREFIX)) {
            if (mapVfs.containsKey(name)) {
                val content = String(mapVfs[name]!!.data!!, UTF_8)
                mapVfs.remove(name)
                return content
            }
        }
        return null
    }

    internal enum class FileStatus {
        ERROR,
        READING,
        WRITING
    }

    internal class FileStruct(var name: String, val mode: Int, val encoding: String, mapVfs: MutableMap<String, VfsStruct>, val page: String) {
        var status: FileStatus
        var reader: BufferedReader? = null
        var writer: BufferedWriter? = null
        var vfs: Boolean = false
        var baos: ByteArrayOutputStream? = null

        init {
            this.status = FileStatus.ERROR
            this.vfs = false
            if (mode == 1) { // read
                if (name.startsWith(VFS_PREFIX)) {
                    if (mapVfs.containsKey(name)) {
                        try {
                            val bais = ByteArrayInputStream(mapVfs[name]!!.data!!)
                            val isr = InputStreamReader(bais, encoding)
                            reader = BufferedReader(isr)
                            this.status = FileStatus.READING
                            this.vfs = true
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                } else {
                    try {
                        val inputStream: InputStream = if (name.startsWith(RESOURCE_PREFIX)) {
                            javaClass.getResourceAsStream(name.substring(1))
                        } else {
                            FileInputStream(name)
                        }
                        val isr = InputStreamReader(inputStream, encoding)
                        reader = BufferedReader(isr)
                        this.status = FileStatus.READING
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            } else if (mode and 2 != 0) { // write
                if (name.startsWith(VFS_PREFIX)) {
                    val vs = mapVfs[name]
                    if (vs == null || !vs.readonly) {
                        try {
                            // mode:2=truncate mode:3=append
                            baos = ByteArrayOutputStream()
                            if (mapVfs.containsKey(name) && mode == 3) {
                                baos!!.write(mapVfs[name]!!.data!!)
                            }
                            val osw = OutputStreamWriter(baos!!, encoding)
                            writer = BufferedWriter(osw)
                            this.status = FileStatus.WRITING
                            this.vfs = true
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                } else {
                    try {
                        // mode:2=truncate mode:3=append
                        val fos = FileOutputStream(name, mode and 1 != 0)
                        val osw = OutputStreamWriter(fos, encoding)
                        writer = BufferedWriter(osw)
                        this.status = FileStatus.WRITING
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        }

        fun read(): Int {
            if (status != FileStatus.READING) {
                return -1
            }
            try {
                return reader!!.read()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return -1
        }

        fun readString(): String? {
            if (status != FileStatus.READING) {
                return null
            }
            try {
                return reader!!.readLine()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

        fun write(ch: Char): Boolean {
            if (status != FileStatus.WRITING) {
                return false
            }
            try {
                writer!!.write(ch.toInt())
                return true
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return false
        }

        fun writeString(str: String): Boolean {
            if (status != FileStatus.WRITING) {
                return false
            }
            try {
                writer!!.write(str)
                return true
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return false
        }

        fun destroy() {
            try {
                if (status == FileStatus.READING) {
                    reader!!.close()
                } else if (status == FileStatus.WRITING) {
                    writer!!.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun destroy(mapVfs: MutableMap<String, VfsStruct>) {
            if (!vfs)
                return
            try {
                if (status == FileStatus.READING) {
                    reader!!.close()
                } else if (status == FileStatus.WRITING) {
                    writer!!.flush()
                    mapVfs[name] = VfsStruct(baos!!.toByteArray())
                    writer!!.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    companion object {

        private const val OFFSET_FILE = 20000
        private const val MAX_FILE = 1000
        private const val VFS_PREFIX = "$"
        private const val RESOURCE_PREFIX = "!"
        private val logger = Logger.getLogger("file")
    }
}
