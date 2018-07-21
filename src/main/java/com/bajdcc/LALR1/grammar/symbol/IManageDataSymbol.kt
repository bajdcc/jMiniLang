package com.bajdcc.LALR1.grammar.symbol

import com.bajdcc.LALR1.grammar.tree.Function
import com.bajdcc.util.HashListMap
import com.bajdcc.util.HashListMapEx

/**
 * 符号表数据接口
 *
 * @author bajdcc
 */
interface IManageDataSymbol {

    /**
     * @return 符号表
     */
    val symbolList: HashListMap<Any>

    /**
     * @return 过程表
     */
    val funcMap: HashListMapEx<String, MutableList<Function>>
}
