package com.bajdcc.LL1.grammar

import com.bajdcc.LALR1.semantic.lexer.TokenFactory
import com.bajdcc.LL1.grammar.error.GrammarException
import com.bajdcc.LL1.syntax.Syntax
import com.bajdcc.LL1.syntax.handler.SyntaxException
import com.bajdcc.LL1.syntax.prediction.PredictionTable
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
    private val tokenFactory: TokenFactory

    /**
     * 单词流
     */
    private var arrTokens: MutableList<Token>? = null

    /**
     * 预测分析表
     */
    private var table: PredictionTable? = null

    /**
     * 获得预测分析表描述
     *
     * @return 预测分析表描述
     */
    val predictionString: String
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
            for (token in arrTokens!!) {
                sb.append(token.toString())
                sb.append(System.lineSeparator())
            }
            return sb.toString()
        }

    init {
        tokenFactory = TokenFactory(context)// 用于分析的文本
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
     * 生成预测分析表
     */
    private fun generateTable() {
        table = PredictionTable(arrNonTerminals, arrTerminals,
                mapNonTerminals[beginRuleName]!!.rule,
                mapTerminals[epsilon]!!, tokenFactory)
    }

    /**
     * 进行语法分析
     *
     * @throws GrammarException 语法错误
     */
    @Throws(GrammarException::class)
    fun run() {
        table!!.run()
        arrTokens = tokenFactory.tokenList().toMutableList()
    }
}
