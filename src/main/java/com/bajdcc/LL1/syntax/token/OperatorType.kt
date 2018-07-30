package com.bajdcc.LL1.syntax.token

/**
 * 操作符
 *
 * @author bajdcc
 */
enum class OperatorType(var desc: String) {
    INFER("->"), ALTERNATIVE("|"), LT("<"), GT(">")
}
