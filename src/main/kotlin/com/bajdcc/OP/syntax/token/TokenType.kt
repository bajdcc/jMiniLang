package com.bajdcc.OP.syntax.token

/**
 * 单词类型
 *
 * @author bajdcc
 */
enum class TokenType(var desc: String) {
    TERMINAL("终结符"),
    NONTERMINAL("非终结符"),
    EOF("全文末尾"),
    COMMENT("注释"),
    OPERATOR("操作符"),
    WHITSPACE("空白字符"),
    ERROR("错误")
}
