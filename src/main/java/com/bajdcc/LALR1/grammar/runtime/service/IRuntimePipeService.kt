package com.bajdcc.LALR1.grammar.runtime.service

import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray

/**
 * 【运行时】运行时管道服务接口
 *
 * @author bajdcc
 */
interface IRuntimePipeService {

    /**
     * 创建管道
     *
     * @param name 管道名称
     * @param page 创建时的页面
     * @return 管道句柄
     */
    fun create(name: String, page: String): Int

    /**
     * 销毁管道
     *
     * @param handle 管道句柄
     * @return 是否成功
     */
    fun destroy(handle: Int): Boolean

    /**
     * 销毁管道
     *
     * @param pid  进程ID
     * @param name 管道名称
     * @return 是否成功
     */
    fun destroyByName(pid: Int, name: String): Boolean

    /**
     * 管道读
     *
     * @param pid    进程ID
     * @param handle 管道句柄
     * @return 读取的字符
     */
    fun read(pid: Int, handle: Int): Char

    /**
     * 管道读
     *
     * @param pid    进程ID
     * @param handle 管道句柄
     * @return 读取的字符
     */
    fun readNoBlock(pid: Int, handle: Int): Char

    /**
     * 管道写
     *
     * @param handle 管道句柄
     * @param ch     字符
     * @return 是否成功
     */
    fun write(handle: Int, ch: Char): Boolean

    /**
     * 管道是否为空
     *
     * @param handle 管道句柄
     * @return 是否为空
     */
    fun hasData(handle: Int): Boolean

    /**
     * 查询管道是否存在
     *
     * @param name 管理名
     * @return 是否存在
     */
    fun query(name: String): Boolean

    /**
     * 获取管道数量
     *
     * @return 管道数量
     */
    fun size(): Long

    /**
     * 获取列表
     *
     * @param api 是否API调用
     * @return 列表
     */
    fun stat(api: Boolean): RuntimeArray

    /**
     * 写入数据
     *
     * @param name 管道名
     * @param data 数据
     * @return 管道是否存在
     */
    fun writeString(name: String, data: String): Boolean

    /**
     * 写入数据（不存在时创建）
     *
     * @param name 管道名
     * @param data 数据
     */
    fun writeStringNew(name: String, data: String)

    /**
     * 一次性读取完管道所有内容并销毁
     * @param name 管道名
     * @return 内容，如管道不存在则返回空
     */
    fun readAndDestroy(name: String): String?

    /**
     * 一次性读取完管道所有内容
     * @param name 管道名
     * @return 内容，如管道不存在则返回空
     */
    fun readAll(name: String): String?
}
