package com.bajdcc.util

import java.util.*

/**
 * 二重布尔矩阵
 *
 * @author bajdcc
 */
class BitVector2(nx: Int, ny: Int) : Cloneable {
    /**
     * 内部一重布尔数组
     */
    internal val bs: BitSet

    /**
     * 行数
     */
    internal val nX = nx

    /**
     * 列数
     */
    internal val nY = ny

    /**
     * 全部置位
     */
    fun set() {
        bs.set(0, nX * nY - 1)
    }

    /**
     * 置位
     *
     * @param x 行
     * @param y 列
     */
    operator fun set(x: Int, y: Int) {
        bs.set(x * nX + y)
    }

    /**
     * 置位
     *
     * @param x     行
     * @param y     列
     * @param value 设置的值
     */
    operator fun set(x: Int, y: Int, value: Boolean) {
        bs.set(x * nX + y, value)
    }

    /**
     * 位置测试
     *
     * @param x 行
     * @param y 列
     * @return 位
     */
    fun test(x: Int, y: Int): Boolean {
        return bs.get(x * nX + y)
    }

    /**
     * 全部清零
     */
    fun clear() {
        bs.clear()
    }

    /**
     * 清零
     *
     * @param x 行
     * @param y 列
     */
    fun clear(x: Int, y: Int) {
        bs.clear(x * nX + y)
    }

    public override fun clone(): Any {
        return super.clone()
    }

    init {
        if (nx <= 0 || ny <= 0) {
            throw NegativeArraySizeException()
        }
        bs = BitSet(nx * ny)
    }
}
