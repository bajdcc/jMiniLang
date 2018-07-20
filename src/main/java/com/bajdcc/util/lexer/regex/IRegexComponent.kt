package com.bajdcc.util.lexer.regex

/**
 * 正则表达式部件接口（父类）
 *
 * @author bajdcc
 */
interface IRegexComponent {
    /**
     * 设定扩展自身结点的方式
     *
     * @param visitor 递归遍历算法
     */
    fun visit(visitor: IRegexComponentVisitor)
}
