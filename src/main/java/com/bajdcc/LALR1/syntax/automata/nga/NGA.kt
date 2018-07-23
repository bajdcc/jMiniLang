package com.bajdcc.LALR1.syntax.automata.nga

import com.bajdcc.LALR1.syntax.ISyntaxComponent
import com.bajdcc.LALR1.syntax.ISyntaxComponentVisitor
import com.bajdcc.LALR1.syntax.Syntax
import com.bajdcc.LALR1.syntax.exp.*
import com.bajdcc.LALR1.syntax.rule.RuleItem
import com.bajdcc.LALR1.syntax.stringify.NGAToString
import com.bajdcc.util.VisitBag
import com.bajdcc.util.lexer.automata.BreadthFirstSearch

/**
 * **非确定性文法自动机**（**NGA**）构成算法（**AST-&gt;NGA**）
 * *功能：进行LR项目集的计算*
 * [arrNonTerminals] 非终结符集合
 * [arrTerminals] 终结符集合
 * @author bajdcc
 */
open class NGA(protected var arrNonTerminals: List<RuleExp>,
               protected var arrTerminals: List<TokenExp>) : ISyntaxComponentVisitor {

    /**
     * 规则到文法自动机状态的映射
     */
    protected var mapNGA = mutableMapOf<RuleItem, NGAStatus>()

    /**
     * 保存结果的数据包
     */
    private var bag = NGABag()

    /**
     * 非确定性文法自动机描述
     *
     * @return 非确定性文法自动机描述
     */
    val ngaString: String
        get() {
            val sb = StringBuilder()
            sb.append("#### 产生式 ####")
            sb.append(System.lineSeparator())
            for (status in mapNGA.values) {
                sb.append(getNGAString(status, ""))
                sb.append(System.lineSeparator())
            }
            return sb.toString()
        }

    init {
        generateNGAMap()
    }

    /**
     * 连接两个状态
     *
     * @param begin 初态
     * @param end   终态
     * @return 新的边
     */
    private fun connect(begin: NGAStatus?, end: NGAStatus?): NGAEdge {
        val edge = NGAEdge()// 申请一条新边
        edge.begin = begin
        edge.end = end
        begin!!.outEdges.add(edge)// 添加进起始边的出边
        end!!.inEdges.add(edge)// 添加进结束边的入边
        return edge
    }

    /**
     * 断开某个状态和某条边
     *
     * @param status 某状态
     * @param edge   某条边
     */
    private fun disconnect(edge: NGAEdge) {
        edge.end!!.inEdges.remove(edge)// 当前边的结束状态的入边集合去除当前边
    }

    /**
     * 断开某个状态和所有边
     *
     * @param status 某状态
     */
    private fun disconnect(status: NGAStatus) {
        /* 清除所有入边 */
        run {
            val it = status.inEdges.iterator()
            while (it.hasNext()) {
                val edge = it.next()
                it.remove()
                disconnect(edge)
            }
        }
        /* 清除所有出边 */
        val it = status.outEdges.iterator()
        while (it.hasNext()) {
            val edge = it.next()
            it.remove()
            disconnect(edge)
        }
    }

    /**
     * 产生NGA映射表
     */
    private fun generateNGAMap() {
        for (exp in arrNonTerminals) {
            for ((i, item) in exp.rule.arrRules.withIndex()) {
                /* 表达式转换成NGA */
                bag = NGABag()
                bag.expression = item.expression
                bag.prefix = exp.name + "[" + i + "]"
                bag.expression!!.visit(this)
                val enga = bag.nga
                /* NGA去Epsilon边 */
                val status = deleteEpsilon(enga!!)
                /* 保存 */
                mapNGA[item] = status
            }
        }
    }

    /**
     * NGA去Epsilon边（与DFA去E边算法相似）
     *
     * @param enga ENGA
     * @return NGA状态
     */
    private fun deleteEpsilon(enga: ENGA): NGAStatus {
        /* 获取状态闭包 */
        val ngaStatusList = getNGAStatusClosure(BreadthFirstSearch(), enga.begin!!)
        /* 可到达状态集合 */
        val availableStatus = mutableListOf<NGAStatus>()
        /* 可到达标签集合 */
        val availableLabels = mutableListOf<String>()
        /* 可到达标签集哈希表（用于查找） */
        val availableLabelsSet = mutableSetOf<String>()
        /* 搜索所有有效状态 */
        availableStatus.add(ngaStatusList[0])
        availableLabels.add(ngaStatusList[0].data.label)
        availableLabelsSet.add(ngaStatusList[0].data.label)
        for (status in ngaStatusList) {
            if (status === ngaStatusList[0]) {// 排除第一个
                continue
            }
            val available = status.inEdges.isNotEmpty() && status.inEdges.any { it.data.type != NGAEdgeType.EPSILON }
            if (available && !availableLabelsSet.contains(status.data.label)) {
                availableStatus.add(status)
                availableLabels.add(status.data.label)
                availableLabelsSet.add(status.data.label)
            }
        }
        val epsilonBFS = object : BreadthFirstSearch<NGAEdge, NGAStatus>() {
            override fun testEdge(edge: NGAEdge): Boolean {
                return edge.data.type == NGAEdgeType.EPSILON
            }
        }
        /* 遍历所有有效状态 */
        for (status in availableStatus) {
            /* 获取当前状态的Epsilon闭包 */
            val epsilonClosure = getNGAStatusClosure(epsilonBFS, status)
            /* 去除自身状态 */
            epsilonClosure.remove(status)
            /* 遍历Epsilon闭包的状态 */
            for (epsilonStatus in epsilonClosure) {
                if (epsilonStatus.data.final) {
                    /* 如果闭包中有终态，则当前状态为终态 */
                    status.data.final = true
                }
                /* 遍历闭包中所有边 */
                epsilonStatus.outEdges.filter { edge -> edge.data.type != NGAEdgeType.EPSILON }.forEach { edge ->
                    /* 获得索引 */
                    val idx = availableLabels.indexOf(edge.end!!.data.label)
                    /* 如果当前边不是Epsilon边，就将闭包中的有效边添加到当前状态 */
                    connect(status, availableStatus[idx]).data = edge.data
                }
            }
        }
        /* 删除Epsilon边 */
        for (status in ngaStatusList) {
            val it = status.outEdges.iterator()
            while (it.hasNext()) {
                val edge = it.next()
                if (edge.data.type == NGAEdgeType.EPSILON) {
                    it.remove()
                    disconnect(edge)// 删除Epsilon边
                }
            }
        }
        /* 删除无效状态 */
        val unaccessiableStatus = ngaStatusList.filter { !availableStatus.contains(it) }
        for (status in unaccessiableStatus) {
            ngaStatusList.remove(status)// 删除无效状态
            disconnect(status)// 删除与状态有关的所有边
        }
        return enga.begin!!
    }

    /**
     * 开始遍历子结点
     */
    private fun beginChilren() {
        bag.stkNGA.push(mutableListOf())
    }

    /**
     * 结束遍历子结点
     */
    private fun endChilren() {
        bag.childNGA = bag.stkNGA.pop()
    }

    /**
     * 保存结果
     *
     * @param enga EpsilonNGA
     */
    private fun store(enga: ENGA) {
        if (bag.stkNGA.isEmpty()) {
            enga.end!!.data.final = true
            bag.nga = enga
        } else {
            bag.stkNGA.peek().add(enga)
        }
    }

    override fun visitBegin(node: TokenExp, bag: VisitBag) {

    }

    override fun visitBegin(node: RuleExp, bag: VisitBag) {

    }

    override fun visitBegin(node: SequenceExp, bag: VisitBag) {
        beginChilren()
    }

    override fun visitBegin(node: BranchExp, bag: VisitBag) {
        beginChilren()
    }

    override fun visitBegin(node: OptionExp, bag: VisitBag) {
        beginChilren()
    }

    override fun visitBegin(node: PropertyExp, bag: VisitBag) {
        beginChilren()
    }

    override fun visitEnd(node: TokenExp) {
        /* 新建ENGA */
        val enga = createENGA(node)
        /* 连接ENGA边并保存当前结点 */
        val edge = connect(enga.begin, enga.end)
        edge.data.type = NGAEdgeType.TOKEN
        edge.data.token = node
        store(enga)
    }

    override fun visitEnd(node: RuleExp) {
        /* 新建ENGA */
        val enga = createENGA(node)
        /* 连接ENGA边并保存当前结点 */
        val edge = connect(enga.begin, enga.end)
        edge.data.type = NGAEdgeType.RULE
        edge.data.rule = node
        store(enga)
    }

    override fun visitEnd(node: SequenceExp) {
        endChilren()
        /* 串联 */
        val enga = ENGA()
        for (child in bag.childNGA) {
            if (enga.begin != null) {
                connect(enga.end, child.begin)// 首尾相连
                enga.end = child.end
            } else {
                enga.begin = bag.childNGA[0].begin
                enga.end = bag.childNGA[0].end
            }
        }
        store(enga)
    }

    override fun visitEnd(node: BranchExp) {
        endChilren()
        /* 新建ENGA */
        val enga = createENGA(node)
        /* 并联 */
        for (child in bag.childNGA) {
            /* 复制标签 */
            child.begin!!.data.label = enga.begin!!.data.label
            child.end!!.data.label = enga.end!!.data.label
            /* 连接首尾 */
            connect(enga.begin, child.begin)
            connect(child.end, enga.end)
        }
        store(enga)
    }

    override fun visitEnd(node: OptionExp) {
        endChilren()
        /* 获得唯一的一个子结点 */
        val enga = bag.childNGA[0]
        enga.begin!!.data.label = Syntax.getSingleString(
                bag.prefix, bag.expression!!, node, true)
        enga.end!!.data.label = Syntax.getSingleString(
                bag.prefix, bag.expression!!, node, false)
        /* 添加可选边，即Epsilon边 */
        connect(enga.begin, enga.end)
        store(enga)
    }

    override fun visitEnd(node: PropertyExp) {
        endChilren()
        /* 获得唯一的一个子结点 */
        val enga = bag.childNGA[0]
        enga.begin!!.data.label = Syntax.getSingleString(
                bag.prefix, bag.expression!!, node, true)
        enga.end!!.data.label = Syntax.getSingleString(
                bag.prefix, bag.expression!!, node, false)
        /* 获得该结点的边 */
        val edge = enga.begin!!.outEdges[0]
        edge.data.storage = node.iStorage
        edge.data.handler = node.errorHandler
        edge.data.action = node.actionHandler
        store(enga)
    }

    /**
     * 新建ENGA
     *
     * @param node 结点
     * @return ENGA边
     */
    private fun createENGA(node: ISyntaxComponent): ENGA {
        val enga = ENGA()
        enga.begin = NGAStatus()
        enga.end = NGAStatus()
        enga.begin!!.data.label = Syntax.getSingleString(
                bag.prefix, bag.expression!!, node, true)
        enga.end!!.data.label = Syntax.getSingleString(
                bag.prefix, bag.expression!!, node, false)
        return enga
    }

    /**
     * 非确定性文法自动机描述
     *
     * @param status NGA状态
     * @param prefix 前缀
     * @return 描述
     */
    fun getNGAString(status: NGAStatus, prefix: String): String {
        val alg = NGAToString(prefix)
        status.visit(alg)
        return alg.toString()
    }

    override fun toString(): String {
        return ngaString
    }

    companion object {

        /**
         * 获取NGA状态闭包
         *
         * @param bfs    遍历算法
         * @param status 初态
         * @return 初态闭包
         */
        fun getNGAStatusClosure(bfs: BreadthFirstSearch<NGAEdge, NGAStatus>, status: NGAStatus): MutableList<NGAStatus> {
            status.visit(bfs)
            return bfs.arrStatus
        }
    }
}
