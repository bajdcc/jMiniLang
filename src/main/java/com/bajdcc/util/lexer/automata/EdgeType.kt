package com.bajdcc.util.lexer.automata

/**
 * 状态自动机边类型
 *
 * @author bajdcc
 */
enum class EdgeType constructor(var desc: String) {
    EPSILON("Epsilon边"), CHARSET("字符区间")
}
