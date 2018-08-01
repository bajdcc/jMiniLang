package com.bajdcc.OP.syntax.token

/**
 * 算符优先关系类型
 *
 * @author bajdcc
 */
enum class PrecedenceType(var desc: String) {
    LT("<"), GT(">"), EQ("="), NULL("-")
}