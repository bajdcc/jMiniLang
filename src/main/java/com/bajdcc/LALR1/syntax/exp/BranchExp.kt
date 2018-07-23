package com.bajdcc.LALR1.syntax.exp

import com.bajdcc.LALR1.syntax.ISyntaxComponent
import com.bajdcc.LALR1.syntax.ISyntaxComponentVisitor
import com.bajdcc.util.VisitBag

/**
 * 文法规则（分支）
 *
 * @author bajdcc
 */
class BranchExp : ISyntaxComponent, IExpCollction {

    /**
     * 子表达式表
     */
    var arrExpressions = mutableListOf<ISyntaxComponent>()

    override val isEmpty: Boolean
        get() = arrExpressions.isEmpty()

    override fun visit(visitor: ISyntaxComponentVisitor) {
        val bag = VisitBag()
        visitor.visitBegin(this, bag)
        if (bag.visitChildren) {
            arrExpressions.forEach { exp ->
                exp.visit(visitor)
            }
        }
        if (bag.visitEnd) {
            visitor.visitEnd(this)
        }
    }

    override fun add(exp: ISyntaxComponent) {
        arrExpressions.add(exp)
    }
}
