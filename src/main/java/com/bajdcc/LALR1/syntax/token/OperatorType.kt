package com.bajdcc.LALR1.syntax.token

/**
 * 操作符
 *
 * @author bajdcc
 */
enum class OperatorType constructor(var desc: String) {
    INFER("->"),
    ALTERNATIVE("|"),
    LPARAN("("),
    RPARAN(")"),
    LBRACE("{"),
    RBRACE("}"),
    LSQUARE("["),
    RSQUARE("]"),
    LT("<"),
    GT(">")
}
