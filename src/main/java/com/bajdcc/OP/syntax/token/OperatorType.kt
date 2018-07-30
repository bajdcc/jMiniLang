package com.bajdcc.OP.syntax.token

/**
 * 操作符
 *
 * @author bajdcc
 */
enum class OperatorType(var desc: String) {
    INFER("->"), ALTERNATIVE("|"), LT("<"), GT(">")
}
