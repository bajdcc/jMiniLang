package com.bajdcc.LALR1.grammar.runtime.service

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray

/**
 * 【运行时】运行时共享服务接口
 *
 * @author bajdcc
 */
interface IRuntimeShareService {

    /**
     * 创建共享
     *
     * @param name 共享名称
     * @param obj  变量
     * @param page 创建时的页名
     * @return 操作状态
     */
    fun startSharing(name: String, obj: RuntimeObject, page: String): Int

    /**
     * 创建共享（可覆盖）
     *
     * @param name 共享名称
     * @param obj  变量
     * @param page 创建时的页名
     * @return 操作状态
     */
    fun createSharing(name: String, obj: RuntimeObject, page: String): Int

    /**
     * 查询共享
     *
     * @param name      变量名称
     * @param reference 是否引用
     * @return 共享变量
     */
    fun getSharing(name: String, reference: Boolean): RuntimeObject

    /**
     * 停止共享
     *
     * @param name 变量名称
     * @return 操作状态
     */
    fun stopSharing(name: String): Int

    /**
     * 是否锁定
     *
     * @param name 变量名称
     * @return 是否锁定
     */
    fun isLocked(name: String): Boolean

    /**
     * 设置锁定
     *
     * @param name 变量名称
     * @param lock 是否锁定
     */
    fun setLocked(name: String, lock: Boolean)

    /**
     * 获取共享数量
     *
     * @return 共享数量
     */
    fun size(): Long

    /**
     * 获取列表
     *
     * @param api 是否API调用
     * @return 列表
     */
    fun stat(api: Boolean): RuntimeArray
}
