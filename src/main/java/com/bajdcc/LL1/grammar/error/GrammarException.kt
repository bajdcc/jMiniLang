package com.bajdcc.LL1.grammar.error

import com.bajdcc.util.Position

/**
 * 文法生成过程中的异常
 *
 * @author bajdcc
 */
class GrammarException(errorCode: GrammarError,
                       val position: Position,
                       val obj: Any?) : Exception(errorCode.message) {

    val info = obj?.toString() ?: ""

    /**
     * 文法推导式解析过程中的错误
     */
    enum class GrammarError(var message: String) {
        UNDECLARED("无法识别的符号"), SYNTAX("语法错误")
    }
}
