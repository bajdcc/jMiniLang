package com.bajdcc.LALR1.syntax.exp

import com.bajdcc.LALR1.syntax.ISyntaxComponent
import com.bajdcc.LALR1.syntax.ISyntaxComponentVisitor
import com.bajdcc.LALR1.syntax.rule.Rule
import com.bajdcc.util.VisitBag

/**
 * 文法规则（非终结符）
 * [id] 非终结符ID
 * [name] 非终结符名称
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
