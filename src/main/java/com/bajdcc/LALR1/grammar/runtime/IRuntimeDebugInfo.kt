package com.bajdcc.LALR1.grammar.runtime

import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray

/**
 * 【扩展】调试、本地化开发接口
 *
 * @author bajdcc
 */
interface IRuntimeDebugInfo {

    /**
     * 返回数据存储
     *
     * @return 数据存储
     */
    val dataMap: MutableMap<String, Any>

    /**
     * 得到导出的函数列表
     * @return 导出函数
     */
    val externFuncList: List<RuntimeArray>

    /**
     * 根据当前指令页地址找到函数名
     *
     * @param addr 地址
     * @return 函数名
     */
    fun getFuncNameByAddress(addr: Int): String

    /**
     * 根据导出的函数名找到地址
     *
     * @param name 函数名
     * @return 地址
     */
    fun getAddressOfExportFunc(name: String): Int

    /**
     * 根据自定义参数名称找到本地过程导出接口
     *
     * @param name 自定义参数名称
     * @return 本地过程导出接口
     */
    fun getExecCallByName(name: String): IRuntimeDebugExec?

    /**
     * 根据自定义参数名称找到本地值导出接口
     *
     * @param name 自定义参数名称
     * @return 本地值导出接口
     */
    fun getValueCallByName(name: String): IRuntimeDebugValue?

    /**
     * 添加外部变量
     *
     * @param name 变量名
     * @param func 调用过程
     * @return 是否冲突
     */
    fun addExternalValue(name: String, func: IRuntimeDebugValue): Boolean

    /**
     * 添加外部过程
     *
     * @param name 过程名
     * @param func 调用过程
     * @return 是否冲突
     */
    fun addExternalFunc(name: String, func: IRuntimeDebugExec): Boolean
}
