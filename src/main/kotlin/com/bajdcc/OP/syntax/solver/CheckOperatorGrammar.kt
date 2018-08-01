package com.bajdcc.OP.syntax.solver

import com.bajdcc.OP.syntax.ISyntaxComponentVisitor
import com.bajdcc.OP.syntax.exp.BranchExp
import com.bajdcc.OP.syntax.exp.RuleExp
import com.bajdcc.OP.syntax.exp.SequenceExp
import com.bajdcc.OP.syntax.exp.TokenExp
import com.bajdcc.util.VisitBag

/**
 * 检查一个文法是否属于算符文法
 *
 * @author bajdcc
 */
class CheckOperatorGrammar : ISyntaxComponentVisitor {

    /**
     * 是否刚刚遍历过非终结符
     */
    private var nonTerminalJustPast = false

    /**
     * 重复的非终结符名称（假如存在）
     */
    /**
     * 重复的非终结符名称
     *
     * @return 重复的非终结符名称
     */
    var invalidName: String? = null
        private set

    /**
     * 检查是否合法
     *
     * @return 名称是否合法
     */
    val isValid: Boolean
        get() = invalidName == null

    override fun visitBegin(node: TokenExp, bag: VisitBag) {
        bag.visitChildren = false
        bag.visitEnd = false
        if (invalidName == null) {
            nonTerminalJustPast = false
        }
    }

    override fun visitBegin(node: RuleExp, bag: VisitBag) {
        bag.visitChildren = false
        bag.visitEnd = false
        if (nonTerminalJustPast) {
            invalidName = node.name
        } else {
            nonTerminalJustPast = true
        }
    }

    override fun visitBegin(node: SequenceExp, bag: VisitBag) {

    }

    override fun visitBegin(node: BranchExp, bag: VisitBag) {
        bag.visitEnd = false
    }

    override fun visitEnd(node: TokenExp) {

    }

    override fun visitEnd(node: RuleExp) {

    }

    override fun visitEnd(node: SequenceExp) {
        nonTerminalJustPast = false
    }

    override fun visitEnd(node: BranchExp) {

    }
}
