package com.bajdcc.OP.syntax.precedence

import com.bajdcc.LL1.syntax.prediction.PredictionInstruction
import com.bajdcc.LL1.syntax.token.PredictType
import com.bajdcc.OP.grammar.error.GrammarException
import com.bajdcc.OP.grammar.error.GrammarException.GrammarError
import com.bajdcc.OP.grammar.handler.IPatternHandler
import com.bajdcc.OP.syntax.exp.RuleExp
import com.bajdcc.OP.syntax.exp.TokenExp
import com.bajdcc.OP.syntax.handler.SyntaxException
import com.bajdcc.OP.syntax.handler.SyntaxException.SyntaxError
import com.bajdcc.OP.syntax.solver.FirstVTSolver
import com.bajdcc.OP.syntax.solver.LastVTSolver
import com.bajdcc.OP.syntax.solver.OPTableSolver
import com.bajdcc.OP.syntax.token.PrecedenceType
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

import java.util.*

/**
 * 【算法优先分析】算法优先关系表
 *
 * @author bajdcc
 */
class PrecedenceTable(
        protected var arrNonTerminals: MutableList<RuleExp>,
        protected var arrTerminals: MutableList<TokenExp>,
        private val mapPattern: MutableMap<String, IPatternHandler>,
        private val iter: IRegexStringIterator) : OPTableSolver() {

    /**
     * 非终结符映射
     */
    protected var mapNonTerminals = mutableMapOf<RuleExp, Int>()

    /**
     * 终结符映射
     */
    protected var mapTerminals = mutableMapOf<TokenExp, Int>()

    /**
     * 算符优先分析表
     */
    private var table: Array<Array<PrecedenceType>>? = null

    /**
     * 得到当前终结符ID
     *
     * @return 终结符ID
     */
    private val tokenId: Int
        get() {
            val token = iter.ex().token()
            if (token.type === TokenType.EOF) {
                return -2
            }
            for (exp in arrTerminals) {
                if (exp.kType === token.type && (exp.`object` == null || exp.`object` == token.obj)) {
                    return exp.id
                }
            }
            return -1
        }

    /**
     * 获得矩阵描述
     *
     * @return 矩阵描述
     */
    val matrixString: String
        get() {
            val sb = StringBuilder()
            val size = arrTerminals.size
            sb.append("#### 算符优先关系矩阵 ####")
            sb.append(System.lineSeparator())
            sb.append("\t")
            for (i in 0 until size) {
                sb.append(i).append("\t")
            }
            sb.append(System.lineSeparator())
            for (i in 0 until size) {
                sb.append(i).append("\t")
                for (j in 0 until size) {
                    sb.append(table!![i][j].desc).append("\t")
                }
                sb.append(System.lineSeparator())
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
     * 抛出异常
     *
     * @param error 错误类型
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    private fun err(error: SyntaxError) {
        throw SyntaxException(error, iter.position(), iter.ex().token())
    }

    /**
     * 添加归约模式
     *
     * @param pattern 模式串（由0和1组成，0=Vn，1=Vt）
     * @param handler 处理器
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    fun addPatternHandler(pattern: String, handler: IPatternHandler) {
        if (mapPattern.put(pattern, handler) != null) {
            err(SyntaxError.REDECLARATION)
        }
    }

    /**
     * 初始化
     */
    private fun initialize() {
        initMap()
        initTable()
        buildFirstVTAndLastVt()
        buildTable()
    }

    /**
     * 初始化符号映射表
     */
    private fun initMap() {
        for (i in arrNonTerminals.indices) {
            mapNonTerminals[arrNonTerminals[i]] = i
        }
        for (i in arrTerminals.indices) {
            mapTerminals[arrTerminals[i]] = i
        }
    }

    /**
     * 初始化算符优先关系表
     */
    private fun initTable() {
        val size = arrTerminals.size
        table = Array(size) { Array(size) { PrecedenceType.NULL } }
    }

    /**
     * 构造FirstVT和LastVT
     */
    private fun buildFirstVTAndLastVt() {
        /* 迭代求值 */
        var update: Boolean
        do {
            update = false
            for (exp in arrNonTerminals) {
                for (item in exp.rule.arrRules) {
                    val firstVT = FirstVTSolver(exp)
                    val lastVT = LastVTSolver(exp)
                    item.expression.visit(firstVT)
                    item.expression.visitReverse(lastVT)
                    update = update or firstVT.isUpdated
                    update = update or lastVT.isUpdated
                }
            }
        } while (update)
        /* 将哈希表按ID排序并保存 */
        for (exp in arrNonTerminals) {
            exp.rule.arrFirstVT = sortTerminal(exp.rule.setFirstVT).toMutableList()
            exp.rule.arrLastVT = sortTerminal(exp.rule.setLastVT).toMutableList()
        }
    }

    /**
     * 建立算符优先表
     */
    private fun buildTable() {
        for (exp in arrNonTerminals) {
            for (item in exp.rule.arrRules) {
                item.expression.visit(this)
            }
        }
    }

    override fun setCell(x: Int, y: Int, type: PrecedenceType) {
        if (table!![x][y] == PrecedenceType.NULL) {
            table!![x][y] = type
        } else if (table!![x][y] != type) {
            System.err.println(String.format("项目冲突：[%s] -> [%s]，'%s' -> '%s'",
                    arrTerminals[x], arrTerminals[y],
                    table!![x][y].desc, type.desc))
        }
    }

    /**
     * 给指定终结符集合按ID排序
     *
     * @param colletion 要排序的集合
     * @return 排序后的结果
     */
    private fun sortTerminal(colletion: Collection<TokenExp>): List<TokenExp> {
        val list = colletion.toList()
        return list.sortedBy { it.id }
    }

    private fun println() {
        if (debug)
            println()
    }

    private fun println(str: String) {
        if (debug)
            println(str)
    }

    /**
     * 进行分析
     *
     * @return 计算后的值
     * @throws GrammarException 语法错误
     */
    @Throws(GrammarException::class)
    fun run(): Any? {
        /* 指令堆栈 */
        val spi = Stack<PredictionInstruction>()
        /* 数据堆栈 */
        val sobj = Stack<FixedData>()
        /* 结束符号进栈 */
        spi.push(PredictionInstruction(PredictType.EPSILON, -1))
        sobj.push(FixedData())
        /* 执行步骤顺序 */
        var index = 0
        /* 输入字符索引 */
        var input = tokenId// #为-2
        /* 栈顶的终结符索引 */
        var top = -1
        /* 栈顶的终结符位置 */
        var topIndex = -1
        while (!(spi.size == 2 && input == -2)) {// 栈层为2且输入为#，退出
            index++
            println("步骤[$index]")
            println("\t----------------")
            if (input == -1) {// 没有找到，非法字符
                err(GrammarError.UNDECLARED)
            }
            if (input == -2 && spi.size == 1) {// 栈为#，输入为#，报错
                err(GrammarError.NULL)
            }
            val token = iter.ex().token()
            println("\t输入：[$token]")
            if (top != -1 && input != -2
                    && table!![top][input] == PrecedenceType.NULL) {
                err(GrammarError.MISS_PRECEDENCE)
            }
            if (top == -1 || input != -2 && table!![top][input] != PrecedenceType.GT) {
                /* 栈顶为#，或者top<=input，则直接移进 */
                println("\t移进：[$token]")
                /* 1.指令进栈 */
                spi.push(PredictionInstruction(PredictType.TERMINAL, input))
                /* 2.数据进栈 */
                sobj.push(FixedData(token))
                /* 3.保存单词 */
                iter.ex().saveToken()
                /* 4.取下一个单词 */
                iter.scan()
                /* 5.刷新当前输入字符索引 */
                input = tokenId
            } else {
                /* 不是移进就是归约 */
                /* 1.从栈顶向下寻找第一个出现LT的终结符 */
                var head: Int
                var comp_top = top
                var comp_top_index = topIndex
                head = topIndex - 1
                while (head >= 0) {
                    if (spi[head].type == PredictType.EPSILON) {
                        // 找到底部#
                        comp_top_index = head + 1
                        break
                    }
                    if (spi[head].type == PredictType.TERMINAL) {
                        if (table!![spi[head].inst][comp_top] == PrecedenceType.LT) {
                            // 找到第一个优先级LT的
                            comp_top_index = head + 1
                            break
                        } else if (table!![spi[head].inst][comp_top] == PrecedenceType.EQ) {
                            // 素短语内部优先级相同
                            comp_top = spi[head].inst
                            comp_top_index = head
                        }
                    }
                    head--
                }
                // head原来为最左素短语的头，从head+1到栈顶为可归约子串
                val primePhraseCount = spi.size - comp_top_index
                /* 2.保存最左素短语 */
                val primeInstList = mutableListOf<PredictionInstruction>()
                val primeDataList = mutableListOf<FixedData>()
                for (i in 0 until primePhraseCount) {
                    primeInstList.add(0, spi.pop())
                    primeDataList.add(0, sobj.pop())
                }
                println("\t----==== 最左素短语模式 ====----")
                val pattern = getPattern(primeInstList)
                println("\t" + pattern + ": "
                        + pattern.replace("0", "[op]").replace("1", "[tok]"))
                println("\t----==== 最左素短语 ====----")
                for (i in 0 until primePhraseCount) {
                    println("\t" + primeDataList[i])
                }
                /* 3.新建指令集和数据集（用于用户级回调） */
                val tempTokenList = mutableListOf<Token>()
                val tempObjectList = mutableListOf<Any>()
                for (i in 0 until primePhraseCount) {
                    val pt = primeInstList[i].type
                    if (pt == PredictType.TERMINAL) {
                        tempTokenList.add(primeDataList[i].token!!)
                    } else if (pt == PredictType.NONTERMINAL) {
                        tempObjectList.add(primeDataList[i].obj!!)
                    }
                }
                /* 4.寻找定义过的有效的模式，进行归约 */
                val handler = mapPattern[pattern]
                if (handler == null) {
                    System.err.println("缺少处理模式：" + pattern + ": "
                            + pattern.replace("0", "[op]").replace("1", "[tok]"))
                    err(GrammarError.MISS_HANDLER)
                }
                println("\t----==== 处理模式名称 ====----")
                println("\t" + handler!!.patternName)
                /* 5.归约处理 */
                val result = handler.handle(tempTokenList, tempObjectList)
                println("\t----==== 处理结果 ====----")
                println("\t" + result)
                /* 将结果压栈 */
                /* 6.指令进栈（非终结符进栈） */
                spi.push(PredictionInstruction(PredictType.NONTERMINAL, -1))
                /* 7.数据进栈（结果进栈） */
                sobj.push(FixedData(result))
            }
            println("\t----==== 指令堆栈 ====----")
            for (i in spi.indices.reversed()) {
                val pi = spi[i]
                when (pi.type) {
                    PredictType.NONTERMINAL -> println("\t$i: [数据]")
                    PredictType.TERMINAL -> println("\t" + i + ": ["
                            + arrTerminals[pi.inst].toString() + "]")
                    PredictType.EPSILON -> println("\t" + i + ": ["
                            + TokenType.EOF.desc + "]")
                }
            }
            println("\t----==== 数据堆栈 ====----")
            for (i in sobj.indices.reversed()) {
                println("\t" + i + ": [" + sobj[i] + "]")
            }
            println()
            /* 更新栈顶终结符索引 */
            if (spi.peek().type == PredictType.TERMINAL) {
                top = spi.peek().inst
                topIndex = spi.size - 1
            } else {// 若栈顶为非终结符，则第二顶必为终结符
                top = spi.elementAt(spi.size - 2).inst
                topIndex = spi.size - 2
            }
        }
        println()
        return if (sobj.peek().obj == null) sobj.peek().token!!.obj else sobj.peek().obj
    }

    override fun toString(): String {
        return matrixString
    }

    companion object {

        private val debug = false

        /**
         * 获取素短语模式
         *
         * @param spi 素短语
         * @return 模式字符串
         */
        private fun getPattern(spi: Collection<PredictionInstruction>): String {
            val sb = StringBuilder()
            for (pi in spi) {
                sb.append(if (pi.type == PredictType.TERMINAL) "1" else "0")
            }
            return sb.toString()
        }
    }
}
