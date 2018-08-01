package com.bajdcc.OP.grammar.handler

import com.bajdcc.util.lexer.token.Token

/**
 * 归约动作处理器
 *
 * @author bajdcc
 */
interface IPatternHandler {

    /**
     * 获取归约动作描述
     *
     * @return 动作描述
     */
    val patternName: String

    /**
     * 处理
     *
     * @param tokens  有序终结符（用于判定）
     * @param symbols 有序非终结符（用于存储）
     * @return 处理后的结果
     */
    fun handle(tokens: List<Token>, symbols: List<Any>): Any?
}
