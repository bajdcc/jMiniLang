package com.bajdcc.util.lexer.automata.dfa

import com.bajdcc.util.lexer.automata.BreadthFirstSearch
import com.bajdcc.util.lexer.automata.EdgeType
import com.bajdcc.util.lexer.automata.nfa.NFA
import com.bajdcc.util.lexer.automata.nfa.NFAEdge
import com.bajdcc.util.lexer.automata.nfa.NFAStatus
import com.bajdcc.util.lexer.regex.IRegexComponent

import kotlin.streams.toList

/**
 * 确定性自动机（DFA）
 *
 * @author bajdcc
 */
class DFA(exp: IRegexComponent, debug: Boolean) : NFA(exp, debug) {

    /**
     * DFA状态集合
     */
    private var dfa: DFAStatus? = null

    /**
     * 获得DFA状态转换矩阵
     *
     * @return DFA状态转换矩阵
     */
    val dfaTable: List<DFAStatus>
        get() = getDFAStatusClosure(
                BreadthFirstSearch(), dfa!!)

    /**
     * 获取DFA状态闭包
     *
     * @param bfs    遍历算法
     * @param status 初态
     * @return 初态闭包
     */
    protected fun getDFAStatusClosure(
            bfs: BreadthFirstSearch<DFAEdge, DFAStatus>, status: DFAStatus): List<DFAStatus> {
        status.visit(bfs)
        return bfs.arrStatus
    }

    /**
     * 提供DFA描述
     *
     * @return DFA描述
     */
    val dfaString: String
        get() {
            val nfaStatusList = getNFAStatusClosure(
                    BreadthFirstSearch(), nfa!!.begin!!)
            val dfaStatusList = getDFAStatusClosure(
                    BreadthFirstSearch(), dfa!!)
            val sb = StringBuilder()
            for (i in dfaStatusList.indices) {
                val status = dfaStatusList[i]
                sb.append("状态[").append(i).append("]").append(if (status.data.bFinal) "[结束]" else "").append(" => ").append(status.data.getStatusString(nfaStatusList)).append(System.lineSeparator())
                for (edge in status.outEdges) {
                    sb.append("\t边 => [").append(dfaStatusList.indexOf(edge.end)).append("]").append(System.lineSeparator())
                    sb.append("\t\t类型 => ").append(edge.data.type.desc)
                    when (edge.data.type) {
                        EdgeType.CHARSET -> sb.append("\t").append(characterMap.ranges[edge.data.param])
                        EdgeType.EPSILON -> {}
                    }
                    sb.append(System.lineSeparator())
                }
            }
            return sb.toString()
        }

    /**
     * 获取状态转移矩阵描述
     *
     * @return 状态转移矩阵描述
     */
    val dfaTableString: String
        get() {
            val transition = buildTransition(ArrayList())
            val sb = StringBuilder()
            for (aTransition in transition) {
                for (anATransition in aTransition) {
                    sb.append("\t").append(anATransition)
                }
                sb.append(System.lineSeparator())
            }
            return sb.toString()
        }

    init {
        transfer()
    }

    /**
     * 转换
     */
    private fun transfer() {
        deleteEpsilonEdges()
        if (bDebug) {
            println("#### 消除Epsilon边 ####")
            println(nfaString)
        }
        determine()
        if (bDebug) {
            println("#### 确定化 ####")
            println(dfaString)
            println("#### 状态转移矩阵 ####")
            println(dfaTableString)
        }
        minimization()
        if (bDebug) {
            println("#### 最小化 ####")
            println(dfaString)
            println("#### 状态转移矩阵 ####")
            println(dfaTableString)
        }
    }

    /**
     * 连接两个状态
     *
     * @param begin 初态
     * @param end   终态
     * @return 新的边
     */
    protected fun connect(begin: DFAStatus?, end: DFAStatus): DFAEdge {
        val edge = DFAEdge()// 申请一条新边
        edge.begin = begin
        edge.end = end
        begin!!.outEdges.add(edge)// 添加进起始边的出边
        end.inEdges.add(edge)// 添加进结束边的入边
        return edge
    }

    /**
     * 断开某个状态和某条边
     *
     * @param edge   某条边
     */
    protected fun disconnect(edge: DFAEdge) {
        edge.begin!!.outEdges.remove(edge)
        edge.end!!.inEdges.remove(edge)// 当前边的结束状态的入边集合去除当前边
    }

    /**
     * 断开某个状态和所有边
     *
     * @param status 某状态
     */
    protected fun disconnect(status: DFAStatus) {
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
     * 去除Epsilon边
     */
    private fun deleteEpsilonEdges() {
        val nfaStatusList = getNFAStatusClosure(
                BreadthFirstSearch(), nfa!!.begin!!)// 获取状态闭包
        val unaccessiableList = ArrayList<NFAStatus>()// 不可到达状态集合
        for (status in nfaStatusList) {
            var epsilon = true
            for ((_, _, data) in status.inEdges) {
                if (data.type !== EdgeType.EPSILON) {// 不是Epsilon边
                    epsilon = false// 当前可到达
                    break
                }
            }
            if (epsilon) {
                unaccessiableList.add(status)// 如果所有入边为Epsilon边，则不可到达
            }
        }
        unaccessiableList.remove(nfa!!.begin)// 初态设为有效
        val epsilonBFS = object : BreadthFirstSearch<NFAEdge, NFAStatus>() {
            override fun testEdge(edge: NFAEdge): Boolean {
                return edge.data.type === EdgeType.EPSILON
            }
        }
        /* 遍历所有有效状态 */
        // 若为有效状态
        /* 获取当前状态的Epsilon闭包 *//* 去除自身状态 *//* 遍历Epsilon闭包的状态 *//* 如果闭包中有终态，则当前状态为终态 *//* 遍历闭包中所有边 *//* 如果当前边不是Epsilon边，就将闭包中的有效边添加到当前状态 *//* 如果当前边不是Epsilon边，就将闭包中的有效边添加到当前状态 */
        nfaStatusList.stream().filter { status -> !unaccessiableList.contains(status) }.forEach {// 若为有效状态
            status ->
            /* 获取当前状态的Epsilon闭包 */
            val epsilonClosure = getNFAStatusClosure(
                    epsilonBFS, status)
            /* 去除自身状态 */
            epsilonClosure.remove(status)
            /* 遍历Epsilon闭包的状态 */
            for (epsilonStatus in epsilonClosure) {
                if (epsilonStatus.data.bFinal) {
                    /* 如果闭包中有终态，则当前状态为终态 */
                    status.data.bFinal = true
                }
                /* 遍历闭包中所有边 */
                /* 如果当前边不是Epsilon边，就将闭包中的有效边添加到当前状态 */
                epsilonStatus.outEdges.stream().filter { (_, _, data) -> data.type !== EdgeType.EPSILON }.forEach { (_, end, data) ->
                    /* 如果当前边不是Epsilon边，就将闭包中的有效边添加到当前状态 */
                    connect(status, end!!).data = data
                }
            }
        }
        /* 删除Epsilon边 */
        for (status in nfaStatusList) {
            val it = status.outEdges.iterator()
            while (it.hasNext()) {
                val edge = it.next()
                if (edge.data.type === EdgeType.EPSILON) {
                    it.remove()
                    disconnect(edge)// 删除Epsilon边
                }
            }
        }
        /* 删除无效状态 */
        for (status in unaccessiableList) {
            nfaStatusList.remove(status)// 删除无效状态
            disconnect(status)// 删除与状态有关的所有边
        }
        unaccessiableList.clear()
        /* 删除重复边 */
        for (status in nfaStatusList) {
            var i = 0
            while (i < status.outEdges.size) {
                val (_, end, data) = status.outEdges[i]
                val it2 = status.outEdges.listIterator(i + 1)
                while (it2.hasNext()) {
                    val edge2 = it2.next()
                    if (end == edge2.end
                            && data.type === edge2.data.type
                            && data.param == edge2.data.param) {
                        it2.remove()
                        disconnect(edge2)
                    }
                }
                i++
            }
        }
    }

    /**
     * NFA确定化，转为DFA
     */
    private fun determine() {
        /* 取得NFA所有状态 */
        val nfaStatusList = getNFAStatusClosure(
                BreadthFirstSearch(), nfa!!.begin!!)
        val dfaStatusList = ArrayList<DFAStatus>()
        /* 哈希表用来进行DFA状态表的查找 */
        val dfaStatusListMap = HashMap<String, Int>()
        val initStatus = DFAStatus()
        initStatus.data.bFinal = nfa!!.begin!!.data.bFinal// 是否终态
        initStatus.data.nfaStatus.add(nfa!!.begin!!)// DFA[0]=NFA初态集合
        dfaStatusList.add(initStatus)
        dfaStatusListMap[initStatus.data.getStatusString(nfaStatusList)] = 0
        /* 构造DFA表 */
        var i = 0
        while (i < dfaStatusList.size) {
            val dfaStatus = dfaStatusList[i]
            val bags = ArrayList<DFAEdgeBag>()
            /* 遍历当前NFA状态集合的所有边 */
            for (nfaStatus in dfaStatus.data.nfaStatus) {
                for (nfaEdge in nfaStatus.outEdges) {
                    var dfaBag: DFAEdgeBag? = null
                    for (bag in bags) {
                        /* 检查是否在表中 */
                        if (nfaEdge.data.type === bag.type && nfaEdge.data.param == bag.param) {
                            dfaBag = bag
                            break
                        }
                    }
                    /* 若不存在，则新建 */
                    if (dfaBag == null) {
                        dfaBag = DFAEdgeBag()
                        dfaBag.type = nfaEdge.data.type
                        dfaBag.param = nfaEdge.data.param
                        bags.add(dfaBag)
                    }
                    /* 添加当前边 */
                    dfaBag.nfaEdges.add(nfaEdge)
                    /* 添加当前状态 */
                    dfaBag.nfaStatus.add(nfaEdge.end!!)
                }
            }
            /* 遍历当前的所有DFA边 */
            for (bag in bags) {
                /* 检测DFA指向的状态是否存在 */
                val status: DFAStatus
                /* 哈希字符串 */
                val hash = bag.getStatusString(nfaStatusList)
                if (dfaStatusListMap.containsKey(bag
                                .getStatusString(nfaStatusList))) {
                    status = dfaStatusList.get(dfaStatusListMap[hash]!!)
                } else {// 不存在DFA
                    status = DFAStatus()
                    status.data.nfaStatus = ArrayList(
                            bag.nfaStatus)
                    /* 检查终态 */
                    for (nfaStatus in status.data.nfaStatus) {
                        if (nfaStatus.data.bFinal) {
                            status.data.bFinal = true
                            break
                        }
                    }
                    dfaStatusList.add(status)
                    dfaStatusListMap[hash] = dfaStatusList.size - 1
                }
                /* 创建DFA边 */
                val edge = connect(dfaStatus, status)
                edge.data.type = bag.type
                edge.data.param = bag.param
                edge.data.nfaEdges = bag.nfaEdges
            }
            i++
        }
        dfa = dfaStatusList[0]
    }

    /**
     * DFA最小化
     */
    private fun minimization() {
        /* 是否存在等价类的flag */
        var bExistequivalentClass = true
        while (bExistequivalentClass) {
            /* 终态集合 */
            val finalStatus = mutableListOf<Int>()
            /* 非终态集合 */
            val nonFinalStatus = mutableListOf<Int>()
            /* DFA状态转移表，填充终态集合 */
            val transition = buildTransition(finalStatus)
            /* 填充非终态集合和状态集合的哈希表 */
            for (i in transition.indices) {
                if (!finalStatus.contains(i)) {
                    nonFinalStatus.add(i)// 添加非终态序号
                }
            }
            /* DFA状态表 */
            val statusList = dfaTable
            /* 处理终态 */
            bExistequivalentClass = mergeStatus(
                    partition(finalStatus, transition), statusList)
            /* 处理非终态 */
            bExistequivalentClass = bExistequivalentClass or mergeStatus(
                    partition(nonFinalStatus, transition), statusList)
        }
    }

    /**
     * 最小化划分
     *
     * @param statusList 初始划分
     * @param transition 状态转移表
     * @return 划分
     */
    private fun partition(statusList: MutableList<Int>, transition: Array<IntArray>): MutableList<MutableList<Int>> {
        if (statusList.size == 1) {
            /* 存放结果 */
            val pat = mutableListOf<MutableList<Int>>()
            pat.add(ArrayList(statusList[0]))
            return pat
        } else {
            /* 用于查找相同状态 */
            val map = mutableMapOf<String, MutableList<Int>>()
            for (status in statusList) {
                /* 获得状态hash */
                val hash = getStatusLineString(transition[status])
                /* 状态是否出现过 */
                if (map.containsKey(hash)) {
                    /* 状态重复，加入上个相同状态的集合 */
                    map[hash]!!.add(status)
                } else {
                    /* 前次出现，创建数组保存它 */
                    val set = ArrayList<Int>()
                    set.add(status)
                    map[hash] = set
                }
            }
            return ArrayList(map.values)
        }
    }

    /**
     * 合并相同状态
     *
     * @param pat        状态划分
     * @param statusList 状态转移表
     */
    private fun mergeStatus(pat: MutableList<MutableList<Int>>, statusList: List<DFAStatus>): Boolean {
        /* 保存要处理的多状态合并的划分 */
        val dealWith = pat.stream().filter { collection -> collection.size > 1 }.toList()
        // 有多个状态
        /* 是否已经没有等价类，若没有，就返回false，这样算法就结束（收敛 ） */
        if (dealWith.isEmpty()) {
            return false
        }
        /* 合并每一分组 */
        for (collection in dealWith) {
            /* 目标状态为集合中第一个状态，其余状态被合并 */
            val dstStatus = collection[0]
            /* 目标状态 */
            val status = statusList[dstStatus]
            for (i in 1 until collection.size) {
                /* 重复的状态 */
                val srcStatus = collection[i]
                val dupStatus = statusList[srcStatus]
                /* 备份重复状态的入边 */
                val edges = ArrayList(
                        dupStatus.inEdges)
                /* 将指向重复状态的边改为指向目标状态的边 */
                for (edge in edges) {
                    /* 复制边 */
                    connect(edge.begin, status).data = edge.data
                }
                /* 去除重复状态 */
                disconnect(dupStatus)
            }
        }
        return true
    }

    /**
     * 获取DFA状态转移表某行的字符串
     *
     * @param line 某行的索引矩阵
     * @return 哈希字符串
     */
    private fun getStatusLineString(line: IntArray): String {
        val sb = StringBuilder()
        for (i in line) {
            sb.append(i).append(",")
        }
        return sb.toString()
    }

    /**
     * 建立状态
     *
     * @param finalStatus 终态
     * @return 状态转换矩阵
     */
    fun buildTransition(finalStatus: MutableCollection<Int>): Array<IntArray> {
        finalStatus.clear()
        /* DFA状态表 */
        val statusList = dfaTable
        /* 建立状态转移矩阵 */
        val transition = Array(statusList.size) {
            IntArray(characterMap.ranges.size)
        }
        /* 填充状态转移表 */
        for (i in statusList.indices) {
            val status = statusList[i]
            if (status.data.bFinal) {
                finalStatus.add(i)// 标记终态
            }
            for (j in 0 until transition[i].size) {
                transition[i][j] = -1// 置无效标记-1
            }
            for (edge in status.outEdges) {
                if (edge.data.type === EdgeType.CHARSET) {
                    transition[i][edge.data.param] = statusList
                            .indexOf(edge.end)
                }
            }
        }
        return transition
    }
}