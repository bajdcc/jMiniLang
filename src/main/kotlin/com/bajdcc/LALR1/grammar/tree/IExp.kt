package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder

/**
 * 【语义类型】基本表达式接口
 *
 * @author bajdcc
 */
interface IExp : ICommon {

    /**
     * 是否是左值（即不可修改的常量）
     *
     * @return 是否为常量
     */
    fun isConstant(): Boolean

    /**
     * 是否可枚举
     *
     * @return 是否可枚举
     */
    fun isEnumerable(): Boolean

    /**
     * 表达式化简
     *
     * @param recorder 错误记录
     * @return 简化后的表达式
     */
    fun simplify(recorder: ISemanticRecorder): IExp

    /**
     * 设置Yield
     */
    fun setYield()
}
