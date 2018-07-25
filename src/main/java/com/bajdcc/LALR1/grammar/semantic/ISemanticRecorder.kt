package com.bajdcc.LALR1.grammar.semantic

import com.bajdcc.LALR1.grammar.error.SemanticException
import com.bajdcc.LALR1.grammar.error.SemanticException.SemanticError
import com.bajdcc.util.lexer.token.Token

/**
 * 【语义分析】语义错误记录接口
 *
 * @author bajdcc
 */
interface ISemanticRecorder {

    /**
     * 获取错误列表
     *
     * @return 错误列表
     */
    val error: List<SemanticException>

    /**
     * 是否没有任何错误
     *
     * @return 没有错误则为真
     */
    val correct: Boolean

    /**
     * 记录一个错误
     *
     * @param error 语义分析错误类型
     * @param token 单词
     */
    fun add(error: SemanticError, token: Token)
}
