package com.bajdcc.util.lexer.automata.nfa

/**
 * NFA边
 * [data] 数据
 * @author bajdcc
 */
data class NFAEdge(var begin: NFAStatus? = null,
                   var end: NFAStatus? = null,
                   var data: NFAEdgeData = NFAEdgeData())