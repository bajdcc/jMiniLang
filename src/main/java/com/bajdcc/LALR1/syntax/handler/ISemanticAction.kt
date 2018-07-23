package com.bajdcc.LALR1.syntax.handler

import com.bajdcc.LALR1.grammar.symbol.IManageSymbol

/**
 * 【语义分析】语义动作接口
 *
 * @author bajdcc
 */
interface ISemanticAction {
    /**
     * 处理语义动作
     *
     * @param manage 符号表管理接口
     */
    fun handle(manage: IManageSymbol)
}
