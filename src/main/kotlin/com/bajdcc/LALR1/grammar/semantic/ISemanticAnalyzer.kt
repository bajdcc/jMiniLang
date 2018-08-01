package com.bajdcc.LALR1.grammar.semantic

import com.bajdcc.LALR1.grammar.symbol.IQuerySymbol
import com.bajdcc.LALR1.semantic.token.IIndexedData

/**
 * 语义分析接口
 *
 * @author bajdcc
 */
interface ISemanticAnalyzer {

    /**
     * 语义处理接口
     *
     * @param indexed  索引包接口
     * @param query    单词工厂接口
     * @param recorder 语义错误记录接口
     * @return 处理后的数据
     */
    fun handle(indexed: IIndexedData, query: IQuerySymbol,
               recorder: ISemanticRecorder): Any
}
