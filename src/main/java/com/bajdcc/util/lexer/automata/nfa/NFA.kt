package com.bajdcc.util.lexer.automata.nfa

import com.bajdcc.util.lexer.automata.BreadthFirstSearch
import com.bajdcc.util.lexer.automata.EdgeType
import com.bajdcc.util.lexer.regex.*
import java.util.*

/**
 * NFA构成算法（AST-&gt;NFA）
 * [expression] 表达式树根结点
 * [bDebug] 是否为调试模式（打印信息）
 * @author bajdcc
 */
open class NFA(protected var expression: IRegexComponent,
               protected var bDebug: Boolean) : IRegexComponentVisitor {

    /**
     * 深度
     */
    private var level = 0

    /**
     * NFA栈
     */
    private val stkNFA = Stack<MutableList<ENFA>>()

    /**
     * NFA子表
     */
    private var childNFA: MutableList<ENFA>? = mutableListOf()

    /**
     * ENFA
     */
    protected var nfa: ENFA? = null

    /**
     * Sigma状态集
     */
    /**
     * 获取字符映射表
     *
     * @return 字符映射表
     */
    var characterMap = CharacterMap()
        protected set

    /**
     * 字符区间描述
     *
     * @return 字符区间描述
     */
    val statusString: String
        get() = characterMap.toString()

    /**
     * 获取NFA状态闭包
     *
     * @param bfs    遍历算法
     * @param status 初态
     * @return 初态闭包
     */
    protected fun getNFAStatusClosure(
            bfs: BreadthFirstSearch<NFAEdge, NFAStatus>, status: NFAStatus): MutableList<NFAStatus> {
        status.visit(bfs)
        return bfs.arrStatus
    }

    /**
     * NFA描述
     *
     * @return NFA描述
     */
    val nfaString: String
        get() {
            val sb = StringBuilder()
            val statusList = getNFAStatusClosure(
                    BreadthFirstSearch(), nfa!!.begin!!)
            for (i in statusList.indices) {
                val status = statusList[i]
                sb.append("状态[").append(i).append("]").append(if (status.data.bFinal) "[结束]" else "").append(System.lineSeparator())
                for (edge in status.outEdges) {
                    sb.append("\t边 => [").append(statusList.indexOf(edge.end)).append("]").append(System.lineSeparator())
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

    init {
        expression.visit(characterMap)
        if (bDebug) {
            println("#### 状态集合 ####")
            println(statusString)
        }
        expression.visit(this)
        if (bDebug) {
            println("#### EpsilonNFA ####")
            println(nfaString)
        }
    }

    /**
     * 连接两个状态
     *
     * @param begin 初态
     * @param end   终态
     * @return 新的边
     */
    protected fun connect(begin: NFAStatus, end: NFAStatus): NFAEdge {
        val edge = NFAEdge()// 申请一条新边
        edge.begin = begin
        edge.end = end
        begin.outEdges.add(edge)// 添加进起始边的出边
        end.inEdges.add(edge)// 添加进结束边的入边
        return edge
    }

    /**
     * 断开某个状态和某条边
     *
     * @param edge   某条边
     */
    protected fun disconnect(edge: NFAEdge) {
        edge.end!!.inEdges.remove(edge)// 当前边的结束状态的入边集合去除当前边
    }

    /**
     * 断开某个状态和所有边
     *
     * @param status 某状态
     */
    protected fun disconnect(status: NFAStatus) {
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

    override fun visitBegin(node: Charset) {
        enter()
        val enfa = ENFA()
        enfa.begin = NFAStatus()
        enfa.end = NFAStatus()
        // 遍历所有字符区间
        // 若在当前结点范围内，则添加边
        // 连接两个状态
        // 字符类型
        characterMap.ranges.stream().filter { range -> node.include(range.lowerBound) }.forEach {// 若在当前结点范围内，则添加边
            range ->
            val edge = connect(enfa.begin!!, enfa.end!!)// 连接两个状态
            edge.data.type = EdgeType.CHARSET// 字符类型
            edge.data.param = characterMap.find(range.lowerBound)
        }
        storeENFA(enfa)
    }

    override fun visitBegin(node: Constructure) {
        enter()
        enterChildren()
    }

    override fun visitBegin(node: Repetition) {
        enter()
        enterChildren()
    }

    override fun visitEnd(node: Charset) {
        leave()
    }

    override fun visitEnd(node: Constructure) {
        leaveChildren()
        var result: ENFA? = null
        if (!node.branch) {
            /* 将当前NFA的两端同每个子结点的两端串联 */
            for (enfa in childNFA!!) {
                if (result == null) {
                    result = childNFA!![0]
                } else {
                    connect(result.end!!, enfa.begin!!)
                    result.end = enfa.end
                }
            }
        } else {
            result = ENFA()
            result.begin = NFAStatus()
            result.end = NFAStatus()
            /* 将当前NFA的两端同每个子结点的两端并联 */
            for (enfa in childNFA!!) {
                connect(result.begin!!, enfa.begin!!)
                connect(enfa.end!!, result.end!!)
            }
        }
        storeENFA(result!!)
        leave()
    }

    override fun visitEnd(node: Repetition) {
        leaveChildren()
        // #### 注意 ####
        // 由于正则表达式的语法树结构使然，循环语句的子结点必定只有一个，
        // 为字符集、并联或串联。
        /* 构造子图副本 */
        val subENFAList = ArrayList<ENFA>()
        var enfa = ENFA()
        enfa.begin = childNFA!![0].begin
        enfa.end = childNFA!![0].end
        val count = Math.max(node.lowerBound, node.upperBound)
        subENFAList.add(enfa)
        /* 循环复制ENFA */
        for (i in 1..count) {
            subENFAList.add(copyENFA(enfa))
        }
        enfa = ENFA()
        /* 构造循环起始部分 */
        if (node.lowerBound > 0) {
            enfa.begin = childNFA!![0].begin
            enfa.end = childNFA!![0].end
            for (i in 1 until node.lowerBound) {
                connect(enfa.end!!, subENFAList[i].begin!!)// 连接首尾
                enfa.end = subENFAList[i].end
            }
        }
        if (node.upperBound != -1) {// 有限循环，构造循环结束部分
            for (i in node.lowerBound until node.upperBound) {
                if (enfa.end != null) {
                    connect(enfa.end!!, subENFAList[i].begin!!)// 连接首尾
                    enfa.end = subENFAList[i].end
                } else {
                    enfa = subENFAList[i]
                }
                connect(subENFAList[i].begin!!,
                        subENFAList[node.upperBound - 1].end!!)
            }
        } else {// 无限循环
            val tailBegin: NFAStatus
            val tailEnd: NFAStatus
            if (enfa.end == null) {// 循环最低次数为0，即未构造起始部分，故需构造
                enfa.begin = NFAStatus()
                tailBegin = enfa.begin!!
                enfa.end = NFAStatus()
                tailEnd = enfa.end!!
            } else {// 起始部分已构造完毕，故起始端无需再次构造
                tailBegin = enfa.end!!
                enfa.end = NFAStatus()
                tailEnd = enfa.end!!
            }
            /* 构造无限循环的结束部分，连接起始端与循环端的双向e边 */
            connect(tailBegin, subENFAList[node.lowerBound].begin!!)
            connect(subENFAList[node.lowerBound].end!!, tailBegin)
            connect(tailBegin, tailEnd)
        }
        /* 构造循环的头尾部分 */
        val begin = NFAStatus()
        val end = NFAStatus()
        connect(begin, enfa.begin!!)
        connect(enfa.end!!, end)
        enfa.begin = begin
        enfa.end = end
        storeENFA(enfa)
        leave()
    }

    /**
     * 返回栈顶NFA
     *
     * @return 当前栈顶NFA
     */
    private fun currentNFA(): MutableList<ENFA> {
        return stkNFA.peek()
    }

    /**
     * 存储NFA
     *
     * @param enfa ENFA
     */
    private fun storeENFA(enfa: ENFA) {
        currentNFA().add(enfa)
    }

    /**
     * 进入结点
     */
    private fun enter() {
        if (level == 0) {// 首次访问AST时
            enterChildren()
        }
        level++
    }

    /**
     * 离开结点
     */
    private fun leave() {
        level--
        if (level == 0) {// 离开整个AST时，存储结果
            leaveChildren()
            store()
        }
    }

    /**
     * 进入子结点
     */
    private fun enterChildren() {
        stkNFA.push(ArrayList())// 新建ENFA表
        childNFA = null
    }

    /**
     * 离开子结点
     */
    private fun leaveChildren() {
        childNFA = stkNFA.pop()// 获得当前结点的子结点
    }

    /**
     * 存储结果
     */
    private fun store() {
        // #### 注意 ####
        // 本程序由regex构造的AST形成的NFA有明确且唯一的初态和终态
        val enfa = childNFA!![0]// 此时位于顶层，故顶层首个ENFA为根ENFA
        enfa.end!!.data.bFinal = true// 根ENFA的初态为begin，终态为end
        nfa = enfa
    }

    /**
     * 复制ENFA
     *
     * @param enfa ENFA
     * @return 副本
     */
    private fun copyENFA(enfa: ENFA): ENFA {
        val dstStatusList = mutableListOf<NFAStatus>()// 终态表
        // 获取状态闭包
        val srcStatusList = ArrayList(getNFAStatusClosure(
                BreadthFirstSearch(), enfa.begin!!))
        /* 复制状态 */
        for (status in srcStatusList) {
            val newStatus = NFAStatus()
            newStatus.data = status.data
            dstStatusList.add(newStatus)
        }
        /* 复制边 */
        for (i in srcStatusList.indices) {
            val status = srcStatusList[i]
            for (edge in status.outEdges) {
                val newEdge = connect(dstStatusList[i],
                        dstStatusList[srcStatusList.indexOf(edge.end)])
                newEdge.data = edge.data
            }
        }
        /* 新建ENFA，连接初态和终态 */
        val result = ENFA()
        result.begin = dstStatusList[srcStatusList.indexOf(enfa.begin)]
        result.end = dstStatusList[srcStatusList.indexOf(enfa.end)]
        return result
    }
}
