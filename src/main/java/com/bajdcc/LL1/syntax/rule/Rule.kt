package com.bajdcc.LL1.syntax.rule

import com.bajdcc.LL1.syntax.exp.RuleExp
import com.bajdcc.LL1.syntax.exp.TokenExp

/**
 * 文法规则（文法推导式表）
 *
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
    var arrFirsts = mutableListOf<TokenExp>()

    /**
     * 终结符Follow集合
     */
    var setFollows = mutableSetOf<TokenExp>()

    /**
     * First集合（终结符）
     */
    var arrFollows = mutableListOf<TokenExp>()

    /**
     * 是否可产生空串
     */
    var epsilon = false
}
