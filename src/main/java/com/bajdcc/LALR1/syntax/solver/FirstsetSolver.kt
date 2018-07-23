package com.bajdcc.LALR1.syntax.solver

import com.bajdcc.LALR1.syntax.ISyntaxComponentVisitor
import com.bajdcc.LALR1.syntax.exp.*
import com.bajdcc.LALR1.syntax.rule.RuleItem
import com.bajdcc.util.VisitBag
import java.util.*

/**
 * 求解一个产生式的First集合
 *
 * @author bajdcc
 */
class FirstsetSolver : ISyntaxComponentVisitor {

    /**
     * 终结符表
     */
    private val setTokens = mutableSetOf<TokenExp>()

    /**
     * 非终结符表
     */
    private val setRules = mutableSetOf<RuleExp>()

    /**
     * 产生式推导的串长度是否可能为零
     */
    private var bZero = true

    /**
     * 求解
     *
     * @param target 目标产生式对象
     * @return 产生式是否合法
     */
    fun solve(target: RuleItem): Boolean {
        if (bZero) {
            return false
        }
        target.setFirstSetTokens = HashSet(setTokens)
        target.setFirstSetRules = HashSet(setRules)
        return true
    }

    override fun visitBegin(node: TokenExp, bag: VisitBag) {
        bag.visitChildren = false
        bag.visitEnd = false
        setTokens.add(node)
        if (bZero) {
            bZero = false
        }
    }

    override fun visitBegin(node: RuleExp, bag: VisitBag) {
        bag.visitChildren = false
        bag.visitEnd = false
        setRules.add(node)
        if (bZero) {
            bZero = false
        }
    }

    override fun visitBegin(node: SequenceExp, bag: VisitBag) {
        bag.visitChildren = false
        bag.visitEnd = false
        var zero = false
        for (exp in node.arrExpressions) {
            exp.visit(this)
            zero = bZero
            if (!zero) {
                break
            }
        }
        bZero = zero
    }

    override fun visitBegin(node: BranchExp, bag: VisitBag) {
        bag.visitChildren = false
        bag.visitEnd = false
        var zero = false
        for (exp in node.arrExpressions) {
            exp.visit(this)
            if (bZero) {
                zero = bZero
            }
        }
        bZero = zero
    }

    override fun visitBegin(node: OptionExp, bag: VisitBag) {
        bag.visitChildren = false
        bag.visitEnd = false
        node.expression.visit(this)
        bZero = true
    }

    override fun visitBegin(node: PropertyExp, bag: VisitBag) {
        bag.visitChildren = false
        bag.visitEnd = false
        node.expression.visit(this)
        bZero = false
    }

    override fun visitEnd(node: TokenExp) {

    }

    override fun visitEnd(node: RuleExp) {

    }

    override fun visitEnd(node: SequenceExp) {

    }

    override fun visitEnd(node: BranchExp) {

    }

    override fun visitEnd(node: OptionExp) {

    }

    override fun visitEnd(node: PropertyExp) {

    }
}
