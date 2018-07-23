package com.bajdcc.LALR1.syntax.automata.nga

/**
 * 非确定性文法自动机边类型
 *
 * @author bajdcc
 */
enum class NGAEdgeType constructor(var desc: String?) {
    EPSILON("Epsilon边"), TOKEN("终结符"), RULE("非终结符")
}
