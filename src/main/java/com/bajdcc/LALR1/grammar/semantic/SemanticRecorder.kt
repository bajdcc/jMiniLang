package com.bajdcc.LALR1.grammar.semantic

import com.bajdcc.LALR1.grammar.error.SemanticException
import com.bajdcc.LALR1.grammar.error.SemanticException.SemanticError
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.token.Token

/**
 * 【语义分析】语义错误记录
 *
 * @author bajdcc
 */
class SemanticRecorder : ISemanticRecorder {

    private val errors = mutableListOf<SemanticException>()

    override val error: List<SemanticException>
        get() = errors

    override val correct: Boolean
        get() = errors.isEmpty()

    override fun add(error: SemanticError, token: Token) {
        if (errors.isEmpty())
            errors.add(SemanticException(error, token))
        val e = errors[errors.size - 1]
        if (e.errorCode != error || e.position.position.different(token.position))
            errors.add(SemanticException(error, token))
    }

    fun toString(iter: IRegexStringIterator): String {
        val sb = StringBuilder()
        sb.append("#### 语义错误列表 ####")
        sb.append(System.lineSeparator())
        for (error in errors) {
            sb.append(error.toString(iter))
            sb.append(System.lineSeparator())
        }
        return sb.toString()
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("#### 语义错误列表 ####")
        sb.append(System.lineSeparator())
        for (error in errors) {
            sb.append(error.toString())
            sb.append(System.lineSeparator())
        }
        return sb.toString()
    }
}
