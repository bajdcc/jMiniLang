package com.bajdcc.LL1.syntax.solver

import com.bajdcc.LL1.syntax.ISyntaxComponentVisitor
import com.bajdcc.LL1.syntax.exp.BranchExp
import com.bajdcc.LL1.syntax.exp.RuleExp
import com.bajdcc.LL1.syntax.exp.SequenceExp
import com.bajdcc.LL1.syntax.exp.TokenExp
import com.bajdcc.LL1.syntax.token.PredictType
import com.bajdcc.util.VisitBag
import com.bajdcc.util.lexer.token.TokenType

/**
 * 求解一个产生式的Select集合
 *
 * @author bajdcc
 */
abstract class SelectSetSolver : ISyntaxComponentVisitor {

    /**
     * 是否为产生式的第一个终结符
     */
    private var firstSymbol = false

    /**
     * 是否需要把当前符号添加进指令集
     */
    private var insertSymbol = false

    /**
     * 当前产生式从左到右片段是否产生空串
     */
    private var epsilon = true

    /**
     * 当前产生式规则是否可以推导出空串
     *
     * @return 是否可以推导出空串
     */
    protected abstract val isEpsilon: Boolean

    /**
     * 获得当前产生式左部的Follow集
     *
     * @return Follow集
     */
    protected abstract val follow: Collection<TokenExp>

    /**
     * 设置预测分析表的某一项为当前产生式
     *
     * @param token 列索引，终结符
     */
    protected abstract fun setCellToRuleId(token: Int)

    /**
     * 当前处理的规则ID自增（一条产生式有多个规则）
     */
    protected abstract fun addRule()

    /**
     * 给当前处理的规则添加非终结符指令（预测分析表中的产生式规则）
     *
     * @param type 指令类型（VT或VN）
     * @param inst 指令ID（索引）
     */
    protected abstract fun addInstToRule(type: PredictType, inst: Int)

    override fun visitBegin(node: TokenExp, bag: VisitBag) {
        bag.visitChildren = false
        bag.visitEnd = false
        if (node.kType === TokenType.EOF) {// Epsilon
            addRule()// 空串，则指令集为空（不压入新状态）
            for (token in follow) {// 有空串，添加Follow集
                setCellToRuleId(token.id)
            }
            firstSymbol = false
        } else if (firstSymbol) {
            addRule()// 需要添加指令集
            setCellToRuleId(node.id)
            firstSymbol = false
            insertSymbol = true
        }
        if (insertSymbol) {
            addInstToRule(PredictType.TERMINAL, node.id)
        }
        if (node.kType !== TokenType.EOF) {
            epsilon = false
        }
    }

    override fun visitBegin(node: RuleExp, bag: VisitBag) {
        bag.visitChildren = false
        bag.visitEnd = false
        if (firstSymbol) {
            addRule()// 需要添加指令集
            insertSymbol = true
            firstSymbol = false
        }
        if (insertSymbol) {
            if (epsilon) {
                // 添加First集
                node.rule.arrFirsts
                        .stream()
                        .filter { token -> token.kType !== TokenType.EOF }
                        .forEach { token -> setCellToRuleId(token.id) }
            }
        }
        addInstToRule(PredictType.NONTERMINAL, node.id)
        if (!node.rule.epsilon) {
            epsilon = false
        }
    }

    override fun visitBegin(node: SequenceExp, bag: VisitBag) {
        bag.visitEnd = false
        firstSymbol = true
        insertSymbol = false
        epsilon = true
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
