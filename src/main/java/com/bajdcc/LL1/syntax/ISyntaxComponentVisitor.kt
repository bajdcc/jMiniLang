package com.bajdcc.LL1.syntax

import com.bajdcc.LL1.syntax.exp.BranchExp
import com.bajdcc.LL1.syntax.exp.RuleExp
import com.bajdcc.LL1.syntax.exp.SequenceExp
import com.bajdcc.LL1.syntax.exp.TokenExp
import com.bajdcc.util.VisitBag

/**
 * 基于文法组件的访问接口（Visitor模式）
 *
 * @author bajdcc
 */
interface ISyntaxComponentVisitor {

    fun visitBegin(node: TokenExp, bag: VisitBag)

    fun visitBegin(node: RuleExp, bag: VisitBag)

    fun visitBegin(node: SequenceExp, bag: VisitBag)

    fun visitBegin(node: BranchExp, bag: VisitBag)

    fun visitEnd(node: TokenExp)

    fun visitEnd(node: RuleExp)

    fun visitEnd(node: SequenceExp)

    fun visitEnd(node: BranchExp)

}
