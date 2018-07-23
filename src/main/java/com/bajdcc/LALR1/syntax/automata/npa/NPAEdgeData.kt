package com.bajdcc.LALR1.syntax.automata.npa

import com.bajdcc.LALR1.semantic.token.ISemanticAction
import com.bajdcc.LALR1.syntax.handler.IErrorHandler

/**
 * 非确定性下推自动机边数据
 * [type] 边类型
 * [inst] 指令
 * [index] 指令参数
 * [handler] 处理序号
 * [status] 状态参数
 * [token] 记号参数
 * [lookaheads] LookAhead表
 * [error] 错误处理器
 * [action] 语义动作
 * [errorJump] 出错后跳转的状态
 * @author bajdcc
 */
data class NPAEdgeData(var type: NPAEdgeType = NPAEdgeType.MOVE,
                       var inst: NPAInstruction = NPAInstruction.PASS,
                       var index: Int = -1,
                       var handler: Int = -1,
                       var status: NPAStatus? = null,
                       var token: Int = -1,
                       var lookaheads: MutableSet<Int> = mutableSetOf(),
                       var error: IErrorHandler? = null,
                       var action: ISemanticAction? = null,
                       var errorJump: NPAStatus? = null)