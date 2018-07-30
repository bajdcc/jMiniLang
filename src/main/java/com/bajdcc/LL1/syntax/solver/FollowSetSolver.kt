package com.bajdcc.LL1.syntax.solver

import com.bajdcc.LL1.syntax.ISyntaxComponentVisitor
import com.bajdcc.LL1.syntax.exp.BranchExp
import com.bajdcc.LL1.syntax.exp.RuleExp
import com.bajdcc.LL1.syntax.exp.SequenceExp
import com.bajdcc.LL1.syntax.exp.TokenExp
import com.bajdcc.util.VisitBag

/**
 * 求解一个产生式的Follow集合
 *
 * @author bajdcc
 */
class FollowSetSolver(
        private val origin: RuleExp,
        private val target: RuleExp) : ISyntaxComponentVisitor {

    /**
     * Follow集是否更新，作为算法收敛的依据
     */
    /**
     * Follow集是否更改
     *
     * @return 经过此次计算，Follow集是否更新
     */
    var isUpdated = false
        private set

    /**
     * 上一次是否刚遍历过当前求解的非终结符
     */
    private var enable = false

    /**
     * 添加非终结符Follow集
     *
     *
     * A=origin，B=target
     *
     *
     *
     * 添加Follow集（A->...B或A->...B(...->epsilon)）
     *
     *
     *
     * Follow(B) = Follow(A) U Follow(B)
     *
     */
    private fun addVnFollowToFollow() {
        setUpdate(target.rule.setFollows.addAll(origin.rule.setFollows))
    }

    /**
     * 添加非终结符First集
     *
     *
     * A=origin，B=target，beta=exp=Vn
     *
     *
     *
     * 添加First集（A->...B beta）
     *
     *
     *
     * Follow(B) = Follow(A) U First(beta)
     *
     *
     * @param exp 非终结符
     */
    private fun addVnFirstToFollow(exp: RuleExp) {
        setUpdate(target.rule.setFollows.addAll(exp.rule.arrFirsts))
    }

    /**
     *
     *
     * A=origin，B=target，beta=exp=Vt
     *
     *
     *
     * 添加终结符（A->...B beta）
     *
     *
     *
     * Follow(B) = Follow(A) U beta
     *
     *
     * @param exp 终结符
     */
    private fun addVtFirstToFollow(exp: TokenExp) {
        setUpdate(target.rule.setFollows.add(exp))
    }

    /**
     * 设置更新状态
     *
     * @param update 更新状态
     */
    private fun setUpdate(update: Boolean) {
        if (update) {
            this.isUpdated = true
        }
    }

    override fun visitBegin(node: TokenExp, bag: VisitBag) {
        bag.visitChildren = false
        bag.visitEnd = false
        if (enable) {// 上次经过非终结符
            addVtFirstToFollow(node)
            enable = false
        }
    }

    override fun visitBegin(node: RuleExp, bag: VisitBag) {
        bag.visitChildren = false
        bag.visitEnd = false
        if (enable) {// 上次经过非终结符
            addVnFirstToFollow(node)
            if (!node.rule.epsilon) {
                enable = false
            }
        }
        if (node.rule === target.rule) {// 设置为经过非终结符
            enable = true
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
        if (enable) {// 上次经过非终结符
            addVnFollowToFollow()
        }
        enable = false
    }

    override fun visitEnd(node: BranchExp) {

    }
}
