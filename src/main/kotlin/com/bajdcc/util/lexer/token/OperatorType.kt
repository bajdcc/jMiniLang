package com.bajdcc.util.lexer.token

/**
 * 操作符
 *
 * @author bajdcc
 */
enum class OperatorType constructor(var desc: String) {
    ASSIGN("="),
    PLUS("+"),
    MINUS("-"),
    TIMES("*"),
    DIVIDE("/"),
    ESCAPE("\\"),
    QUERY("?"),
    MOD("%"),
    BIT_AND("&"),
    BIT_OR("|"),
    BIT_NOT("~"),
    BIT_XOR("^"),
    LOGICAL_NOT("!"),
    LESS_THAN("<"),
    GREATER_THAN(">"),

    LPARAN("("),
    RPARAN(")"),
    LBRACE("{"),
    RBRACE("}"),
    LSQUARE("["),
    RSQUARE("]"),
    COMMA(","),
    DOT("."),
    SEMI(";"),
    COLON(":"),
    ELLIPSIS("..."),

    EQUAL("=="),
    NOT_EQUAL("!="),
    PLUS_PLUS("++"),
    MINUS_MINUS("--"),
    PLUS_ASSIGN("+="),
    MINUS_ASSIGN("-="),
    TIMES_ASSIGN("*="),
    DIV_ASSIGN("/="),
    AND_ASSIGN("&="),
    OR_ASSIGN("|="),
    XOR_ASSIGN("^="),
    MOD_ASSIGN("%="),
    LESS_THAN_OR_EQUAL("<="),
    GREATER_THAN_OR_EQUAL(">="),
    LOGICAL_AND("&&"),
    LOGICAL_OR("||"),
    POINTER("->"),
    LEFT_SHIFT("<<"),
    RIGHT_SHIFT(">>"),
    LEFT_SHIFT_ASSIGN("<<="),
    RIGHT_SHIFT_ASSIGN(">>="),
    PROPERTY("::"),
    EQ_ASSIGN(":=")
}
