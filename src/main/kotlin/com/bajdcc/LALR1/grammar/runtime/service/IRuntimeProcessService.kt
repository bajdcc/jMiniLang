package com.bajdcc.LALR1.grammar.runtime.service

import com.bajdcc.LALR1.grammar.runtime.IRuntimeRing3

/**
 * 【运行时】运行时进程服务接口
 *
 * @author bajdcc
 */
interface IRuntimeProcessService {

    /**
     * 获取PID
     * @return PID
     */
    val pid: Int

    /**
     * 虚拟机运行频率
     *
     * @return 运行频率
     */
    val speed: Double

    /**
     * 获取当前进程信息（缓存）
     *
     * @return 进程信息
     */
    val procInfoCache: List<Array<Any>>

    /**
     * 得到RING3接口
     * @return RING3接口
     */
    val ring3: IRuntimeRing3

    /**
     * 进程阻塞
     *
     * @param pid 进程ID
     * @return 成功与否
     */
    fun block(pid: Int): Boolean

    /**
     * 进程唤醒
     *
     * @param pid 进程ID
     * @return 成功与否
     */
    fun wakeup(pid: Int): Boolean

    /**
     * 进程休眠（异步唤醒）
     *
     * @param pid  进程ID
     * @param ms   毫秒
     * @return 总休眠毫秒数
     */
    fun blocks(pid: Int, ms: Int): Long

    /**
     * 进程休眠
     *
     * @param pid  进程ID
     * @param turn 休眠趟数
     * @return 总休眠趟数
     */
    fun sleep(pid: Int, turn: Int): Int

    /**
     * 进程等待
     *
     * @param joined 等待的进程ID
     * @param pid    当前进程ID
     * @return 是则继续等待
     */
    fun join(joined: Int, pid: Int): Boolean

    /**
     * 进行存活
     *
     * @param pid 进程标识
     * @return 是否存活
     */
    fun live(pid: Int): Boolean

    /**
     * 是否还可以创建进程
     *
     * @return 是否可以创建
     */
    fun available(): Boolean

    /**
     * 添加代码页
     *
     * @param name 页名
     * @param code 代码
     * @return 是否成功
     */
    fun addCodePage(name: String, code: String): Boolean

    /**
     * 让解释器等待一段时间，让UI刷新
     */
    fun waitForUI()

    /**
     * 设置是否输出调试信息（堆栈）
     *
     * @param pid   当前进程PID
     * @param debug 是否调试
     */
    fun setDebug(pid: Int, debug: Boolean)

    /**
     * 设置高速运行模式
     * @param mode 是否不休眠
     */
    fun setHighSpeed(mode: Boolean)

    /**
     * 强制结束用户态进程
     * @param pid PID
     * @param error 错误原因
     * @return 状态码
     */
    fun ring3Kill(pid: Int, error: String): Int

    /**
     * 得到RING3接口
     * @param pid PID
     * @return RING3接口
     */
    fun getRing3(pid: Int): IRuntimeRing3?
}
