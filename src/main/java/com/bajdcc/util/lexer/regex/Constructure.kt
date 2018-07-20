package com.bajdcc.util.lexer.regex

/**
 * 顺序结构或分支结构
 * [branch] 若是则为分支，否则为顺序
 * @author bajdcc
 */
class Constructure(var branch: Boolean) : IRegexComponent {

    /**
     * 孩子结点
     */
    var arrComponents = mutableListOf<IRegexComponent>()

    override fun visit(visitor: IRegexComponentVisitor) {
        visitor.visitBegin(this)
        arrComponents.forEach { it.visit(visitor) }
        visitor.visitEnd(this)
    }
}
