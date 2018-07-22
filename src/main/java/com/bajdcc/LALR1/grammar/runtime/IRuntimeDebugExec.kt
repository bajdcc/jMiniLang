package com.bajdcc.LALR1.grammar.runtime

/**
 * 【扩展】外部化过程接口
 *
 * @author bajdcc
 */
interface IRuntimeDebugExec {

    /**
     * 返回参数类型，用于运行时类型检查
     *
     * @return 若无参数则返回空
     */
    val argsType: Array<RuntimeObjectType>?

    /**
     * 返回过程文档
     *
     * @return 文档
     */
    val doc: String

    /**
     * 调用外部化过程
     *
     * @param args   参数表
     * @param status 状态接口
     * @return 过程返回值，若没有则返回空
     * @throws Exception 异常
     */
    @Throws(Exception::class)
    fun ExternalProcCall(args: List<RuntimeObject>, status: IRuntimeStatus): RuntimeObject?
}
