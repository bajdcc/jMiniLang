package com.bajdcc.LALR1.grammar.error

import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.token.Token

/**
 * 【语义分析】语义错误结构
 *
 * @author bajdcc
 */
class SemanticException(
        /**
         * 错误类型
         */
        /**
         * @return 错误类型
         */
        val errorCode: SemanticError,
        /**
         * 操作符
         */
        /**
         * @return 错误位置
         */
        val position: Token) : Exception(errorCode.message) {

    fun toString(iter: IRegexStringIterator): String {
        val snapshot = iter.ex().getErrorSnapshot(position.position)
        return if (snapshot.isEmpty()) {
            toString()
        } else message + ": " + position + System.lineSeparator() +
                snapshot + System.lineSeparator()
    }

    override fun toString(): String {
        return message + ": " + position.toString()
    }

    /**
     * 语义分析过程中的错误
     */
    enum class SemanticError(var message: String?) {
        UNKNOWN("未知"),
        INVALID_OPERATOR("操作非法"),
        MISSING_FUNCNAME("过程名不存在"),
        DUP_ENTRY("不能设置为入口函数名"),
        DUP_FUNCNAME("重复的函数名"),
        VARIABLE_NOT_DECLARAED("变量未定义"),
        VARIABLE_REDECLARAED("变量重复定义"),
        VAR_FUN_CONFLICT("变量名与函数名冲突"),
        MISMATCH_ARGS("参数个数不匹配"),
        DUP_PARAM("参数重复定义"),
        WRONG_EXTERN_SYMBOL("导出符号不存在"),
        WRONG_CYCLE("缺少循环体"),
        WRONG_YIELD("非法调用"),
        WRONG_ENUMERABLE("要求枚举对象"),
        TOO_MANY_ARGS("参数个数太多"),
        INVALID_ASSIGNMENT("非法的赋值语句"),
        DIFFERENT_FUNCNAME("过程名不匹配")
    }
}
