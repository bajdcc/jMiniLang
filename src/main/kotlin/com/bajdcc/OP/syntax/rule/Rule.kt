package com.bajdcc.OP.syntax.rule

import com.bajdcc.OP.syntax.exp.RuleExp
import com.bajdcc.OP.syntax.exp.TokenExp

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

    var arrFirstVT = mutableListOf<TokenExp>()

    var arrLastVT = mutableListOf<TokenExp>()

    var setFirstVT = mutableSetOf<TokenExp>()

    var setLastVT = mutableSetOf<TokenExp>()
}
