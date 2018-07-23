package com.bajdcc.LALR1.syntax.handler

import com.bajdcc.util.Position

/**
 * 文法生成过程中的异常
 *
 * @author bajdcc
 */
class SyntaxException(val errorCode: SyntaxError, pos: Position, obj: Any?) : Exception(errorCode.message) {

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
     * 代码页
     */
    /**
     * @return 代码页
     */
    /**
     * 设置代码页
     *
     * @param pageName 代码页
     */
    var pageName = ""
    /**
     * 文件名
     */
    /**
     * @return 文件名
     */
    /**
     * 设置文件名
     *
     * @param fileName 文件名
     */
    var fileName = ""

    /**
     * 文法推导式解析过程中的错误
     */
    enum class SyntaxError constructor(var message: String) {
        NULL("推导式为空"),
        UNDECLARED("无法识别的符号"),
        SYNTAX("语法错误"),
        INCOMPLETE("推导式不完整"),
        EPSILON("可能产生空串"),
        INDIRECT_RECURSION("存在间接左递归"),
        FAILED("不能产生字符串"),
        MISS_NODEPENDENCY_RULE("找不到无最左依赖的规则"),
        REDECLARATION("重复定义"),
        COMPILE_ERROR("编译错误")
    }

    init {
        position = pos
        if (obj != null) {
            info = obj.toString()
        }
    }
}
