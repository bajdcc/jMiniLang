package com.bajdcc.LALR1.grammar.runtime

/**
 * 【虚拟机】运行时异常
 *
 * @author bajdcc
 */
class RuntimeException(var error: RuntimeError?, var position: Int, var info: String?) : Exception() {

    enum class RuntimeError(var message: String?) {
        EXIT("程序退出"),
        NULL("未知"),
        WRONG_CODEPAGE("代码页错误"),
        WRONG_INST("非法指令"),
        WRONG_COROUTINE("协程错误"),
        NULL_STACK("堆栈为空"),
        NULL_QUEUE("队列为空"),
        WRONG_OPERATOR("非法操作"),
        DUP_PAGENAME("代码页名称冲突"),
        WRONG_IMPORT("导入模块名错误"),
        WRONG_EXPORT("导出模块名错误"),
        WRONG_LOAD_EXTERN("导入外部符号错误"),
        WRONG_ARGCOUNT("参数个数不一致"),
        WRONG_ARGINVALID("参数错误"),
        WRONG_ARGTYPE("参数类型不一致"),
        NULL_OPERATOR("空值运算"),
        WRONG_FUNCNAME("过程不存在"),
        STACK_OVERFLOW("堆栈溢出"),
        ARG_OVERFLOW("参数过多"),
        UNDEFINED_CONVERT("未定义的强制转换"),
        INVALID_INDEX("索引无效"),
        INVALID_VARIABLE("变量查询失败"),
        PROCESS_OVERFLOW("进程数量已达最大值"),
        EMPTY_CALLSTACK("调用栈为空"),
        MAX_HANDLE("句柄不足"),
        DUP_SHARE_NAME("共享名重复"),
        INVALID_SHARE_NAME("共享名不存在"),
        INVALID_REFERENCE("引用计数异常"),
        ACCESS_FORBIDDEN("越权访问"),
        THROWS_EXCEPTION("发生异常"),
        DUP_EXCEPTION("重复发生异常")
    }
}
