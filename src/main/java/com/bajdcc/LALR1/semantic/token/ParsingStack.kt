package com.bajdcc.LALR1.semantic.token

import com.bajdcc.util.lexer.token.Token
import java.util.*

/**
 * 单词解析栈
 *
 * @author bajdcc
 */
class ParsingStack : IIndexedData {

    /**
     * 当前索引表
     */
    private var mapTokenBag = mutableMapOf<Int, TokenBag>()

    /**
     * 单词包栈
     */
    private val stkMapTokenBags = Stack<MutableMap<Int, TokenBag>>()

    init {
        push()
    }

    /**
     * 放入一个索引数据，同时置当前为栈顶
     */
    fun push() {
        mapTokenBag = mutableMapOf()
        stkMapTokenBags.push(mapTokenBag)
    }

    /**
     * 弹出一个索引数据，同时置当前为栈顶
     */
    fun pop() {
        if (!stkMapTokenBags.isEmpty()) {
            stkMapTokenBags.pop()
            if (!stkMapTokenBags.isEmpty()) {
                mapTokenBag = stkMapTokenBags.peek()
            }
        }
    }

    /**
     * 设置索引数据
     *
     * @param index 索引位置
     * @param token 单词
     */
    operator fun set(index: Int, token: Token) {
        mapTokenBag[index] = TokenBag(token)
    }

    /**
     * 设置索引数据
     *
     * @param index 索引位置
     * @param obj   对象
     */
    operator fun set(index: Int, obj: Any) {
        mapTokenBag[index] = TokenBag(obj = obj)
    }

    override fun get(index: Int): TokenBag {
        return mapTokenBag[index]!!
    }

    override fun exists(index: Int): Boolean {
        return mapTokenBag.containsKey(index)
    }

    private fun printTokenBag(sb: StringBuilder, bags: Map<Int, TokenBag>?) {
        if (bags != null) {
            if (bags.isEmpty()) {
                sb.append("(empty)")
            } else {
                for ((key, value) in bags) {
                    sb.append("[").append(key).append(": ")
                    if (value.token != null) {
                        sb.append(value.token!!.toString())
                    } else {
                        sb.append(value.obj!!.toString())
                    }
                    sb.append("]")
                }
            }
        }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("-------- stack begin --------")
        sb.append(System.lineSeparator())
        sb.append("0: ")
        printTokenBag(sb, mapTokenBag)
        sb.append(System.lineSeparator())
        var i = 1
        stkMapTokenBags.forEach { hashMap ->
            sb.append(i).append(": ")
            printTokenBag(sb, hashMap)
            sb.append(System.lineSeparator())
            i++
        }
        sb.append("-------- stack end --------")
        return sb.toString()
    }
}
