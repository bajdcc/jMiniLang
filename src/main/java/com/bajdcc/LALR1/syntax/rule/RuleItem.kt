package com.bajdcc.LALR1.syntax.rule

import com.bajdcc.LALR1.grammar.semantic.ISemanticAnalyzer
import com.bajdcc.LALR1.syntax.ISyntaxComponent
import com.bajdcc.LALR1.syntax.exp.RuleExp
import com.bajdcc.LALR1.syntax.exp.TokenExp

/**
 * 文法规则部件（文法推导式）
 * [expression] 规则表达式
 * [parent] 父结点指针
 * @author bajdcc
 */
class RuleItem(var expression: ISyntaxComponent,
               var parent: Rule) {

    /**
     * 规则对应的语义分析接口
     */
    var handler: ISemanticAnalyzer? = null

    /**
     * First集合（终结符）
     */
    var setFirstSetTokens = mutableSetOf<TokenExp>()

    /**
     * First集合（非终结符）
     */
    var setFirstSetRules = mutableSetOf<RuleExp>()
}
