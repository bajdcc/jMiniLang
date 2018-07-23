package com.bajdcc.LALR1.syntax.rule

import com.bajdcc.LALR1.syntax.exp.RuleExp
import com.bajdcc.LALR1.syntax.exp.TokenExp

/**
 * 文法规则（文法推导式表）
 * [nonTerminal] 规则起始非终结符
 * @author bajdcc
 */
class Rule(var nonTerminal: RuleExp) {

    /**
     * 规则表达式列表
     */
    var arrRules = mutableListOf<RuleItem>()

    /**
     * 左递归等级：0为否，1为直接，大于1为间接
     */
    var iRecursiveLevel = 0

    /**
     * 终结符First集合
     */
    var arrTokens = mutableListOf<TokenExp>()
}
