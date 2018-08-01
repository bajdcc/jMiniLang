package com.bajdcc.LALR1.grammar.runtime

/**
 * 【运行时】用户态运行时状态查询
 *
 * @author bajdcc
 */
interface IRuntimeRing3 {

    /**
     * 是否是RING3程序
     * @return 是否是RING3程序
     */
    val isRing3: Boolean

    /**
     * 获取用户句柄列表
     * @return 用户句柄列表
     */
    val handles: Set<Int>

    /**
     * 获取等待的用户句柄
     * @return 等待的用户句柄
     */
    /**
     * 设置等待的用户句柄
     * @param id 用户句柄
     */
    var blockHandle: Int

    /**
     * 运行用户态代码
     * @param code 代码
     * @return PID
     * @throws Exception 系统异常
     */
    @Throws(Exception::class)
    fun exec(code: String): Int

    /**
     * 运行用户态代码（文件名）
     * @param filename 文件名
     * @param code 代码
     * @return PID
     * @throws Exception 系统异常
     */
    @Throws(Exception::class)
    fun execFile(filename: String, code: String): Int

    /**
     * 流输出
     * @param text 内容
     */
    fun put(text: String)

    /**
     * 设置布尔值
     * @param option 选项类型
     * @param flag 值
     */
    fun setOptionsBool(option: RuntimeMachine.Ring3Option, flag: Boolean)

    /**
     * 查看设置
     * @param option 选项类型
     * @return 返回设置的值
     */
    fun isOptionsBool(option: RuntimeMachine.Ring3Option): Boolean

    /**
     * 添加用户句柄
     * @param id 用户句柄
     */
    fun addHandle(id: Int)

    /**
     * 删除用户句柄
     * @param id 用户句柄
     */
    fun removeHandle(id: Int)

    /**
     * 进程分叉
     * @return 父进程返回子进程PID，子进程返回0
     */
    @Throws(Exception::class)
    fun fork(): Int
}
