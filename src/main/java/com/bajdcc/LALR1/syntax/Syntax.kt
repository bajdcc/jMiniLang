package com.bajdcc.LALR1.syntax

import com.bajdcc.LALR1.grammar.semantic.ISemanticAction
import com.bajdcc.LALR1.syntax.automata.npa.NPA
import com.bajdcc.LALR1.syntax.exp.*
import com.bajdcc.LALR1.syntax.handler.IErrorHandler
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.LALR1.syntax.handler.SyntaxException.SyntaxError
import com.bajdcc.LALR1.syntax.lexer.SyntaxLexer
import com.bajdcc.LALR1.syntax.rule.RuleItem
import com.bajdcc.LALR1.syntax.solver.FirstsetSolver
import com.bajdcc.LALR1.syntax.stringify.SyntaxToString
import com.bajdcc.LALR1.syntax.token.OperatorType
import com.bajdcc.LALR1.syntax.token.Token
import com.bajdcc.LALR1.syntax.token.TokenType
import com.bajdcc.util.BitVector2
import com.bajdcc.util.Position
import com.bajdcc.util.lexer.error.RegexException
import java.util.*
import com.bajdcc.util.lexer.token.TokenType as LexerTokenType

/**
 * 【语法分析】文法构造器
 *
 *
 * 语法示例：
 *
 *
 * <pre>
 * Z -&gt; A | B[1] | @abc | ( A[0] | @Terminal&lt;terminal comment text&gt; |
 * C[1]&lt;comment&gt; | C[storage id]#Action name#{Error handler name})
</pre> *
 *
 * @author bajdcc
 */
open class Syntax @Throws(RegexException::class)
@JvmOverloads constructor(ignoreLexError: Boolean = true) {

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
    val paragraphString: String
        get() {
            val sb = StringBuilder()
            sb.append("#### 起始符号 ####")
            sb.append(System.lineSeparator())
            sb.append(strBeginRuleName)
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
                    sb.append("\t--== 终结符First集合 ==--")
                    sb.append(System.lineSeparator())
                    for (token in item.setFirstSetTokens) {
                        sb.append("\t\t").append(token.name)
                        sb.append(System.lineSeparator())
                    }
                    sb.append("\t--== 非终结符First集合 ==--")
                    sb.append(System.lineSeparator())
                    for (rule in item.setFirstSetRules) {
                        sb.append("\t\t").append(rule.name)
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

    /**
     * 获得非确定性文法自动机描述
     *
     * @return 非确定性文法自动机描述
     */
    val ngaString: String
        get() = npa!!.ngaString

    /**
     * 获得非确定性下推自动机描述
     *
     * @return 获得非确定性下推自动机描述
     */
    val npaString: String
        get() = npa!!.npaString

    val npaMarkdownString: String
        get() = npa!!.npaMarkdownString

    init {
        syntaxLexer.discard(TokenType.COMMENT)
        syntaxLexer.discard(TokenType.WHITESPACE)
        if (ignoreLexError) {
            syntaxLexer.discard(TokenType.ERROR)
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
    fun addTerminal(name: String, type: LexerTokenType, obj: Any?) {
        val exp = TokenExp(arrTerminals.size, name, type, obj)
        if (!mapTerminals.containsKey(name)) {
            mapTerminals[name] = exp
            arrTerminals.add(exp)
        } else {
            err(SyntaxError.REDECLARATION, name)
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
     * 添加错误处理器
     *
     * @param name    处理器名
     * @param handler 处理接口
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    fun addErrorHandler(name: String, handler: IErrorHandler) {
        if (!mapHandlers.containsKey(name)) {
            mapHandlers[name] = handler
            arrHandlers.add(handler)
        } else {
            err(SyntaxError.REDECLARATION)
        }
    }

    /**
     * 添加语义动作
     *
     * @param name    动作名称
     * @param handler 处理接口
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    fun addActionHandler(name: String, handler: ISemanticAction) {
        if (!mapActions.containsKey(name)) {
            mapActions[name] = handler
            arrActions.add(handler)
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
        throw SyntaxException(error, syntaxLexer.position(), token!!.obj)
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
        if (token!!.type === type) {
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
        if (token!!.type !== type) {
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
        if (!mapNonTerminals.containsKey(token!!.obj!!.toString())) {
            err(SyntaxError.UNDECLARED, token!!)
        }
        return mapNonTerminals[token!!.obj!!.toString()]!!
    }

    /**
     * 匹配终结符
     *
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    private fun matchTerminal(): TokenExp {
        match(TokenType.TERMINAL, SyntaxError.SYNTAX)
        if (!mapTerminals.containsKey(token!!.obj!!.toString())) {
            err(SyntaxError.UNDECLARED, token!!)
        }
        return mapTerminals[token!!.obj!!.toString()]!!
    }

    /**
     * 匹配错误处理器
     *
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    private fun matchHandler(): IErrorHandler {
        match(TokenType.HANDLER, SyntaxError.SYNTAX)
        if (!mapHandlers.containsKey(token!!.obj!!.toString())) {
            err(SyntaxError.UNDECLARED, token!!)
        }
        return mapHandlers[token!!.obj!!.toString()]!!
    }

    /**
     * 匹配语义动作
     *
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    private fun matchAction(): ISemanticAction {
        match(TokenType.ACTION, SyntaxError.SYNTAX)
        if (!mapActions.containsKey(token!!.obj!!.toString())) {
            err(SyntaxError.UNDECLARED, token!!)
        }
        return mapActions[token!!.obj!!.toString()]!!
    }

    /**
     * 匹配存储序号
     *
     * @param exp     表达式
     * @param storage 存储序号
     * @return 属性对象
     */
    private fun matchStorage(exp: ISyntaxComponent?, storage: Any?): PropertyExp {
        val property: PropertyExp
        if (token!!.type === TokenType.STORAGE) {
            property = PropertyExp(storage as Int, exp!!)
            next()
        } else {
            property = PropertyExp(-1, exp!!)
        }
        return property
    }

    /**
     * 取下一个单词
     */
    private operator fun next() {
        do {
            token = syntaxLexer.nextToken()
        } while (token == null)
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
        rule = mapNonTerminals[token!!.obj!!.toString()]
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
    protected open fun onAddRuleItem(item: RuleItem) {

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
            if (token!!.type === type && (token!!.obj == null || token!!.obj == obj)) {// 结束字符
                if (syntaxLexer.index() == 0) {// 表达式为空
                    err(SyntaxError.NULL)
                } else if (collection.isEmpty) {// 部件为空
                    err(SyntaxError.INCOMPLETE)
                } else {
                    next()
                    break// 正常终止
                }
            } else if (token!!.type === TokenType.EOF) {
                err(SyntaxError.INCOMPLETE)
            }
            var exp: ISyntaxComponent? = null// 当前待赋值的表达式
            if (token!!.type == TokenType.OPERATOR) {
                val op = token!!.obj as OperatorType?
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
                } else if (op == OperatorType.LPARAN)// '('
                    exp = doAnalysis(TokenType.OPERATOR, OperatorType.RPARAN)// 递归分析
                else if (op == OperatorType.LSQUARE) {// '['
                    exp = doAnalysis(TokenType.OPERATOR, OperatorType.RSQUARE)// 递归分析
                    exp = OptionExp(exp)
                } else err(SyntaxError.SYNTAX)
            }
            else if (token!!.type == TokenType.EOF) return result
            else if (token!!.type == TokenType.TERMINAL) {
                exp = matchTerminal()
                next()
                val property1 = matchStorage(exp, token!!.obj)
                exp = property1
                if (token!!.type === TokenType.ACTION) {
                    property1.actionHandler = matchAction()
                    next()
                }
                if (token!!.type === TokenType.HANDLER) {
                    property1.errorHandler = matchHandler()
                    next()
                }
            }
            else if (token!!.type == TokenType.NONTERMINAL) {
                exp = matchNonTerminal()
                next()
                val property2 = matchStorage(exp, token!!.obj)
                exp = property2
                // 注意：下面的设置是无效的
                if (token!!.type === TokenType.ACTION) {
                    property2.actionHandler = matchAction()
                    next()
                }
                if (token!!.type === TokenType.HANDLER) {
                    property2.errorHandler = matchHandler()
                    next()
                }
            }
            else err(SyntaxError.SYNTAX)

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
    fun initialize(startSymbol: String) {
        strBeginRuleName = startSymbol
        checkStartSymbol()
        semanticAnalysis()
        generateNPA()
    }

    /**
     * 检测起始符号合法性
     *
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    private fun checkStartSymbol() {
        if (!mapNonTerminals.containsKey(strBeginRuleName)) {
            err(SyntaxError.UNDECLARED)
        }
    }

    /**
     * 进行语义分析
     *
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    private fun semanticAnalysis() {
        /* 非终结符数量 */
        val size = arrNonTerminals.size
        /* 计算规则的First集合 */
        for (exp in arrNonTerminals) {
            for (item in exp.rule.arrRules) {
                val solver = FirstsetSolver()
                item.expression.visit(solver)// 计算规则的First集合
                if (!solver.solve(item)) {// 若串长度可能为零，即产生空串
                    err(SyntaxError.EPSILON,
                            getSingleString(exp.name, item.expression))
                }
            }
        }
        /* 建立连通矩阵 */
        val firstsetDependency = BitVector2(size, size)// First集依赖矩阵
        firstsetDependency.clear()
        /* 计算非终结符First集合包含关系的布尔连通矩阵 */
        run {
            for ((i, exp) in arrNonTerminals.withIndex()) {
                for (item in exp.rule.arrRules) {
                    for (rule in item.setFirstSetRules) {
                        firstsetDependency[i] = rule.id
                    }
                }
            }
        }
        /* 计算间接左递归 */
        run {
            /* 标记并清除直接左递归 */
            for (i in 0 until size) {
                if (firstsetDependency.test(i, i)) {
                    arrNonTerminals[i].rule.iRecursiveLevel = 1// 直接左递归
                    firstsetDependency.clear(i, i)
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
                    for ((i, exp) in arrNonTerminals.withIndex()) {
                        if (r.test(i, i)) {
                            if (exp.rule.iRecursiveLevel < 2) {
                                exp.rule.iRecursiveLevel = level
                            }
                        }
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
        /* 计算完整的First集合 */
        run {
            /* 建立处理标记表 */
            val processed = BitSet(size)
            processed.clear()
            for (arrNonTerminal in arrNonTerminals) {
                /* 找出一条无最左依赖的规则 */
                var independentRule = -1// 无最左依赖的规则索引
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
                            independentRule = j
                            break
                        }
                    }
                }
                if (independentRule == -1) {
                    err(SyntaxError.MISS_NODEPENDENCY_RULE,
                            arrNonTerminal.name)
                }
                /* 计算该规则的终结符First集合 */
                run {
                    val rule = arrNonTerminals[independentRule].rule
                    /* 计算规则的终结符First集合 */
                    for (item in rule.arrRules) {
                        for (exp in item.setFirstSetRules) {
                            item.setFirstSetTokens.addAll(exp.rule.arrTokens)
                        }
                    }
                    /* 计算非终结符的终结符First集合 */
                    for (item in rule.arrRules) {
                        rule.arrTokens.addAll(item.setFirstSetTokens)
                    }
                    /* 修正左递归规则的终结符First集合 */
                    for (item in rule.arrRules) {
                        if (item.setFirstSetRules.contains(arrNonTerminals[independentRule])) {
                            item.setFirstSetTokens.addAll(rule.arrTokens)
                        }
                    }
                }
                /* 清除该规则 */
                processed.set(independentRule)
                for (j in 0 until size) {
                    firstsetDependency.clear(j, independentRule)
                }
            }
        }
        /* 搜索不能产生字符串的规则 */
        for (exp in arrNonTerminals) {
            for (item in exp.rule.arrRules) {
                if (item.setFirstSetTokens.isEmpty()) {
                    err(SyntaxError.FAILED, getSingleString(exp.name, item.expression))
                }
            }
        }
    }

    /**
     * 生成非确定性下推自动机
     */
    private fun generateNPA() {
        npa = NPA(arrNonTerminals, arrTerminals, mapNonTerminals[strBeginRuleName]!!.rule, arrActions)
    }

    override fun toString(): String {
        return paragraphString
    }

    companion object {

        /**
         * 终结符表
         */
        var arrTerminals = mutableListOf<TokenExp>()

        /**
         * 终结符映射
         */
        var mapTerminals = mutableMapOf<String, TokenExp>()

        /**
         * 非终结符表
         */
        var arrNonTerminals = mutableListOf<RuleExp>()

        /**
         * 非终结符映射
         */
        var mapNonTerminals = mutableMapOf<String, RuleExp>()

        /**
         * 错误处理表
         */
        var arrHandlers = mutableListOf<IErrorHandler>()
        /**
         * 错误处理映射
         */
        var mapHandlers = mutableMapOf<String, IErrorHandler>()

        /**
         * 语义动作表
         */
        var arrActions = mutableListOf<ISemanticAction>()
        /**
         * 语义动作映射
         */
        var mapActions = mutableMapOf<String, ISemanticAction>()

        /**
         * 文法起始符号
         */
        var strBeginRuleName = ""

        /**
         * 非确定性下推自动机
         */
        var npa: NPA? = null

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
