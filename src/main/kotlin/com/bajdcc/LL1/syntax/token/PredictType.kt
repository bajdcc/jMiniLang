package com.bajdcc.LL1.syntax.token

/**
 * 预测分析指令类型
 *
 * @author bajdcc
 */
enum class PredictType(var desc: String) {
    TERMINAL("终结符"), NONTERMINAL("非终结符"), EPSILON("结束符")
}