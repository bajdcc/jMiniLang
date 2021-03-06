package com.bajdcc.LALR1.grammar.runtime.data

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject

/**
 * 【运行时】运行时数组
 *
 * @author bajdcc
 */
class RuntimeMap : Cloneable {

    constructor()

    constructor(obj: RuntimeMap) {
        copyFrom(obj)
    }

    constructor(obj: Map<String, RuntimeObject>) {
        map = obj.toMutableMap()
    }

    private var map = mutableMapOf<String, RuntimeObject>()

    val isEmpty: Boolean
        get() = map.isEmpty()

    val keys: RuntimeArray
        get() = RuntimeArray(map.keys.map { RuntimeObject(it) }.toMutableList())

    val values: RuntimeArray
        get() = RuntimeArray(map.values.toMutableList())

    fun put(key: String, obj: RuntimeObject) {
        map[key] = obj
    }

    operator fun get(key: String): RuntimeObject? {
        return map[key]
    }

    fun size(): RuntimeObject {
        return RuntimeObject(map.size.toLong())
    }

    operator fun contains(key: String): Boolean {
        return map.containsKey(key)
    }

    fun remove(key: String): RuntimeObject? {
        return map.remove(key)
    }

    fun clear() {
        map.clear()
    }

    /**
     * 深拷贝
     *
     * @param obj 原对象
     */
    fun copyFrom(obj: RuntimeMap) {
        map = obj.map.entries.associate { it.key to it.value.clone() }.toMutableMap()
    }

    public override fun clone(): RuntimeMap {
        return super.clone() as RuntimeMap
    }

    fun getMap(): Map<String, RuntimeObject> {
        return map.toMap()
    }

    override fun toString(): String {
        return if (map.containsKey("__type__")) "class=" + map["__type__"].toString() else map.size.toString()
    }
}
