package com.bajdcc.util.lexer.automata

import com.bajdcc.util.VisitBag

/**
 * 宽度优先搜索
 *
 * @author bajdcc 状态类型
 */
open class BreadthFirstSearch<Edge, Status> : IBreadthFirstSearch<Edge, Status> {

    /**
     * 存放状态的集合
     */
    var arrStatus = mutableListOf<Status>()

    override fun testEdge(edge: Edge): Boolean {
        return true
    }

    override fun visitBegin(status: Status, bag: VisitBag) {

    }

    override fun visitEnd(status: Status) {

    }
}
