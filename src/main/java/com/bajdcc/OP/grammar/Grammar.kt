package com.bajdcc.OP.grammar

import com.bajdcc.LALR1.semantic.lexer.TokenFactory
import com.bajdcc.OP.grammar.error.GrammarException
import com.bajdcc.OP.grammar.handler.IPatternHandler
import com.bajdcc.OP.syntax.Syntax
import com.bajdcc.OP.syntax.handler.SyntaxException
import com.bajdcc.OP.syntax.handler.SyntaxException.SyntaxError
import com.bajdcc.OP.syntax.precedence.PrecedenceTable
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 【语法分析】语法分析
 *
 * @author bajdcc
 */
class Grammar @Throws(RegexException::class)
constructor(context: String) : Syntax(true) {

    /**
     * 单词流工厂
     */
    private val tokenFactory: TokenFactory = TokenFactory(context)

    /**
     * 单词流
     */
    private var arrTokens: MutableList<Token> = mutableListOf()

    /**
     * 预测分析表
     */
    private var table: PrecedenceTable? = null

    /**
     * 归约模式映射
     */
    private val mapPattern = mutableMapOf<String, IPatternHandler>()

    /**
     * 获得算符优先关系描述
     *
     * @return 算符优先关系描述
     */
    val precedenceString: String
        get() = table!!.toString()

    /**
     * 获得单词流描述
     *
     * @return 单词流描述
     */
    val tokenString: String
        get() {
            val sb = StringBuilder()
            sb.append("#### 单词流 ####")
            sb.append(System.lineSeparator())
            for (token in arrTokens) {
                sb.append(token.toString())
                sb.append(System.lineSeparator())
            }
            return sb.toString()
        }

    init {
        // 用于分析的文本
        tokenFactory.discard(TokenType.COMMENT)
        tokenFactory.discard(TokenType.WHITESPACE)
        tokenFactory.discard(TokenType.ERROR)
        tokenFactory.discard(TokenType.MACRO)
        tokenFactory.scan()
    }

    /**
     * 初始化
     *
     * @param startSymbol 开始符号
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    override fun initialize(startSymbol: String) {
        super.initialize(startSymbol)
        generateTable()
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
     * 生成算符优先分析表
     */
    private fun generateTable() {
        table = PrecedenceTable(arrNonTerminals, arrTerminals, mapPattern,
                tokenFactory)
    }

    /**
     * 进行语法分析
     *
     * @return 计算后的值
     * @throws GrammarException 语法错误
     */
    @Throws(GrammarException::class)
    fun run(): Any? {
        val obj = table!!.run()
        arrTokens = tokenFactory.tokenList().toMutableList()
        return obj
    }
}
