package com.bajdcc.util.lexer.automata.dfa

import com.bajdcc.util.lexer.automata.EdgeType
import com.bajdcc.util.lexer.automata.nfa.NFAEdge

/**
 * DFA边数据
 * [type] 边类型
 * [param] 数据
 * [nfaEdges] NFA边集合
 * @author bajdcc
 */
data class DFAEdgeData (var type: EdgeType = EdgeType.EPSILON,
                        var param: Int = -1,
                        var nfaEdges: MutableList<NFAEdge> = mutableListOf())