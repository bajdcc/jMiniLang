package com.bajdcc.util.lexer.automata.dfa

/**
 * DFA边
 *
 * @author bajdcc
 */
class DFAEdge {
    /**
     * 初态
     */
    var begin: DFAStatus? = null

    /**
     * 终态
     */
    var end: DFAStatus? = null

    /**
     * 数据
     */
    var data = DFAEdgeData()
}