package com.bajdcc.util.lexer.automata.nfa

import com.bajdcc.util.VisitBag
import com.bajdcc.util.lexer.automata.BreadthFirstSearch

/**
 * NFA状态
 * [data] 数据
 * @author bajdcc
 */
class NFAStatus(var data: NFAStatusData = NFAStatusData()) {
    /**
     * 出边集合
     */
    var outEdges = mutableListOf<NFAEdge>()

    /**
     * 入边集合
     */
    var inEdges = mutableListOf<NFAEdge>()

    /**
     * 用于遍历包括该状态在内的所有状态（连通），结果存放在PATH中
     *
     * @param bfs 遍历算法
     */
    fun visit(bfs: BreadthFirstSearch<NFAEdge, NFAStatus>) {
        val stack = bfs.arrStatus
        val set = mutableSetOf<NFAStatus>()
        stack.clear()
        set.add(this)
        stack.add(this)
        var i = 0
        while (i < stack.size) {// 遍历每个状态
            val status = stack[i]
            val bag = VisitBag()
            bfs.visitBegin(status, bag)
            if (bag.visitChildren) {
                // 遍历状态的出边
                // 边未被访问，且边类型符合要求
                status.outEdges.stream().filter { edge -> !set.contains(edge.end) && bfs.testEdge(edge) }.forEach {// 边未被访问，且边类型符合要求
                    edge ->
                    stack.add(edge.end!!)
                    set.add(edge.end!!)
                }
            }
            if (bag.visitEnd) {
                bfs.visitEnd(status)
            }
            i++
        }
    }
}
