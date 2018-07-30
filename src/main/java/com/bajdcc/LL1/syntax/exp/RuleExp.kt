package com.bajdcc.LL1.syntax.exp

import com.bajdcc.LL1.syntax.ISyntaxComponent
import com.bajdcc.LL1.syntax.ISyntaxComponentVisitor
import com.bajdcc.LL1.syntax.rule.Rule
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

    override fun toString(): String {
        return id.toString() + "： " + name
    }
}
