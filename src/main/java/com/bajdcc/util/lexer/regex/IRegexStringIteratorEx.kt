package com.bajdcc.util.lexer.regex

import com.bajdcc.util.Position
import com.bajdcc.util.lexer.token.Token

/**
 * 字符串迭代器附加接口
 *
 * @author bajdcc
 */
interface IRegexStringIteratorEx {

    /**
     * 是否到末尾
     *
     * @return 是否到末尾
     * @see com.bajdcc.util.lexer.token.TokenType
     */
    val isEOF: Boolean

    /**
     * 保存单词
     */
    fun saveToken()

    /**
     * 返回之前的位置
     *
     * @return 上一个位置
     */
    fun lastPosition(): Position

    /**
     * 获取当前单词
     *
     * @return 当前单词
     */
    fun token(): Token

    /**
     * 获取所有单词
     *
     * @return 所有单词
     */
    fun tokenList(): List<Token>

    /**
     * 获取错误现场
     *
     * @param position 位置
     * @return 错误现场描述
     */
    fun getErrorSnapshot(position: Position): String
}