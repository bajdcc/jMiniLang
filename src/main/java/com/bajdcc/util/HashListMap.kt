package com.bajdcc.util

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap

/**
 * 双向哈希表
 * [T] -> Int
 * @author bajdcc
 */
class HashListMap<T> {

    private val map: BiMap<T, Int> = HashBiMap.create()

    operator fun contains(t: T): Boolean {
        return map.containsKey(t)
    }

    fun add(t: T) {
        if (!map.containsKey(t)) {
            map[t] = map.size
        }
    }

    fun put(t: T): Int {
        if (map.containsKey(t)) {
            return map[t]!!
        }
        add(t)
        return map.size - 1
    }

    fun indexOf(t: T): Int {
        return map[t]!!
    }

    fun forEach(f: (T) -> Unit) = map.forEach { f(it.key) }
    fun toList() : List<T> = map.toList().map { it.first!! }
    fun get(index: Int): T = map.inverse()[index]!!
}