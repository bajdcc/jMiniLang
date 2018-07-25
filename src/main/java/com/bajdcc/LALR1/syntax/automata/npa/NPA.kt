package com.bajdcc.LALR1.syntax.automata.npa

import com.bajdcc.LALR1.grammar.semantic.ISemanticAction
import com.bajdcc.LALR1.syntax.automata.nga.NGA
import com.bajdcc.LALR1.syntax.automata.nga.NGAEdge
import com.bajdcc.LALR1.syntax.automata.nga.NGAEdgeType
import com.bajdcc.LALR1.syntax.automata.nga.NGAStatus
import com.bajdcc.LALR1.syntax.exp.RuleExp
import com.bajdcc.LALR1.syntax.exp.TokenExp
import com.bajdcc.LALR1.syntax.rule.Rule
import com.bajdcc.LALR1.syntax.rule.RuleItem
import com.bajdcc.util.lexer.automata.BreadthFirstSearch

/**
 * 非确定性下推自动机（NPA）构成算法
 * [nonterminals] 非终结符
 * [terminals] 终结符
 * [initRule] 起始规则
 * [arrActions] 语义动作集合
 * @author bajdcc
 */
open class NPA(nonterminals: List<RuleExp>,
          terminals: List<TokenExp>,
          private val initRule: Rule,
          private val arrActions: List<ISemanticAction>) : NGA(nonterminals, terminals) {

    /**
     * 规则集合
     */
    private val arrRuleItems = mutableListOf<RuleItem>()

    /**
     * 起始状态集合
     */
    private val arrInitStatusList = mutableListOf<NPAStatus>()

    /**
     * 获得NPA初态
     *
     * @return NPA初态表
     */
    val initStatusList: List<NPAStatus>
        get() = arrInitStatusList

    /**
     * 获得NPA所有状态
     *
     * @return NPA状态表
     */
    val npaStatusList: List<NPAStatus>
        get() {
            val npaStatusList = mutableListOf<NPAStatus>()
            for (status in arrInitStatusList) {
                npaStatusList.addAll(getNGAStatusClosure(
                        BreadthFirstSearch(), status))
            }
            return npaStatusList
        }

    /**
     * 获得所有推导式
     *
     * @return NPA状态表
     */
    val ruleItems: List<RuleItem>
        get() = arrRuleItems

    /**
     * 获得NPA描述
     *
     * @return NPA描述
     */
    val npaString: String
        get() {
            val sb = StringBuilder()
            val statusList = npaStatusList
            sb.append("#### 初始状态 ####")
            sb.append(System.lineSeparator())
            for (i in arrInitStatusList.indices) {
                sb.append("状态[").append(i).append("]： ").append(arrInitStatusList[i].data.label)
                sb.append(System.lineSeparator())
            }
            sb.append(System.lineSeparator())
            sb.append("#### 状态转换图 ####")
            sb.append(System.lineSeparator())
            for (i in statusList.indices) {
                val status = statusList[i]
                sb.append("状态[").append(i).append("]： ")
                sb.append(System.lineSeparator())
                sb.append("\t项目：").append(status.data.label)
                sb.append(System.lineSeparator())
                sb.append("\t规则：").append(arrRuleItems[status.data.rule].parent.nonTerminal.name)
                sb.append(System.lineSeparator())
                for (edge in status.outEdges) {
                    sb.append("\t\t----------------")
                    sb.append(System.lineSeparator())
                    sb.append("\t\t到达状态[").append(statusList.indexOf(edge.end)).append("]: ").append(edge.end!!.data.label)
                    sb.append(System.lineSeparator())
                    sb.append("\t\t类型：").append(edge.data.type.desc)
                    when (edge.data.type) {
                        NPAEdgeType.FINISH -> {
                        }
                        NPAEdgeType.LEFT_RECURSION -> {
                        }
                        NPAEdgeType.MOVE -> sb.append("\t=> ").append(edge.data.token).append("(").append(arrTerminals[edge.data.token]).append(")")
                        NPAEdgeType.REDUCE -> sb.append("\t=> ").append(edge.data.status!!.data.label)
                        NPAEdgeType.SHIFT -> {
                        }
                    }
                    sb.append(System.lineSeparator())
                    sb.append("\t\t指令：").append(edge.data.inst.desc)
                    when (edge.data.inst) {
                        NPAInstruction.PASS -> {
                        }
                        NPAInstruction.READ -> sb.append("\t=> ").append(edge.data.index)
                        NPAInstruction.SHIFT -> {
                        }
                        NPAInstruction.LEFT_RECURSION, NPAInstruction.TRANSLATE -> sb.append("\t=> ").append(arrRuleItems[edge.data.handler].parent.nonTerminal.name).append(" ").append(edge.data.index)
                        NPAInstruction.LEFT_RECURSION_DISCARD, NPAInstruction.TRANSLATE_DISCARD, NPAInstruction.TRANSLATE_FINISH -> sb.append("\t=> ").append(arrRuleItems[edge.data.handler].parent.nonTerminal.name)
                    }
                    if (edge.data.action != null)
                        sb.append("  ").append("[Action]")
                    sb.append(System.lineSeparator())
                    if (edge.data.lookaheads.isNotEmpty()) {
                        sb.append("\t\t预查：")
                        for (id in edge.data.lookaheads) {
                            sb.append("[").append(arrTerminals[id]).append("]")
                        }
                        sb.append(System.lineSeparator())
                    }
                }
                sb.append(System.lineSeparator())
            }
            return sb.toString()
        }

    /**
     * 获得NPA描述（MARKDOWN）
     */
    val npaMarkdownString: String
        get() {
            val sb = StringBuilder()
            val statusList = npaStatusList
            sb.append("# 初始状态 #")
            sb.append(System.lineSeparator())
            for (i in arrInitStatusList.indices) {
                sb.append("状态[").append(i).append("]： ").append(arrInitStatusList[i].data.label)
                sb.append(System.lineSeparator())
            }
            sb.append(System.lineSeparator())
            sb.append("# 状态转换图 #")
            sb.append(System.lineSeparator())
            for (i in statusList.indices) {
                val status = statusList[i]
                sb.append("## 状态[").append(i).append("]： ")
                sb.append(System.lineSeparator()).append(System.lineSeparator())
                sb.append("项目：").append(status.data.label)
                sb.append(System.lineSeparator()).append(System.lineSeparator())
                sb.append("规则：").append(arrRuleItems[status.data.rule].parent.nonTerminal.name)
                sb.append(System.lineSeparator()).append(System.lineSeparator())
                for (edge in status.outEdges.sortedBy { statusList.indexOf(it.end) }) {
                    sb.append("----")
                    sb.append(System.lineSeparator()).append(System.lineSeparator())
                    sb.append("&emsp;&emsp;&emsp;&emsp;到达状态[").append(statusList.indexOf(edge.end)).append("]: ").append(edge.end!!.data.label)
                    sb.append(System.lineSeparator()).append(System.lineSeparator())
                    sb.append("&emsp;&emsp;&emsp;&emsp;类型：").append(edge.data.type.desc)
                    when (edge.data.type) {
                        NPAEdgeType.FINISH -> {
                        }
                        NPAEdgeType.LEFT_RECURSION -> {
                        }
                        NPAEdgeType.MOVE -> sb.append("\t=> ").append(edge.data.token).append("(").append(arrTerminals[edge.data.token]).append(")")
                        NPAEdgeType.REDUCE -> sb.append("\t=> ").append(edge.data.status!!.data.label)
                        NPAEdgeType.SHIFT -> {
                        }
                    }
                    sb.append(System.lineSeparator()).append(System.lineSeparator())
                    sb.append("&emsp;&emsp;&emsp;&emsp;指令：").append(edge.data.inst.desc)
                    when (edge.data.inst) {
                        NPAInstruction.PASS -> {
                        }
                        NPAInstruction.READ -> sb.append("\t=> ").append(edge.data.index)
                        NPAInstruction.SHIFT -> {
                        }
                        NPAInstruction.LEFT_RECURSION, NPAInstruction.TRANSLATE -> sb.append("\t=> ").append(arrRuleItems[edge.data.handler].parent.nonTerminal.name).append(" ").append(edge.data.index)
                        NPAInstruction.LEFT_RECURSION_DISCARD, NPAInstruction.TRANSLATE_DISCARD, NPAInstruction.TRANSLATE_FINISH -> sb.append("\t=> ").append(arrRuleItems[edge.data.handler].parent.nonTerminal.name)
                    }
                    if (edge.data.action != null)
                        sb.append("  ").append("[Action]")
                    sb.append(System.lineSeparator()).append(System.lineSeparator())
                    if (edge.data.lookaheads.isNotEmpty()) {
                        sb.append("&emsp;&emsp;&emsp;&emsp;预查：")
                        for (id in edge.data.lookaheads.sorted()) {
                            sb.append("[").append(arrTerminals[id]).append("]&emsp;")
                        }
                        sb.append(System.lineSeparator()).append(System.lineSeparator())
                    }
                }
                sb.append(System.lineSeparator()).append(System.lineSeparator())
            }
            return sb.toString()
        }

    init {
        generateNPA()
    }

    /**
     * 连接两个状态
     *
     * @param begin 初态
     * @param end   终态
     * @return 新的边
     */
    private fun connect(begin: NPAStatus, end: NPAStatus): NPAEdge {
        val edge = NPAEdge()// 申请一条新边
        edge.begin = begin
        edge.end = end
        begin.outEdges.add(edge)// 添加进起始边的出边
        end.inEdges.add(edge)// 添加进结束边的入边
        return edge
    }

    /**
     * 断开某个状态和某条边
     *
     * @param status 某状态
     * @param edge   某条边
     */
    private fun disconnect(edge: NPAEdge) {
        edge.end!!.inEdges.remove(edge)// 当前边的结束状态的入边集合去除当前边
    }

    /**
     * 断开某个状态和所有边
     *
     * @param status 某状态
     */
    private fun disconnect(status: NPAStatus) {
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
     * 产生下推自动机
     */
    private fun generateNPA() {
        /* 下推自动机状态 */
        val npaStatusList = mutableListOf<NPAStatus>()
        /* 文法自动机状态 */
        val ngaStatusList = mutableListOf<NGAStatus>()
        /* 下推自动机边（规则映射到NGA边） */
        val ruleEdgeMap = mutableMapOf<Rule, MutableList<NGAEdge>>()
        /* 遍历每条规则 */
        for ((key, value) in mapNGA) {
            /* 保存规则 */
            arrRuleItems.add(key)
            /* 搜索当前规则中的所有状态 */
            val currentNGAStatusList = NGA.getNGAStatusClosure(BreadthFirstSearch(), value)
            /* 搜索所有的边 */
            for (status in currentNGAStatusList) {
                /* 若边为非终结符边，则加入邻接表，终结符->带终结符的所有边 */
                status.outEdges.filter { it.data.type === NGAEdgeType.RULE }.forEach { edge ->
                    val rule = edge.data.rule!!.rule
                    if (!ruleEdgeMap.containsKey(rule)) {
                        ruleEdgeMap[rule] = mutableListOf()
                    }
                    ruleEdgeMap[rule]!!.add(edge)
                }
            }
            /* 为所有的NGA状态构造对应的NPA状态，为一一对应 */
            for (status in currentNGAStatusList) {
                /* 保存NGA状态 */
                ngaStatusList.add(status)
                /* 新建NPA状态 */
                val npaStatus = NPAStatus()
                npaStatus.data.label = status.data.label
                npaStatus.data.rule = arrRuleItems.indexOf(key)
                npaStatusList.add(npaStatus)
            }
        }
        /* 遍历所有NPA状态 */
        for (i in npaStatusList.indices) {
            /* 获得NGA状态 */
            val ngaStatus = ngaStatusList[i]
            /* 获得NPA状态 */
            val npaStatus = npaStatusList[i]
            /* 获得规则 */
            val ruleItem = arrRuleItems[npaStatus.data.rule]
            /* 检查是否为纯左递归，类似[A::=Aa]此类，无法直接添加纯左递归边，需要LA及归约 */
            if (isNoLeftRecursiveStatus(ngaStatus, ruleItem.parent)) {
                /* 当前状态是否为初始状态且推导规则是否属于起始规则（无NGA入边） */
                val isInitRuleStatus = initRule == ruleItem.parent
                /* 若是，则将当前状态对应的NPA状态加入初始状态表中 */
                if (ngaStatus.inEdges.isEmpty() && isInitRuleStatus) {
                    arrInitStatusList.add(npaStatus)
                }
                /* 建立计算优先级使用的记号表，其中元素为从当前状态出发的Rule或Token边的First集（LA预查优先） */
                val tokenSet = mutableSetOf<Int>()
                /* 遍历文法自动机的所有边 */
                for (edge in ngaStatus.outEdges) when (edge.data.type) {
                    NGAEdgeType.EPSILON -> {
                    }
                    NGAEdgeType.RULE ->
                        /* 判断边是否为纯左递归 */
                        if (!isLeftRecursiveEdge(edge, ruleItem.parent)) {
                            for (item in edge.data.rule!!.rule.arrRules) {
                                /* 起始状态 */
                                val initItemStatus = mapNGA[item]!!
                                /* 判断状态是否为纯左递归 */
                                if (isNoLeftRecursiveStatus(initItemStatus, item.parent)) {
                                    /* 添加Shift边，功能为将一条状态序号放入堆栈顶 */
                                    val npaEdge = connect(npaStatus, npaStatusList[ngaStatusList.indexOf(initItemStatus)])
                                    npaEdge.data.error = edge.data.handler
                                    npaEdge.data.action = edge.data.action
                                    npaEdge.data.type = NPAEdgeType.SHIFT
                                    npaEdge.data.inst = NPAInstruction.SHIFT
                                    npaEdge.data.errorJump = npaStatusList[ngaStatusList.indexOf(edge.end)]
                                    /* 为移进项目构造LookAhead表，LA不吃字符，只是单纯压入新的状态（用于规约） */
                                    npaEdge.data.lookaheads = mutableSetOf()
                                    npaEdge.data.lookaheads.addAll(
                                            item.setFirstSetTokens
                                                    .filter { !tokenSet.contains(it.id) }
                                                    .map { it.id })
                                }
                            }
                            // 将当前非终结符的所有终结符First集加入tokenSet，以便非终结符的Move的LA操作（优先级）
                            tokenSet.addAll(edge.data.rule!!.rule.arrTokens.map { it.id })
                        }
                    NGAEdgeType.TOKEN -> {
                        /* 添加Move边，功能为吃掉（匹配）一个终结符，若终结符不匹配，则报错（即不符合文法） */
                        val npaEdge = connect(npaStatus, npaStatusList[ngaStatusList .indexOf(edge.end)])
                        npaEdge.data.error = edge.data.handler
                        npaEdge.data.action = edge.data.action
                        npaEdge.data.type = NPAEdgeType.MOVE
                        npaEdge.data.token = edge.data.token!!.id
                        npaEdge.data.handler = arrActions .indexOf(edge.data.action)
                        npaEdge.data.errorJump = npaEdge.end
                        /* 根据StorageID配置指令 */
                        if (edge.data.storage != -1) {
                            npaEdge.data.inst = NPAInstruction.READ
                            npaEdge.data.index = edge.data.storage// 参数
                        } else {
                            npaEdge.data.inst = NPAInstruction.PASS
                        }
                        /* 修改TokenSet */
                        if (tokenSet.contains(edge.data.token!!.id)) {
                            /* 使用LookAhead表 */
                            npaEdge.data.lookaheads = mutableSetOf()
                        } else {
                            tokenSet.add(edge.data.token!!.id)
                        }
                    }
                }
                /* 如果当前NGA状态是结束状态（此时要进行规约），则检查是否需要添加其他边 */
                if (ngaStatus.data.final) {
                    if (ruleEdgeMap.containsKey(ruleItem.parent)) {
                        /* 遍历文法自动机中附带了当前推导规则所属规则的边 */
                        val ruleEdges = ruleEdgeMap[ruleItem.parent]!!// 当前规约的文法的非终结符为A，获得包含A的所有边
                        for (ngaEdge in ruleEdges) {
                            /* 判断纯左递归，冗长的表达式是为了获得当前边的所在推导式的起始非终结符 */
                            if (isLeftRecursiveEdge(ngaEdge,
                                            arrRuleItems[npaStatusList[
                                                    ngaStatusList.indexOf(ngaEdge.begin)].data.rule].parent)) {
                                /* 添加Left Recursion边（特殊的Reduce边） */
                                val npaEdge = connect(npaStatus, npaStatusList[ngaStatusList.indexOf(ngaEdge.end)])
                                npaEdge.data.type = NPAEdgeType.LEFT_RECURSION
                                if (ngaEdge.data.storage != -1) {
                                    npaEdge.data.inst = NPAInstruction.LEFT_RECURSION
                                    npaEdge.data.index = ngaEdge.data.storage
                                } else {
                                    npaEdge.data.inst = NPAInstruction.LEFT_RECURSION_DISCARD
                                }
                                npaEdge.data.handler = npaStatus.data.rule// 规约的规则
                                /* 为左递归构造Lookahead表（Follow集），若LA成功则进入左递归 */
                                npaEdge.data.lookaheads = mutableSetOf()
                                for (edge in ngaEdge.end!!.outEdges) {
                                    /* 若出边为终结符，则直接加入（终结符First集仍是本身） */
                                    if (edge.data.type === NGAEdgeType.TOKEN) {
                                        npaEdge.data.lookaheads.add(edge.data.token!!.id)
                                    } else {
                                        /* 若出边为非终结符，则加入非终结符的First集 */
                                        npaEdge.data.lookaheads.addAll(edge.data.rule!!.rule.arrTokens.map { it.id })
                                    }
                                }
                            } else {
                                /* 添加Reduce边 */
                                val npaEdge = connect(npaStatus, npaStatusList[ngaStatusList.indexOf(ngaEdge.end)])
                                npaEdge.data.type = NPAEdgeType.REDUCE
                                npaEdge.data.status = npaStatusList[ngaStatusList.indexOf(ngaEdge.begin)]
                                if (ngaEdge.data.storage != -1) {
                                    npaEdge.data.inst = NPAInstruction.TRANSLATE
                                    npaEdge.data.index = ngaEdge.data.storage
                                } else {
                                    npaEdge.data.inst = NPAInstruction.TRANSLATE_DISCARD
                                }
                                npaEdge.data.handler = npaStatus.data.rule// 规约的规则
                            }
                        }
                    }
                    if (isInitRuleStatus) {
                        /* 添加Finish边 */
                        val npaEdge = connect(npaStatus, npaStatus)
                        npaEdge.data.type = NPAEdgeType.FINISH
                        npaEdge.data.inst = NPAInstruction.TRANSLATE_FINISH
                        npaEdge.data.handler = npaStatus.data.rule
                    }
                }
            }
        }
    }

    override fun toString(): String {
        return npaString
    }

    companion object {

        /**
         * 判断状态是否不是纯左递归
         *
         * @param status NGA状态
         * @param rule   规则
         * @return 状态是否不是左递归
         */
        private fun isNoLeftRecursiveStatus(status: NGAStatus, rule: Rule): Boolean {
            // 有入边，则无纯左递归
            if (!status.inEdges.isEmpty())
                return true
            return status.outEdges.isNotEmpty() && status.outEdges.any { it.data.type !== NGAEdgeType.RULE || it.data.rule!!.rule != rule }
        }

        /**
         * 判断边是否为纯左递归
         *
         * @param edge NGA边
         * @param rule 规则
         * @return 状态是否为左递归
         */
        private fun isLeftRecursiveEdge(edge: NGAEdge, rule: Rule): Boolean {
            // 没有入边
            return edge.begin!!.inEdges.isEmpty() && edge.data.type === NGAEdgeType.RULE && edge.data.rule!!.rule == rule
        }

        /**
         * 获取NPA状态闭包
         *
         * @param bfs    遍历算法
         * @param status 初态
         * @return 初态闭包
         */
        protected fun getNGAStatusClosure(
                bfs: BreadthFirstSearch<NPAEdge, NPAStatus>, status: NPAStatus): List<NPAStatus> {
            status.visit(bfs)
            return bfs.arrStatus
        }
    }
}
