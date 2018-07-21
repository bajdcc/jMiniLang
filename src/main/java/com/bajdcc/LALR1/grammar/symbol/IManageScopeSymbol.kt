package com.bajdcc.LALR1.grammar.symbol

import com.bajdcc.LALR1.grammar.tree.Function

/**
 * 命名空间管理接口
 *
 * @author bajdcc
 */
interface IManageScopeSymbol {

    /**
     * 创建并进入新的命名空间
     */
    fun enterScope()

    /**
     * 删除当前命名空间，返回上层
     */
    fun leaveScope()

    /**
     * 删除所有预期参数
     */
    fun clearFutureArgs()

    /**
     * 注册符号
     *
     * @param name 符号名
     */
    fun registerSymbol(name: String)

    /**
     * 注册过程
     *
     * @param func 过程
     */
    fun registerFunc(func: Function)

    /**
     * 注册函数过程
     *
     * @param func 过程
     */
    fun registerLambda(func: Function)

    /**
     * 注册下个块的参数表
     *
     * @param name 参数名
     * @return 无冲突则返回真
     */
    fun registerFutureSymbol(name: String): Boolean
}
