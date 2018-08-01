package com.bajdcc.LALR1.grammar.runtime

import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeFuncObject
import com.bajdcc.LALR1.grammar.runtime.service.IRuntimeService

/**
 * 【运行时】运行时状态查询
 *
 * @author bajdcc
 */
interface IRuntimeStatus {

    /**
     * 得到进程ID
     *
     * @return PID
     */
    val pid: Int

    /**
     * 得到父进程ID
     *
     * @return PID
     */
    val parentPid: Int

    /**
     * 得到进程优先级
     *
     * @return 优先级
     */
    val priority: Int

    /**
     * 得到服务接口
     *
     * @return 运行时服务接口
     */
    val service: IRuntimeService

    /**
     * 获取用户态进程列表
     *
     * @return 进程ID列表
     */
    val usrProcs: List<Int>

    /**
     * 获取内核态进程列表
     *
     * @return 进程ID列表
     */
    val sysProcs: List<Int>

    /**
     * 获取进程列表
     *
     * @return 进程ID列表
     */
    val allProcs: List<Int>

    /**
     * 获取当前进程信息
     *
     * @return 进程信息
     */
    val procInfo: Array<Any>

    /**
     * 得到当前进程页面
     * @return 进程页面
     */
    val page: String

    /**
     * 获取函数参数数量
     * @return 参数数量
     */
    val funcArgsCount: Int

    /**
     * 获取RING3接口
     * @return RING3接口
     */
    val ring3: IRuntimeRing3

    /**
     * 获取所有文档
     * @return 文档数组
     */
    val allDocs: RuntimeArray

    /**
     * 得到过程的文档
     *
     * @param name 过程名
     * @return 文档
     */
    fun getHelpString(name: String): String

    /**
     * 得到过程的地址
     *
     * @param name 过程名
     * @return 地址
     * @throws RuntimeException 运行时错误
     */
    @Throws(RuntimeException::class)
    fun getFuncAddr(name: String): Int

    /**
     * 载入代码并运行
     *
     * @param name 文件名
     * @throws RuntimeException 运行时错误
     */
    @Throws(Exception::class)
    fun runPage(name: String)

    /**
     * 载入代码并运行于新进程
     *
     * @param name 文件名
     * @return 进程ID
     * @throws RuntimeException 运行时错误
     */
    @Throws(Exception::class)
    fun runProcess(name: String): Int

    /**
     * 载入代码并运行于新进程
     *
     * @param name 页名
     * @return 进程ID
     * @throws RuntimeException 运行时错误
     */
    @Throws(Exception::class)
    fun runProcessX(name: String): Int

    /**
     * 载入代码并运行于新用户态进程
     *
     * @param name 文件名
     * @return 进程ID
     * @throws RuntimeException 运行时错误
     */
    @Throws(Exception::class)
    fun runUsrProcess(name: String): Int

    /**
     * 载入代码并运行于新用户态进程
     *
     * @param name 页名
     * @return 进程ID
     * @throws RuntimeException 运行时错误
     */
    @Throws(Exception::class)
    fun runUsrProcessX(name: String): Int

    /**
     * 运行时错误
     *
     * @param type 错误类型
     * @throws RuntimeException 运行时异常
     */
    @Throws(RuntimeException::class)
    fun err(type: RuntimeException.RuntimeError)

    /**
     * 运行时错误
     *
     * @param type    错误类型
     * @param message 补充信息
     * @throws RuntimeException 运行时异常
     */
    @Throws(RuntimeException::class)
    fun err(type: RuntimeException.RuntimeError, message: String)

    /**
     * 运行时警告
     *
     * @param type    错误类型
     * @param message 补充信息
     */
    fun warn(type: RuntimeException.RuntimeError, message: String)

    /**
     * 创建进程
     *
     * @param func 函数
     * @return 进程ID
     * @throws Exception 运行时异常
     */
    @Throws(Exception::class)
    fun createProcess(func: RuntimeFuncObject): Int

    /**
     * 创建进程
     *
     * @param func 函数
     * @param obj  参数
     * @return 进程ID
     * @throws Exception 运行时异常
     */
    @Throws(Exception::class)
    fun createProcess(func: RuntimeFuncObject, obj: RuntimeObject): Int

    /**
     * 创建用户态进程
     *
     * @param func 函数
     * @return 进程ID
     * @throws Exception 运行时异常
     */
    @Throws(Exception::class)
    fun createUsrProcess(func: RuntimeFuncObject): Int

    /**
     * 创建用户态进程
     *
     * @param func 函数
     * @param obj  参数
     * @return 进程ID
     * @throws Exception 运行时异常
     */
    @Throws(Exception::class)
    fun createUsrProcess(func: RuntimeFuncObject, obj: RuntimeObject): Int

    /**
     * 获取页引用
     *
     * @param page 页名
     */
    fun getPageRefers(page: String): List<RuntimeCodePage>?

    /**
     * 设置进程优先级
     *
     * @param priority 优先级
     * @return 是否设置成功
     */
    fun setPriority(priority: Int): Boolean

    /**
     * 进程休眠
     *
     * @return 进程ID
     */
    fun sleep(): Int

    /**
     * 获取进程信息
     *
     * @param id 进程ID
     * @return 进程信息
     */
    fun getProcInfoById(id: Int): Array<Any>

    /**
     * 设置进程说明
     *
     * @param desc 说明信息
     */
    fun setProcDesc(desc: String)

    /**
     * 获取函数参数
     * @param index 第几个参数
     * @return 参数
     */
    fun getFuncArgs(index: Int): RuntimeObject?

    /**
     * 获取RING3接口（指定PID）
     * @param pid PID
     * @return RING3接口，不是则返回空
     */
    fun getRing3(pid: Int): IRuntimeRing3?
}
