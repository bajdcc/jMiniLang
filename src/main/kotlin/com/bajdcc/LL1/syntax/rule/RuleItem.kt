package com.bajdcc.LL1.syntax.rule

import com.bajdcc.LL1.syntax.ISyntaxComponent
import com.bajdcc.LL1.syntax.exp.RuleExp
import com.bajdcc.LL1.syntax.exp.TokenExp

/**
 * 文法规则部件（文法推导式）
 *
 * @author bajdcc
 */
class RuleItem(var expression: ISyntaxComponent,
               var parent: Rule) {

    /**
     * First集合（终结符）
     */
    var setFirstSetTokens = mutableSetOf<TokenExp>()

    /**
     * First集合（终结符）
     */
    var arrFirstSetTokens = mutableListOf<TokenExp>()

    /**
     * First集合（非终结符）
     */
    var setFirstSetRules = mutableSetOf<RuleExp>()

    /**
     * 是否产生空串
     */
    var epsilon = false
}
