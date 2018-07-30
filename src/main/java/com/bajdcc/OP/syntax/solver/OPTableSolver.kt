package com.bajdcc.OP.syntax.solver

import com.bajdcc.OP.syntax.ISyntaxComponentVisitor
import com.bajdcc.OP.syntax.exp.BranchExp
import com.bajdcc.OP.syntax.exp.RuleExp
import com.bajdcc.OP.syntax.exp.SequenceExp
import com.bajdcc.OP.syntax.exp.TokenExp
import com.bajdcc.OP.syntax.token.PrecedenceType
import com.bajdcc.util.VisitBag

/**
 * 求解算符优先表
 *
 * @author bajdcc
 */
abstract class OPTableSolver : ISyntaxComponentVisitor {

    /**
     * 当前符号在产生式中的位置
     */
    private var index = -1

    /**
     * 上一个终结符
     */
    private var lastToken: TokenExp? = null

    /**
     * 上上个终结符
     */
    private var lastlastToken: TokenExp? = null

    /**
     * 上一个非终结符
     */
    private var lastRule: RuleExp? = null

    /**
     * 填写算符优先分析表中的某一项
     *
     * @param x    行
     * @param y    列
     * @param type 类型（LT，GT，EQ）
     */
    protected abstract fun setCell(x: Int, y: Int, type: PrecedenceType)

    override fun visitBegin(node: TokenExp, bag: VisitBag) {
        bag.visitChildren = false
        if (index > 0) {
            if (lastToken != null) {
                // [b=node,a=lastToken]
                // A->...ab...直接得出a=b
                // lastToken=node
                setCell(lastToken!!.id, node.id, PrecedenceType.EQ)
            } else {
                // [b=node,a=token=LastVT(R),R=lastRule]
                // A->...Rb...且a属于LastVT(R)才能得出a>b
                // token>node
                for (token in lastRule!!.rule.arrLastVT) {
                    setCell(token.id, node.id, PrecedenceType.GT)
                }
                if (index > 1) {
                    // [b=node,a=lastlastToken,R=lastRule]
                    // A->...aRb...直接得出a=b
                    // lastlastToken>node
                    setCell(lastlastToken!!.id, node.id, PrecedenceType.EQ)
                }
            }
        }
    }

    override fun visitBegin(node: RuleExp, bag: VisitBag) {
        bag.visitChildren = false
        if (index > 0) {
            // [a=lastToken,b=token=FirstVT(R),R=node]
            // A->...aR...且b属于FirstVT(R)才能得出a<b
            // lastToken<token
            for (token in node.rule.arrFirstVT) {
                setCell(lastToken!!.id, token.id, PrecedenceType.LT)
            }
        }
    }

    override fun visitBegin(node: SequenceExp, bag: VisitBag) {
        bag.visitEnd = false
        index = 0
    }

    override fun visitBegin(node: BranchExp, bag: VisitBag) {
        bag.visitEnd = false
    }

    override fun visitEnd(node: TokenExp) {
        index++
        lastlastToken = lastToken
        lastToken = node
        lastRule = null
    }

    override fun visitEnd(node: RuleExp) {
        index++
        lastlastToken = lastToken
        lastToken = null
        lastRule = node
    }

    override fun visitEnd(node: SequenceExp) {

    }

    override fun visitEnd(node: BranchExp) {

    }
}
