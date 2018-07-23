package com.bajdcc.LALR1.syntax.exp

import com.bajdcc.LALR1.syntax.ISyntaxComponent
import com.bajdcc.LALR1.syntax.ISyntaxComponentVisitor
import com.bajdcc.util.VisitBag

/**
 * 文法规则（可选）
 * [expression] 子表达式
 * @author bajdcc
 */
class OptionExp(var expression: ISyntaxComponent) : ISyntaxComponent {

    override fun visit(visitor: ISyntaxComponentVisitor) {
        val bag = VisitBag()
        visitor.visitBegin(this, bag)
        if (bag.visitChildren) {
            expression.visit(visitor)
        }
        if (bag.visitEnd) {
            visitor.visitEnd(this)
        }
    }
}
