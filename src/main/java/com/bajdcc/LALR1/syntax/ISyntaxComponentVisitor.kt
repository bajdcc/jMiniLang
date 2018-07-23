package com.bajdcc.LALR1.syntax

import com.bajdcc.LALR1.syntax.exp.*
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

    fun visitBegin(node: OptionExp, bag: VisitBag)

    fun visitBegin(node: PropertyExp, bag: VisitBag)

    fun visitEnd(node: TokenExp)

    fun visitEnd(node: RuleExp)

    fun visitEnd(node: SequenceExp)

    fun visitEnd(node: BranchExp)

    fun visitEnd(node: OptionExp)

    fun visitEnd(node: PropertyExp)
}
