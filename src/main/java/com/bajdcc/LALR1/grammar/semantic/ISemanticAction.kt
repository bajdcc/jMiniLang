package com.bajdcc.LALR1.grammar.semantic

import com.bajdcc.LALR1.grammar.symbol.IManageSymbol
import com.bajdcc.LALR1.semantic.token.IIndexedData
import com.bajdcc.LALR1.semantic.token.IRandomAccessOfTokens

/**
 * 【语义分析】语义动作接口
 *
 * @author bajdcc
 */
interface ISemanticAction {

    /**
     * 处理语义动作
     *
     * @param indexed  参数
     * @param manage   符号表管理接口
     * @param access   单词流
     * @param recorder 错误记录器
     */
    fun handle(indexed: IIndexedData, manage: IManageSymbol,
               access: IRandomAccessOfTokens, recorder: ISemanticRecorder)
}
