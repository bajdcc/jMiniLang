package com.bajdcc.OP.grammar.error

import com.bajdcc.util.Position

/**
 * 文法生成过程中的异常
 *
 * @author bajdcc
 */
class GrammarException(
        /**
         * 错误类型
         */
        /**
         * @return 错误类型
         */
        val errorCode: GrammarError, pos: Position, obj: Any?) : Exception(errorCode.message) {

    /**
     * 位置
     */
    /**
     * @return 错误位置
     */
    var position = Position()

    /**
     * 错误信息
     */
    /**
     * @return 错误信息
     */
    var info = ""
        private set

    /**
     * 文法推导式解析过程中的错误
     */
    enum class GrammarError(var message: String?) {
        NULL("输入为空串"), UNDECLARED("无法识别的符号"), SYNTAX("语法错误"), MISS_PRECEDENCE(
                "操作符缺少优先级"),
        MISS_HANDLER("缺少处理模式")
    }

    init {
        position = pos
        if (obj != null) {
            info = obj.toString()
        }
    }
}
