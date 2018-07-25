package com.bajdcc.LALR1.semantic.token

import com.bajdcc.util.lexer.token.Token

/**
 * 【语义分析】随机访问单词流
 *
 * @author bajdcc
 */
interface IRandomAccessOfTokens {

    /**
     * 获取相对位置的单词
     *
     * @param index 索引
     * @return 单词
     */
    fun relativeGet(index: Int): Token

    /**
     * 获取绝对位置的单词
     *
     * @param index 索引
     * @return 单词
     */
    fun positiveGet(index: Int): Token
}
