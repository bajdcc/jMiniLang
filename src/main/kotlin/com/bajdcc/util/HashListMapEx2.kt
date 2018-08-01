package com.bajdcc.util

/**
 * 双向哈希表
 * [K] -> [V]
 * @author bajdcc
 */
class HashListMapEx2<K, V> {

    private var map = mutableMapOf<K, V>()
    private var list = mutableListOf<K>()

    operator fun contains(k: K) = map.containsKey(k)

    fun add(k: K, v: V) {
        if (map.put(k, v) == null)
            list.add(k)
    }

    fun pop() {
        map.remove(list.get(list.size - 1))
        list.removeAt(list.size - 1)
    }

    operator fun get(k: K): V = map[k]!!

    fun copy() = HashListMapEx2<K, V>().also {
        it.map = this.map.toMutableMap()
        it.list = this.list.toMutableList()
    }

    fun toList() : List<V> = map.toList().map { it.second }
}