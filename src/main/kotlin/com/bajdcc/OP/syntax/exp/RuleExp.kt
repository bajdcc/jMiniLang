package com.bajdcc.OP.syntax.exp

import com.bajdcc.OP.syntax.ISyntaxComponent
import com.bajdcc.OP.syntax.ISyntaxComponentVisitor
import com.bajdcc.OP.syntax.rule.Rule
import com.bajdcc.util.VisitBag

/**
 * 文法规则（非终结符）
 *
 * @author bajdcc
 */
class RuleExp(var id: Int,
              var name: String) : ISyntaxComponent {

    /**
     * 规则
     */
    var rule = Rule(this)

    override fun visit(visitor: ISyntaxComponentVisitor) {
        val bag = VisitBag()
        visitor.visitBegin(this, bag)
        if (bag.visitEnd) {
            visitor.visitEnd(this)
        }
    }

    override fun visitReverse(visitor: ISyntaxComponentVisitor) {
        val bag = VisitBag()
        visitor.visitBegin(this, bag)
        if (bag.visitEnd) {
            visitor.visitEnd(this)
        }
    }

    override fun toString(): String {
        return "$id： $name"
    }
}
