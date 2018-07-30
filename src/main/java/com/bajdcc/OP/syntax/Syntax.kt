package com.bajdcc.OP.syntax

import com.bajdcc.OP.syntax.exp.BranchExp
import com.bajdcc.OP.syntax.exp.RuleExp
import com.bajdcc.OP.syntax.exp.SequenceExp
import com.bajdcc.OP.syntax.exp.TokenExp
import com.bajdcc.OP.syntax.handler.SyntaxException
import com.bajdcc.OP.syntax.handler.SyntaxException.SyntaxError
import com.bajdcc.OP.syntax.lexer.SyntaxLexer
import com.bajdcc.OP.syntax.rule.RuleItem
import com.bajdcc.OP.syntax.solver.CheckOperatorGrammar
import com.bajdcc.OP.syntax.stringify.SyntaxToString
import com.bajdcc.OP.syntax.token.OperatorType
import com.bajdcc.OP.syntax.token.Token
import com.bajdcc.OP.syntax.token.TokenType
import com.bajdcc.util.Position
import com.bajdcc.util.lexer.error.RegexException

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
    /* 起始符号 *//* 终结符 *//* 非终结符 *//* 推导规则 *//* 规则正文 *//* FirstVT集合 *//* LastVT集合 */ val paragraphString: String
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
                    sb.append("\t--== FirstVT ==--")
                    sb.append(System.lineSeparator())
                    for (token in exp.rule.arrFirstVT) {
                        sb.append("\t\t").append(token.toString())
                        sb.append(System.lineSeparator())
                    }
                    sb.append("\t--== LastVT ==--")
                    sb.append(System.lineSeparator())
                    for (token in exp.rule.arrLastVT) {
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
     * 添加终结符
     *
     * @param name 终结符名称
     * @param type 单词类型
     * @param obj  单词信息
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    fun addTerminal(name: String,
                    type: com.bajdcc.util.lexer.token.TokenType, obj: Any?) {
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
    protected fun err(error: SyntaxError) {
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
        checkValid()
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
     * 检查产生式的合法性
     *
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    private fun checkValid() {
        for (exp in arrNonTerminals) {
            for (item in exp.rule.arrRules) {
                val check = CheckOperatorGrammar()
                item.expression.visit(check)
                if (!check.isValid) {
                    err(SyntaxError.CONSEQUENT_NONTERMINAL,
                            check.invalidName
                                    + ": "
                                    + getSingleString(exp.name, item.expression))
                }
            }
        }
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
