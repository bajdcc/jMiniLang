package com.bajdcc.LL1.syntax.handler

import com.bajdcc.util.Position

/**
 * 文法生成过程中的异常
 *
 * @author bajdcc
 */
class SyntaxException(errorCode: SyntaxError,
                      val position: Position,
                      obj: Any?) : Exception(errorCode.message) {

    val info = obj?.toString() ?: ""

    /**
     * 文法推导式解析过程中的错误
     */
    enum class SyntaxError(var message: String) {
        NULL("推导式为空"),
        UNDECLARED("无法识别的符号"),
        SYNTAX("语法错误"),
        INCOMPLETE("推导式不完整"),
        DIRECT_RECURSION("存在直接左递归"),
        INDIRECT_RECURSION("存在间接左递归"),
        FAILED("不能产生字符串"),
        MISS_NODEPENDENCY_RULE("找不到无最左依赖的规则"),
        REDECLARATION("重复定义")
    }
}
