package com.bajdcc.OP.syntax.solver

import com.bajdcc.OP.syntax.ISyntaxComponentVisitor
import com.bajdcc.OP.syntax.exp.BranchExp
import com.bajdcc.OP.syntax.exp.RuleExp
import com.bajdcc.OP.syntax.exp.SequenceExp
import com.bajdcc.OP.syntax.exp.TokenExp
import com.bajdcc.util.VisitBag

/**
 * 求解非终结符的FirstVT集合
 *
 * @author bajdcc
 */
class FirstVTSolver(private val origin: RuleExp) : ISyntaxComponentVisitor {

    /**
     * 是否更新，作为算法收敛的依据
     */
    /**
     * 是否更改
     *
     * @return 经过此次计算，集合是否更新
     */
    var isUpdated = false
        private set

    /**
     * 当前规则中止
     */
    private var stop = false

    override fun visitBegin(node: TokenExp, bag: VisitBag) {
        bag.visitChildren = false
        bag.visitEnd = false
        if (!stop) {
            isUpdated = origin.rule.setFirstVT.add(node)// 直接加入终结符，并中止遍历
            stop = true
        }
    }

    override fun visitBegin(node: RuleExp, bag: VisitBag) {
        bag.visitChildren = false
        bag.visitEnd = false
        if (!stop) {
            isUpdated = origin.rule.setFirstVT.addAll(node.rule.setFirstVT)// 加入当前非终结符的FirstVT
        }
    }

    override fun visitBegin(node: SequenceExp, bag: VisitBag) {
        bag.visitEnd = false
        stop = false
    }

    override fun visitBegin(node: BranchExp, bag: VisitBag) {
        bag.visitEnd = false
    }

    override fun visitEnd(node: TokenExp) {

    }

    override fun visitEnd(node: RuleExp) {

    }

    override fun visitEnd(node: SequenceExp) {

    }

    override fun visitEnd(node: BranchExp) {

    }
}
