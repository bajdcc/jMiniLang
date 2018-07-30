package com.bajdcc.LL1.syntax.prediction

import com.bajdcc.LL1.grammar.error.GrammarException
import com.bajdcc.LL1.grammar.error.GrammarException.GrammarError
import com.bajdcc.LL1.syntax.exp.RuleExp
import com.bajdcc.LL1.syntax.exp.TokenExp
import com.bajdcc.LL1.syntax.rule.Rule
import com.bajdcc.LL1.syntax.rule.RuleItem
import com.bajdcc.LL1.syntax.solver.SelectSetSolver
import com.bajdcc.LL1.syntax.token.PredictType
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import java.util.*

/**
 * 【LL1预测分析】预测分析表
 *
 * @author bajdcc
 */
class PredictionTable(
        protected var arrNonTerminals: MutableList<RuleExp>,
        protected var arrTerminals: MutableList<TokenExp>,
        private val initRule: Rule,
        private val epsilon: TokenExp,
        private val iter: IRegexStringIterator) : SelectSetSolver() {

    /**
     * 预测分析表
     */
    private var table: Array<IntArray>? = null

    /**
     * 当前处理的指令包索引
     */
    private var indexBag = -1

    /**
     * 当前处理的非终结符索引
     */
    private var indexVn = -1

    /**
     * 当前处理的指令包
     */
    private var instBag: MutableList<PredictionInstruction>? = null

    /**
     * 当前处理的产生式规则
     */
    private var item: RuleItem? = null

    /**
     * 指令包
     */
    private val instList = mutableListOf<MutableList<PredictionInstruction>>()

    override val isEpsilon: Boolean
        get() = item!!.epsilon

    override val follow: MutableList<TokenExp>
        get() = arrNonTerminals[indexVn].rule.arrFollows

    /**
     * 得到当前终结符ID
     *
     * @return 终结符ID
     */
    private val tokenId: Int
        get() {
            val token = iter.ex().token()
            for (exp in arrTerminals) {
                if (exp.kType === token.type && (exp.`object` == null || exp.`object` == token.obj)) {
                    return exp.id
                }
            }
            return -1
        }

    /**
     * 获得指令描述
     *
     * @return 指令描述
     */
    val instString: String
        get() {
            val sb = StringBuilder()
            sb.append("#### 指令包 ####")
            sb.append(System.lineSeparator())
            for (i in instList.indices) {
                sb.append(i).append(": ")
                val pis = instList[i]
                for (k in pis) {
                    when (k.type) {
                        PredictType.NONTERMINAL -> sb.append("[").append(arrNonTerminals[k.inst].toString()).append("]")
                        PredictType.TERMINAL -> sb.append("[").append(arrTerminals[k.inst].toString()).append("]")
                        else -> {
                        }
                    }
                }
                sb.append(System.lineSeparator())
            }
            return sb.toString()
        }

    /**
     * 获得矩阵描述
     *
     * @return 矩阵描述
     */
    val matrixString: String
        get() {
            val sb = StringBuilder()
            val Vn = arrNonTerminals.size
            val Vt = arrTerminals.size
            sb.append("#### 预测分析矩阵 ####")
            sb.append(System.lineSeparator())
            for (i in 0 until Vn) {
                for (j in 0 until Vt) {
                    if (table!![i][j] != -1) {
                        sb.append(table!![i][j])
                    } else {
                        sb.append("-")
                    }
                    sb.append("\t")
                }
                sb.append(System.lineSeparator())
            }
            return sb.toString()
        }

    /**
     * 获得详细描述
     *
     * @return 详细描述
     */
    val tableString: String
        get() {
            val sb = StringBuilder()
            val Vn = arrNonTerminals.size
            val Vt = arrTerminals.size
            sb.append("#### 预测分析表 ####")
            sb.append(System.lineSeparator())
            for (i in 0 until Vn) {
                sb.append("状态[").append(i).append("]： ")
                sb.append(System.lineSeparator())
                sb.append("\t非终结符 -> ").append(arrNonTerminals[i].name)
                sb.append(System.lineSeparator())
                for (j in 0 until Vt) {
                    val idx = table!![i][j]
                    if (idx != -1) {
                        sb.append("\t\t----------------")
                        sb.append(System.lineSeparator())
                        sb.append("\t\t接受 -> ").append(arrTerminals[j].toString())
                        sb.append(System.lineSeparator())
                        sb.append("\t\t入栈 -> ")
                        for (k in instList[table!![i][j]]) {
                            when (k.type) {
                                PredictType.NONTERMINAL -> sb.append("[").append(arrNonTerminals[k.inst].toString()).append("]")
                                PredictType.TERMINAL -> sb.append("[").append(arrTerminals[k.inst].toString()).append("]")
                                else -> {
                                }
                            }
                        }
                        sb.append(System.lineSeparator())
                    }
                }
            }
            return sb.toString()
        }

    init {
        initialize()
    }

    /**
     * 抛出异常
     *
     * @param error 错误类型
     * @throws GrammarException 语法错误
     */
    @Throws(GrammarException::class)
    private fun err(error: GrammarError) {
        throw GrammarException(error, iter.position(), iter.ex().token())
    }

    /**
     * 初始化
     */
    private fun initialize() {
        initTable()
        predict()
    }

    /**
     * 初始化预测分析表
     */
    private fun initTable() {
        val Vn = arrNonTerminals.size
        val Vt = arrTerminals.size
        table = Array(Vn) { IntArray(Vt) }
        for (i in 0 until Vn) {
            for (j in 0 until Vt) {
                table!![i][j] = -1// 置无效位
            }
        }
    }

    /**
     * 构造预测分析表
     */
    private fun predict() {
        for (exp in arrNonTerminals) {
            indexVn++
            for (item in exp.rule.arrRules) {
                this.item = item
                item.expression.visit(this)
            }
        }
    }

    override fun setCellToRuleId(token: Int) {
        if (table!![indexVn][token] == -1) {
            table!![indexVn][token] = indexBag
        } else if (table!![indexVn][token] != indexBag) {
            System.err.println(String.format("存在二义性冲突：位置（%d，%d），[%d]->[%d]",
                    indexVn, token, table!![indexVn][token], indexBag))
        }
    }

    override fun addRule() {
        indexBag++
        instBag = mutableListOf()
        instList.add(instBag!!)
    }

    override fun addInstToRule(type: PredictType, inst: Int) {
        instBag!!.add(0, PredictionInstruction(type, inst))
    }

    /**
     * 进行分析
     *
     * @throws GrammarException 语法错误
     */
    @Throws(GrammarException::class)
    fun run() {
        /* 核心堆栈 */
        val spi = Stack<PredictionInstruction>()
        /* 结束符号进栈 */
        spi.push(PredictionInstruction(PredictType.TERMINAL, epsilon.id))
        /* 起始符号进栈 */
        spi.push(PredictionInstruction(PredictType.NONTERMINAL,
                initRule.nonTerminal.id))
        /* 设置当前状态 */
        var status = initRule.nonTerminal.id
        /* 执行步骤顺序 */
        var index = 0
        while (!spi.isEmpty()) {
            index++
            println("步骤[$index]")
            println("\t----------------")
            /* 获得当前输入字符索引 */
            val idx = tokenId
            if (idx == -1) {// 没有找到，非法字符
                err(GrammarError.UNDECLARED)
            }
            println("\t输入：" + "[" + arrTerminals[idx].toString()
                    + "]")
            /* 弹出栈顶符号 */
            val top = spi.pop()
            print("\t栈顶：")
            when (top.type) {
                PredictType.NONTERMINAL -> println("["
                        + arrNonTerminals[top.inst].toString() + "]")
                PredictType.TERMINAL -> println("[" + arrTerminals[top.inst].toString()
                        + "]")
                else -> {
                }
            }
            when (top.type) {
                PredictType.NONTERMINAL -> {
                    /* 查预测分析表 */
                    val inst = table!![status][idx]
                    if (inst == -1) {
                        err(GrammarError.SYNTAX)
                    }
                    /* 产生式进栈 */
                    print("\t入栈：")
                    for (pi in instList[inst]) {
                        spi.push(pi)
                        when (pi.type) {
                            PredictType.NONTERMINAL -> print("["
                                    + arrNonTerminals[pi.inst]
                                    .toString() + "]")
                            PredictType.TERMINAL -> print("["
                                    + arrTerminals[pi.inst].toString() + "]")
                            else -> {
                            }
                        }
                    }
                    println()
                }
                PredictType.TERMINAL -> {
                    if (idx == top.inst) {// 终结符匹配，继续
                        iter.ex().saveToken()// 保存单词
                        iter.scan()
                    } else {
                        err(GrammarError.SYNTAX)
                    }
                    println("\t匹配：" + arrTerminals[idx].toString())
                }
                else -> {
                }
            }
            println("\t----------------")
            for (i in spi.indices.reversed()) {
                val pi = spi[i]
                when (pi.type) {
                    PredictType.NONTERMINAL -> println("\t" + i + ": ["
                            + arrNonTerminals[pi.inst].toString() + "]")
                    PredictType.TERMINAL -> println("\t" + i + ": ["
                            + arrTerminals[pi.inst].toString() + "]")
                    else -> {
                    }
                }
            }
            println()
            /* 更新当前状态 */
            status = -1
            for (i in spi.indices.reversed()) {
                if (spi[i].type == PredictType.NONTERMINAL) {
                    status = spi[i].inst
                    break
                }
            }
        }
        println()
    }

    override fun toString(): String {
        return tableString +
                instString +
                matrixString
    }
}
