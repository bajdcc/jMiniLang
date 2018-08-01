package com.bajdcc.OP.syntax

/**
 * 文法部件接口（规则表达式基类）
 *
 * @author bajdcc
 */
interface ISyntaxComponent {
    /**
     * 设定遍历方式
     *
     * @param visitor 递归遍历算法
     */
    fun visit(visitor: ISyntaxComponentVisitor)

    /**
     * 设定遍历方式（逆序）
     *
     * @param visitor 递归遍历算法
     */
    fun visitReverse(visitor: ISyntaxComponentVisitor)
}
