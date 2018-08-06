package com.bajdcc.LALR1.grammar.runtime.service

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray
import org.apache.log4j.Logger

/**
 * 【运行时】运行时共享服务
 *
 * @author bajdcc
 */
class RuntimeShareService(private val service: RuntimeService) : IRuntimeShareService {
    private val mapShares = mutableMapOf<String, ShareStruct>()

    internal data class ShareStruct(var name: String, var obj: RuntimeObject, var page: String,
                           var reference: Int = 1, var locked: Boolean = false) {
        val objType: String
            get() = obj.type.desc
    }

    override fun startSharing(name: String, obj: RuntimeObject, page: String): Int {
        if (mapShares.size >= MAX_SHARING)
            return -1
        if (mapShares.containsKey(name))
            return 0
        mapShares[name] = ShareStruct(name, obj, page)
        logger.debug("Sharing '$name' created")
        return 1
    }

    override fun createSharing(name: String, obj: RuntimeObject, page: String): Int {
        if (mapShares.size >= MAX_SHARING)
            return -1
        mapShares[name] = ShareStruct(name, obj, page)
        logger.debug("Sharing '$name' created")
        return 1
    }

    override fun getSharing(name: String, reference: Boolean): RuntimeObject {
        val ss = mapShares[name]
        if (ss != null) {
            if (reference)
                ss.reference++
            return ss.obj
        }
        return RuntimeObject(null)
    }

    override fun stopSharing(name: String): Int {
        if (!mapShares.containsKey(name))
            return -1
        val ss = mapShares[name]!!
        ss.reference--
        if (ss.reference == 0) {
            mapShares.remove(name)
            return 1
        }
        return if (ss.reference < 0) {
            2
        } else 0
    }

    override fun isLocked(name: String): Boolean {
        return mapShares.containsKey(name) && mapShares[name]!!.locked
    }

    override fun setLocked(name: String, lock: Boolean) {
        if (mapShares.containsKey(name))
            mapShares[name]!!.locked = lock
    }

    override fun size(): Long {
        return mapShares.size.toLong()
    }

    override fun stat(api: Boolean): RuntimeArray {
        val array = RuntimeArray()
        if (api) {
            mapShares.values.sortedBy { it.objType }.sortedBy { it.name }
                    .forEach { value ->
                        val item = RuntimeArray()
                        item.add(RuntimeObject(value.name))
                        item.add(RuntimeObject(value.obj.type.desc))
                        item.add(RuntimeObject(value.page))
                        item.add(RuntimeObject(value.reference.toString()))
                        item.add(RuntimeObject(if (value.locked) "是" else "否"))
                        array.add(RuntimeObject(item))
                    }
        } else {
            array.add(RuntimeObject(String.format("   %-20s   %-15s   %-5s   %-5s",
                    "Name", "Type", "Ref", "Locked")))
            mapShares.values.sortedBy { it.objType }.sortedBy { it.name }
                    .forEach { value ->
                        array.add(RuntimeObject(String.format("   %-20s   %-15s   %-5s   %-5s",
                                value.name, value.obj.type.desc, value.reference.toString(), value.locked.toString())))
                    }
        }
        return array
    }

    companion object {

        private val logger = Logger.getLogger("share")
        private const val MAX_SHARING = 1000
    }
}
