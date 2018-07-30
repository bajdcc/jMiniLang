package com.bajdcc.util.lexer.error

import com.bajdcc.util.Position

/**
 * 正则表达式生成过程中的异常
 *
 * @author bajdcc
 */
class RegexException(val errorCode: RegexError, val position: Position) : Exception(errorCode.message) {

    /**
     * 正则表达式解析过程中的错误
     */
    enum class RegexError(var message: String?) {
        NULL("正则表达式为空"),
        CTYPE("非法字符"),
        ESCAPE("非法的转义字符"),
        BRACK("中括号不匹配"),
        PAREN("小括号不匹配"),
        BRACE("大括号不匹配"),
        RANGE("范围不正确"),
        SYNTAX("语法错误"),
        INCOMPLETE("正则表达式不完整")
    }
}
