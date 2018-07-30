package com.bajdcc.OP.syntax.rule

import com.bajdcc.OP.syntax.ISyntaxComponent

/**
 * 文法规则部件（文法推导式）
 *
 * @author bajdcc
 */
class RuleItem(var expression: ISyntaxComponent,
               var parent: Rule)
