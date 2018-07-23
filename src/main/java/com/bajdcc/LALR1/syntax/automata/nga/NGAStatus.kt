package com.bajdcc.LALR1.syntax.automata.nga

import com.bajdcc.util.VisitBag
import com.bajdcc.util.lexer.automata.BreadthFirstSearch

/**
 * 非确定性文法自动机状态
 *
 * @author bajdcc
 */
class NGAStatus {
    /**
     * 出边集合
     */
    var outEdges = mutableListOf<NGAEdge>()

    /**
     * 入边集合
     */
    var inEdges = mutableListOf<NGAEdge>()

    /**
     * 数据
     */
    var data = NGAStatusData()

    /**
     * 用于遍历包括该状态在内的所有状态（连通），结果存放在PATH中
     *
     * @param bfs 遍历算法
     */
    fun visit(bfs: BreadthFirstSearch<NGAEdge, NGAStatus>) {
        val stack = bfs.arrStatus
        val set = mutableSetOf<NGAStatus>()
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
