package com.bajdcc.util.lexer.automata.dfa

import com.bajdcc.util.VisitBag
import com.bajdcc.util.lexer.automata.BreadthFirstSearch

/**
 * DFA状态
 *
 * @author bajdcc
 */
class DFAStatus {
    /**
     * 出边集合
     */
    var outEdges = mutableListOf<DFAEdge>()

    /**
     * 入边集合
     */
    var inEdges = mutableListOf<DFAEdge>()

    /**
     * 数据
     */
    var data = DFAStatusData()

    /**
     * 用于遍历包括该状态在内的所有状态（连通），结果存放在PATH中
     *
     * @param bfs 遍历算法
     */
    fun visit(bfs: BreadthFirstSearch<DFAEdge, DFAStatus>) {
        val stack = bfs.arrStatus
        val set = mutableSetOf<DFAStatus>()
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
                // 注：这里有坑，如果去掉stream()，使用kotlin遍历时，会先filter到一个list中，没有达到流的效果！
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
