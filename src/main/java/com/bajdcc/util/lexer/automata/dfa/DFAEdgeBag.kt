package com.bajdcc.util.lexer.automata.dfa

import com.bajdcc.util.lexer.automata.EdgeType
import com.bajdcc.util.lexer.automata.nfa.NFAEdge
import com.bajdcc.util.lexer.automata.nfa.NFAStatus

/**
 * DFA边辅助数据结构
 * [type] 边类型
 * [param] 数据
 * [nfaEdges] NFA边集合
 * [nfaStatus] NFA状态集合
 * @author bajdcc
 */
data class DFAEdgeBag(var type: EdgeType = EdgeType.EPSILON,
                      var param: Int = -1,
                      var nfaEdges: MutableList<NFAEdge> = mutableListOf(),
                      var nfaStatus: MutableSet<NFAStatus> = mutableSetOf()) {

    /**
     * 获得状态编号描述（逗号分隔）
     *
     * @param dfaStatusList 状态表
     * @return 状态编号描述
     */
    fun getStatusString(dfaStatusList: List<NFAStatus>): String {
        val sb = StringBuilder()
        for (status in nfaStatus) {
            sb.append(dfaStatusList.indexOf(status)).append(",")
        }
        return sb.toString()
    }
}
