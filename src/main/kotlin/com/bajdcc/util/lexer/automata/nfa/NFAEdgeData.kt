package com.bajdcc.util.lexer.automata.nfa

import com.bajdcc.util.lexer.automata.EdgeType

/**
 * NFA边数据
 * [type] 边类型
 * [param] 数据
 * @author bajdcc
 */
data class NFAEdgeData(var type: EdgeType = EdgeType.EPSILON,
                       var param: Int = -1)
