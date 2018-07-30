package com.bajdcc.OP.syntax.exp

import com.bajdcc.OP.syntax.ISyntaxComponent

/**
 * 可以添加孩子结点的表达式接口
 *
 * @author bajdcc
 */
interface IExpCollction {

    /**
     * 集合是否为空
     *
     * @return 集合是否为空
     */
    val isEmpty: Boolean

    /**
     * 添加孩子结点
     *
     * @param exp 子表达式
     */
    fun add(exp: ISyntaxComponent)
}
