package com.bajdcc.util.lexer.automata.dfa

import com.bajdcc.util.lexer.automata.nfa.NFAStatus
import java.util.*

/**
 * DFA状态数据
 * [bFinal] 是否为终态
 * [nfaStatus] NFA状态集合
 * @author bajdcc
 */
data class DFAStatusData(var bFinal: Boolean = false,
                         var nfaStatus: MutableList<NFAStatus> = mutableListOf()) {
    /**
     * 获得状态编号描述（逗号分隔）
     *
     * @param dfaStatusList 状态表
     * @return 状态编号描述
     */
    fun getStatusString(dfaStatusList: List<NFAStatus>): String {
        if (nfaStatus.isEmpty()) {
            return ""
        }
        val orders = IntArray(nfaStatus.size)
        orders.indices.forEach { i ->
            orders[i] = dfaStatusList.indexOf(nfaStatus[i])
        }
        Arrays.sort(orders)
        val sb = StringBuilder()
        orders.forEach { order ->
            sb.append(order).append(",")
        }
        return sb.toString()
    }
}
