package com.bajdcc.util

import com.google.common.collect.HashBiMap

/**
 * 双向哈希表
 * [K] -> [V]
 * @author bajdcc
 */
class HashListMapEx<K, V> {

    private var map = HashBiMap.create<K, V>()

    operator fun contains(k: K) = map.containsKey(k)

    fun add(k: K, v: V) = map.put(k, v)

    fun put(k: K, v: V) = map.put(k, v) != null

    operator fun get(k: K): V = map[k]!!

    fun copy() = HashListMapEx<K, V>().also {
        it.map = this.map
    }

    fun toList() : List<V> = map.toList().map { it.second }
}