package com.bajdcc.OP.syntax.exp

import com.bajdcc.OP.syntax.ISyntaxComponent
import com.bajdcc.OP.syntax.ISyntaxComponentVisitor
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
            for (exp in arrExpressions) {
                exp.visit(visitor)
            }
        }
        if (bag.visitEnd) {
            visitor.visitEnd(this)
        }
    }

    override fun visitReverse(visitor: ISyntaxComponentVisitor) {
        val bag = VisitBag()
        visitor.visitBegin(this, bag)
        if (bag.visitChildren) {
            for (exp in arrExpressions) {
                exp.visitReverse(visitor)
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
