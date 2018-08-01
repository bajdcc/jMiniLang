package com.bajdcc.OP.syntax.handler

import com.bajdcc.util.Position

/**
 * 文法生成过程中的异常
 *
 * @author bajdcc
 */
class SyntaxException(val errorCode: SyntaxError,
                      val position: Position,
                      obj: Any?) : Exception(errorCode.message) {

    val info = obj?.toString() ?: ""

    /**
     * 文法推导式解析过程中的错误
     */
    enum class SyntaxError(var message: String?) {
        NULL("推导式为空"), UNDECLARED("无法识别的符号"), SYNTAX("语法错误"), INCOMPLETE(
                "推导式不完整"),
        FAILED("不能产生字符串"), CONSEQUENT_NONTERMINAL(
                "存在连续的非终结符"),
        REDECLARATION("重复定义")
    }
}
