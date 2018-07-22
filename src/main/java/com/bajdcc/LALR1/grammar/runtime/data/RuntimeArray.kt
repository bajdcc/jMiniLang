package com.bajdcc.LALR1.grammar.runtime.data

import com.bajdcc.LALR1.grammar.runtime.IRuntimeStatus
import com.bajdcc.LALR1.grammar.runtime.RuntimeException
import com.bajdcc.LALR1.grammar.runtime.RuntimeObject

/**
 * 【运行时】运行时数组
 *
 * @author bajdcc
 */
class RuntimeArray : Cloneable {

    private var array = mutableListOf<RuntimeObject>()

    val isEmpty: Boolean
        get() = array.isEmpty()

    constructor()

    constructor(obj: RuntimeArray) {
        copyFrom(obj)
    }

    constructor(array: MutableList<RuntimeObject>) {
        this.array = array
    }

    fun add(obj: RuntimeObject) {
        array.add(obj)
    }

    fun add(arr: RuntimeArray) {
        array.addAll(arr.array)
    }

    fun insert(index: Int, obj: RuntimeObject) {
        array.add(index, obj)
    }

    operator fun set(index: Int, obj: RuntimeObject): Boolean {
        if (index >= 0 && index < array.size) {
            array[index] = obj
            return true
        }
        return false
    }

    fun pop(): RuntimeObject? {
        return if (array.isEmpty()) {
            null
        } else array.removeAt(array.size - 1)
    }

    fun distinct(): RuntimeArray {
        return RuntimeArray(array.distinct().toMutableList())
    }

    operator fun get(index: Int): RuntimeObject {
        return if (index >= 0 && index < array.size) {
            array[index]
        } else RuntimeObject(null)
    }

    @Throws(RuntimeException::class)
    operator fun get(index: Int, status: IRuntimeStatus): RuntimeObject {
        if (index >= 0 && index < array.size) {
            return array[index]
        }
        status.err(RuntimeException.RuntimeError.INVALID_INDEX, "array.get")
        throw IndexOutOfBoundsException()
    }

    operator fun contains(obj: RuntimeObject): Boolean {
        return array.contains(obj)
    }

    fun size(): RuntimeObject {
        return RuntimeObject(array.size.toLong())
    }

    fun length(): Int {
        return array.size
    }

    fun remove(index: Int): RuntimeObject {
        return if (index >= 0 && index < array.size) {
            array.removeAt(index)
        } else RuntimeObject(null)
    }

    fun delete(obj: RuntimeObject): RuntimeObject {
        return RuntimeObject(array.remove(obj))
    }

    fun reverse() {
        array.reverse()
    }

    fun clear() {
        array.clear()
    }

    /**
     * 深拷贝
     *
     * @param obj 原对象
     */
    fun copyFrom(obj: RuntimeArray) {
        array = obj.array.map{ it.clone() }.toMutableList()
    }

    fun toList(): List<Any> {
        return array.map { it.obj!! }.toList()
    }

    fun toStringList(): List<String> {
        return array.map { it.obj!!.toString() }.toList()
    }

    public override fun clone(): RuntimeArray {
        return super.clone() as RuntimeArray
    }

    fun getArray(): List<RuntimeObject> {
        return array.toList()
    }

    override fun toString(): String {
        return array.size.toString()
    }
}
