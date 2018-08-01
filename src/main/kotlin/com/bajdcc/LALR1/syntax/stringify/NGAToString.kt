package com.bajdcc.LALR1.syntax.stringify

import com.bajdcc.LALR1.syntax.automata.nga.NGAEdge
import com.bajdcc.LALR1.syntax.automata.nga.NGAEdgeType
import com.bajdcc.LALR1.syntax.automata.nga.NGAStatus
import com.bajdcc.util.VisitBag
import com.bajdcc.util.lexer.automata.BreadthFirstSearch

/**
 * NGA序列化（宽度优先搜索）
 *
 * @author bajdcc 状态类型
 */
class NGAToString(prefix: String) : BreadthFirstSearch<NGAEdge, NGAStatus>() {

    /**
     * 描述
     */
    private val context = StringBuilder()

    /**
     * 前缀
     */
    private val prefix = prefix

    /**
     * 存放状态的集合
     */
    private var arrNGAStatus = mutableListOf<NGAStatus>()

    override fun visitBegin(status: NGAStatus, bag: VisitBag) {
        /* 若首次访问节点则先构造状态表 */
        if (arrNGAStatus.isEmpty()) {
            val bfs = BreadthFirstSearch<NGAEdge, NGAStatus>()
            status.visit(bfs)
            arrNGAStatus = bfs.arrStatus
        }
        /* 输出状态标签 */
        appendLine()
        appendPrefix()
        context.append("--== 状态[").append(arrNGAStatus.indexOf(status)).append("]")
                .append(if (status.data.final) "[结束]" else "").append(" ==--")
        appendLine()
        appendPrefix()
        context.append("项目： ").append(status.data.label)
        appendLine()
        /* 输出边 */
        for (edge in status.outEdges) {
            appendPrefix()
            context.append("\t到达 ").append(arrNGAStatus.indexOf(edge.end)).append("  ：  ")
            context.append(edge.data.type.desc)
            when (edge.data.type) {
                NGAEdgeType.EPSILON -> {}
                NGAEdgeType.RULE -> context.append(" = ").append(edge.data.rule)
                NGAEdgeType.TOKEN -> context.append(" = ").append(edge.data.token)
            }
            if (edge.data.action != null)
                context.append("  ").append("[Action]")
            appendLine()
        }
    }

    /**
     * 添加前缀
     */
    private fun appendPrefix() {
        context.append(prefix)
    }

    /**
     * 添加行
     */
    private fun appendLine() {
        context.append(System.lineSeparator())
    }

    override fun toString(): String {
        return context.toString()
    }
}
