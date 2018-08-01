package com.bajdcc.LL1.syntax

import com.bajdcc.LL1.syntax.exp.BranchExp
import com.bajdcc.LL1.syntax.exp.RuleExp
import com.bajdcc.LL1.syntax.exp.SequenceExp
import com.bajdcc.LL1.syntax.exp.TokenExp
import com.bajdcc.LL1.syntax.handler.SyntaxException
import com.bajdcc.LL1.syntax.handler.SyntaxException.SyntaxError
import com.bajdcc.LL1.syntax.lexer.SyntaxLexer
import com.bajdcc.LL1.syntax.rule.RuleItem
import com.bajdcc.LL1.syntax.solver.FirstSetSolver
import com.bajdcc.LL1.syntax.solver.FollowSetSolver
import com.bajdcc.LL1.syntax.stringify.SyntaxToString
import com.bajdcc.LL1.syntax.token.OperatorType
import com.bajdcc.LL1.syntax.token.Token
import com.bajdcc.LL1.syntax.token.TokenType
import com.bajdcc.util.BitVector2
import com.bajdcc.util.Position
import com.bajdcc.util.lexer.error.RegexException
import java.util.*

/**
 * 【语法分析】文法构造器
 *
 *
 * 语法示例：
 *
 *
 * <pre>
 * Z -&gt; A | B | @abc &lt;comment&gt;
</pre> *
 *
 * @author bajdcc
 */
@Suppress("UNUSED_PARAMETER")
open class Syntax @Throws(RegexException::class)
@JvmOverloads constructor(ignoreLexError: Boolean = true) {

    /**
     * 终结符表
     */
    protected var arrTerminals = mutableListOf<TokenExp>()

    /**
     * 终结符映射
     */
    protected var mapTerminals = mutableMapOf<String, TokenExp>()

    /**
     * 非终结符表
     */
    protected var arrNonTerminals = mutableListOf<RuleExp>()

    /**
     * 非终结符映射
     */
    protected var mapNonTerminals = mutableMapOf<String, RuleExp>()

    /**
     * 文法起始符号
     */
    protected var beginRuleName: String? = null

    /**
     * 空串符号
     */
    protected var epsilon: String = ""

    /**
     * 面向文法的词法分析器
     */
    private val syntaxLexer = SyntaxLexer()

    /**
     * 当前字符
     */
    private var token: Token? = null

    /**
     * 当前解析的文法规则
     */
    private var rule: RuleExp? = null

    /**
     * 获得段落式描述
     *
     * @return 段落式描述
     */
    /* 起始符号 *//* 终结符 *//* 非终结符 *//* 推导规则 *//* 规则正文 *//* First集合 */ val paragraphString: String
        get() {
            val sb = StringBuilder()
            sb.append("#### 起始符号 ####")
            sb.append(System.lineSeparator())
            sb.append(beginRuleName)
            sb.append(System.lineSeparator())
            sb.append("#### 终结符 ####")
            sb.append(System.lineSeparator())
            for (exp in arrTerminals) {
                sb.append(exp.toString())
                sb.append(System.lineSeparator())
            }
            sb.append("#### 非终结符 ####")
            sb.append(System.lineSeparator())
            for (exp in arrNonTerminals) {
                sb.append(exp.toString())
                sb.append(System.lineSeparator())
            }
            sb.append("#### 文法产生式 ####")
            sb.append(System.lineSeparator())
            for (exp in arrNonTerminals) {
                for (item in exp.rule.arrRules) {
                    sb.append(getSingleString(exp.name, item.expression))
                    sb.append(System.lineSeparator())
                    sb.append("\t--== First ==--")
                    sb.append(System.lineSeparator())
                    for (token in item.arrFirstSetTokens) {
                        sb.append("\t\t").append(token.toString())
                        sb.append(System.lineSeparator())
                    }
                    sb.append("\t--== Follow ==--")
                    sb.append(System.lineSeparator())
                    for (token in item.parent.arrFollows) {
                        sb.append("\t\t").append(token.toString())
                        sb.append(System.lineSeparator())
                    }
                }
            }
            return sb.toString()
        }

    /**
     * 获得原推导式描述
     *
     * @return 原推导式描述
     */
    val originalString: String
        get() {
            val sb = StringBuilder()
            for (exp in arrNonTerminals) {
                for (item in exp.rule.arrRules) {
                    sb.append(getSingleString(exp.name, item.expression))
                    sb.append(System.lineSeparator())
                }
            }
            return sb.toString()
        }

    init {
        syntaxLexer.discard(TokenType.COMMENT)
        syntaxLexer.discard(TokenType.WHITSPACE)
        if (ignoreLexError) {
            syntaxLexer.discard(TokenType.ERROR)
        }
    }

    /**
     * 定义空串名
     *
     * @param name 空串名称
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    fun setEpsilonName(name: String) {
        val exp = TokenExp(arrTerminals.size, name,
                com.bajdcc.util.lexer.token.TokenType.EOF, null)
        if (!mapTerminals.containsKey(name)) {
            epsilon = name
            mapTerminals[name] = exp
            arrTerminals.add(exp)
        } else {
            err(SyntaxError.REDECLARATION)
        }
    }

    /**
     * 添加终结符
     *
     * @param name 终结符名称
     * @param type 单词类型
     * @param obj  单词信息
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    fun addTerminal(name: String,
                    type: com.bajdcc.util.lexer.token.TokenType, obj: Any) {
        val exp = TokenExp(arrTerminals.size, name, type, obj)
        if (!mapTerminals.containsKey(name)) {
            mapTerminals[name] = exp
            arrTerminals.add(exp)
        } else {
            err(SyntaxError.REDECLARATION)
        }
    }

    /**
     * 添加非终结符
     *
     * @param name 非终结符名称
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    fun addNonTerminal(name: String) {
        val exp = RuleExp(arrNonTerminals.size, name)
        if (!mapNonTerminals.containsKey(name)) {
            mapNonTerminals[name] = exp
            arrNonTerminals.add(exp)
        } else {
            err(SyntaxError.REDECLARATION)
        }
    }

    /**
     * @param inferString 文法推导式
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    fun infer(inferString: String) {
        syntaxLexer.context = inferString
        compile()
    }

    /**
     * 抛出异常
     *
     * @param error 错误类型
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    private fun err(error: SyntaxError) {
        throw SyntaxException(error, syntaxLexer.position(), token!!.`object`)
    }

    /**
     * 抛出异常
     *
     * @param error 错误类型
     * @param obj   错误信息
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    private fun err(error: SyntaxError, obj: Any) {
        throw SyntaxException(error, Position(), obj)
    }

    /**
     * 匹配符号
     *
     * @param type  匹配类型
     * @param error 错误类型
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    private fun expect(type: TokenType, error: SyntaxError) {
        if (token!!.kToken == type) {
            next()
        } else {
            err(error)
        }
    }

    /**
     * 正确匹配当前字符
     *
     * @param type  匹配类型
     * @param error 匹配失败时抛出的异常
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    private fun match(type: TokenType, error: SyntaxError) {
        if (token!!.kToken != type) {
            err(error)
        }
    }

    /**
     * 匹配非终结符
     *
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    private fun matchNonTerminal(): RuleExp {
        match(TokenType.NONTERMINAL, SyntaxError.SYNTAX)
        if (!mapNonTerminals.containsKey(token!!.`object`!!.toString())) {
            err(SyntaxError.UNDECLARED)
        }
        return mapNonTerminals[token!!.`object`!!.toString()]!!
    }

    /**
     * 匹配终结符
     *
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    private fun matchTerminal(): TokenExp {
        match(TokenType.TERMINAL, SyntaxError.SYNTAX)
        if (!mapTerminals.containsKey(token!!.`object`!!.toString())) {
            err(SyntaxError.UNDECLARED)
        }
        return mapTerminals[token!!.`object`!!.toString()]!!
    }

    /**
     * 取下一个单词
     */
    private operator fun next(): Token {
        do {
            token = syntaxLexer.nextToken()
        } while (token == null)
        return token!!
    }

    /**
     * 编译推导式（将文本表达式转换成文法树）
     *
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    private fun compile() {
        /* 处理左端非终结符 */
        doHead()
        /* 处理右端表达式 */
        doTail()
    }

    /**
     * 处理左端非终结符
     *
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    private fun doHead() {
        /* 匹配推导式左端的非终结符 */
        next()
        matchNonTerminal()
        /* 设定本次需要推导的非终结符 */
        rule = mapNonTerminals[token!!.`object`!!.toString()]
        /* 匹配推导符号"->" */
        next()
        expect(TokenType.OPERATOR, SyntaxError.SYNTAX)
    }

    /**
     * 处理右端表达式
     *
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    private fun doTail() {
        /* 获得分析后的表达式根结点 */
        val exp = doAnalysis(TokenType.EOF, null)
        /* 将根结点添加进对应规则 */
        val item = RuleItem(exp, rule!!.rule)
        onAddRuleItem(item)
        rule!!.rule.arrRules.add(item)
    }

    /**
     * 创建推导式之后的回调函数
     *
     * @param item 推导式
     */
    protected fun onAddRuleItem(item: RuleItem) {

    }

    /**
     * 分析表达式
     *
     * @param type 结束类型
     * @param obj  结束数据
     * @return 表达式树根结点
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    private fun doAnalysis(type: TokenType, obj: Any?): ISyntaxComponent {

        /* 新建序列用于存放结果 */
        var sequence = SequenceExp()
        /* 可能会使用的分支 */
        var branch: BranchExp? = null
        /* 添加子结点接口 */
        val collection = sequence
        /* 表达式通用接口 */
        var result: ISyntaxComponent = sequence

        while (true) {
            if (token!!.kToken == type && (token!!.`object` == null || token!!.`object` == obj)) {// 结束字符
                if (syntaxLexer.index() == 0) {// 表达式为空
                    err(SyntaxError.NULL)
                } else if (collection.isEmpty) {// 部件为空
                    err(SyntaxError.INCOMPLETE)
                } else {
                    next()
                    break// 正常终止
                }
            } else if (token!!.kToken == TokenType.EOF) {
                err(SyntaxError.INCOMPLETE)
            }
            var exp: ISyntaxComponent? = null// 当前待赋值的表达式
            if (token!!.kToken == TokenType.OPERATOR) {
                val op = token!!.`object` as OperatorType
                next()
                if (op == OperatorType.ALTERNATIVE) {
                    if (collection.isEmpty)
                    // 在此之前没有存储表达式 (|...)
                    {
                        err(SyntaxError.INCOMPLETE)
                    } else {
                        if (branch == null) {// 分支为空，则建立分支
                            branch = BranchExp()
                            branch.add(sequence)// 用新建的分支包含并替代当前序列
                            result = branch
                        }
                        sequence = SequenceExp()// 新建一个序列
                        branch.add(sequence)
                        continue
                    }
                } else err(SyntaxError.SYNTAX)
            } else if (token!!.kToken == TokenType.EOF) return result
            else if (token!!.kToken == TokenType.TERMINAL) {
                exp = matchTerminal()
                next()
            } else if (token!!.kToken == TokenType.NONTERMINAL) {
                exp = matchNonTerminal()
                next()
            } else err(SyntaxError.SYNTAX)

            if (exp != null) {
                sequence.add(exp)
            }
        }
        return result
    }

    /**
     * 初始化
     *
     * @param startSymbol 开始符号
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    open fun initialize(startSymbol: String) {
        beginRuleName = startSymbol
        checkStartSymbol()
        buildFirstAndFollow()
    }

    /**
     * 检测起始符号合法性
     *
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    private fun checkStartSymbol() {
        if (!mapNonTerminals.containsKey(beginRuleName)) {
            err(SyntaxError.UNDECLARED)
        }
    }

    /**
     * 构造First集和Follow集
     *
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    private fun buildFirstAndFollow() {
        /* 非终结符数量 */
        val size = arrNonTerminals.size
        /* 计算规则的First集合 */
        var update: Boolean
        do {
            update = false
            for (exp in arrNonTerminals) {
                for (item in exp.rule.arrRules) {
                    val solver = FirstSetSolver()
                    item.expression.visit(solver)// 计算规则的First集合
                    item.epsilon = solver.solve(item)
                    if (item.epsilon && !exp.rule.epsilon) {
                        exp.rule.epsilon = true
                        update = true
                    }
                }
            }
        } while (update)
        /* 建立连通矩阵 */
        val firstsetDependency = BitVector2(size, size)// First集依赖矩阵
        firstsetDependency.clear()
        /* 计算非终结符First集合包含关系的布尔连通矩阵 */
        run {
            var i = 0
            for (exp in arrNonTerminals) {
                for (item in exp.rule.arrRules) {
                    for (rule in item.setFirstSetRules) {
                        firstsetDependency[i] = rule.id
                    }
                }
                i++
            }
        }
        /* 检查间接左递归 */
        run {
            /* 标记直接左递归 */
            for (i in 0 until size) {
                if (firstsetDependency.test(i, i)) {// 出现直接左递归
                    err(SyntaxError.DIRECT_RECURSION,
                            arrNonTerminals[i].name)
                }
            }
            /* 获得拷贝 */
            var a = firstsetDependency.clone() as BitVector2
            val b = firstsetDependency.clone() as BitVector2
            val r = BitVector2(size, size)
            /* 检查是否出现环 */
            for (level in 2 until size) {// Warshell算法：求有向图的传递闭包
                /* 进行布尔连通矩阵乘法，即r=aXb */
                for (i in 0 until size) {
                    for (j in 0 until size) {
                        r.clear(i, j)
                        for (k in 0 until size) {
                            val value = r.test(i, j) || a.test(i, k) && b.test(k, j)
                            r[i, j] = value
                        }
                    }
                }
                /* 检查当前结果是否出现环 */
                run {
                    var i = 0
                    for (exp in arrNonTerminals) {
                        if (r.test(i, i)) {
                            if (exp.rule.iRecursiveLevel < 2) {
                                exp.rule.iRecursiveLevel = level
                            }
                        }
                        i++
                    }
                }
                /* 保存结果 */
                a = r.clone() as BitVector2
            }
            /* 检查是否存在环并报告错误 */
            for (exp in arrNonTerminals) {
                if (exp.rule.iRecursiveLevel > 1) {
                    err(SyntaxError.INDIRECT_RECURSION, exp.name + " level:"
                            + exp.rule.iRecursiveLevel)
                }
            }
        }
        /* 建立规则的依赖关系 */
        val nodependencyList = mutableListOf<Int>()
        run {
            /* 建立处理标记表 */
            val processed = BitSet(size)
            processed.clear()
            /* 寻找First集的依赖关系 */
            for (arrNonTerminal in arrNonTerminals) {
                /* 找出一条无最左依赖的规则 */
                var nodependencyRule = -1// 最左依赖的规则索引
                for (j in 0 until size) {
                    if (!processed.get(j)) {
                        var empty = true
                        for (k in 0 until size) {
                            if (firstsetDependency.test(j, k)) {
                                empty = false
                                break
                            }
                        }
                        if (empty) {// 找到
                            nodependencyRule = j
                            break
                        }
                    }
                }
                if (nodependencyRule == -1) {
                    err(SyntaxError.MISS_NODEPENDENCY_RULE,
                            arrNonTerminal.name)
                }
                /* 清除该规则 */
                processed.set(nodependencyRule)
                for (j in 0 until size) {
                    firstsetDependency.clear(j, nodependencyRule)
                }
                nodependencyList.add(nodependencyRule)
            }
        }
        for (nodependencyRule in nodependencyList) {
            /* 计算该规则的终结符First集合 */
            val rule = arrNonTerminals[nodependencyRule].rule
            /* 计算规则的终结符First集合 */
            for (item in rule.arrRules) {
                for (exp in item.setFirstSetRules) {
                    item.setFirstSetTokens.addAll(exp.rule.arrFirsts)
                }
            }
            /* 计算非终结符的终结符First集合 */
            for (item in rule.arrRules) {
                rule.arrFirsts.addAll(item.setFirstSetTokens)
            }
        }
        /* 搜索不能产生字符串的规则 */
        for (exp in arrNonTerminals) {
            for (item in exp.rule.arrRules) {
                if (item.setFirstSetTokens.isEmpty()) {
                    err(SyntaxError.FAILED,
                            getSingleString(exp.name, item.expression))
                }
            }
        }
        /* 求Follow集 */
        mapNonTerminals[beginRuleName]!!.rule.setFollows.add(mapTerminals[epsilon]!!)
        do {
            update = false
            for (origin in arrNonTerminals) {
                for (item in origin.rule.arrRules) {
                    for (target in arrNonTerminals) {
                        val solver = FollowSetSolver(origin,
                                target)
                        item.expression.visit(solver)
                        update = update or solver.isUpdated
                    }
                }
            }
        } while (update)
        /* 将哈希表按ID排序并保存 */
        for (exp in arrNonTerminals) {
            for (item in exp.rule.arrRules) {
                item.arrFirstSetTokens = sortTerminal(item.setFirstSetTokens).toMutableList()
                item.parent.arrFollows = sortTerminal(item.parent.setFollows).toMutableList()
            }
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
        return list.sortedBy { o -> o.id }
    }

    override fun toString(): String {
        return paragraphString
    }

    companion object {

        /**
         * 获得单一产生式描述
         *
         * @param name    非终结符名称
         * @param exp     表达式树
         * @param focused 焦点
         * @param front   前向
         * @return 原产生式描述
         */
        fun getSingleString(name: String, exp: ISyntaxComponent,
                            focused: ISyntaxComponent, front: Boolean): String {
            val sb = StringBuilder()
            sb.append(name)
            sb.append(" -> ")
            val alg = SyntaxToString(focused, front)
            exp.visit(alg)
            sb.append(alg.toString())
            return sb.toString()
        }

        /**
         * 获得单一产生式描述
         *
         * @param name 非终结符名称
         * @param exp  表达式树
         * @return 原产生式描述
         */
        fun getSingleString(name: String, exp: ISyntaxComponent): String {
            val sb = StringBuilder()
            sb.append(name)
            sb.append(" -> ")
            val alg = SyntaxToString()
            exp.visit(alg)
            sb.append(alg.toString())
            return sb.toString()
        }
    }
}
