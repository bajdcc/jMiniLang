package com.bajdcc.LALR1.syntax.automata.nga

import com.bajdcc.LALR1.grammar.semantic.ISemanticAction
import com.bajdcc.LALR1.syntax.exp.RuleExp
import com.bajdcc.LALR1.syntax.exp.TokenExp
import com.bajdcc.LALR1.syntax.handler.IErrorHandler

/**
 * 非确定性文法自动机边数据
 * [type] 边类型
 * [token] 终结符数据
 * [rule] 非终结符数据
 * [storage] 存储序号（-1为无效）
 * [handler] 错误处理器
 * [action] 语义动作
 * @author bajdcc
 */
class NGAEdgeData(var type: NGAEdgeType = NGAEdgeType.EPSILON,
                  var token: TokenExp? = null,
                  var rule: RuleExp? = null,
                  var storage: Int = -1,
                  var handler: IErrorHandler? = null,
                  var action: ISemanticAction? = null)