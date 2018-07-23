package com.bajdcc.LALR1.syntax.lexer

import com.bajdcc.LALR1.syntax.lexer.tokenizer.*
import com.bajdcc.LALR1.syntax.token.Token
import com.bajdcc.LALR1.syntax.token.TokenType
import com.bajdcc.util.lexer.algorithm.ITokenAlgorithm
import com.bajdcc.util.lexer.algorithm.TokenAlgorithmCollection
import com.bajdcc.util.lexer.algorithm.impl.WhitespaceTokenizer
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.regex.IRegexStringFilterHost
import com.bajdcc.util.lexer.regex.RegexStringIterator
import com.bajdcc.util.lexer.token.MetaType

/**
 * 解析文法的词法分析器
 *
 * @author bajdcc
 */
class SyntaxLexer @Throws(RegexException::class)
constructor() : RegexStringIterator(), IRegexStringFilterHost {

    /**
     * 算法集合（正则表达式匹配）
     */
    private val algorithmCollections = TokenAlgorithmCollection(this, this)

    /**
     * 字符转换算法
     */
    private var tokenAlgorithm: ITokenAlgorithm? = null

    /**
     * 丢弃的类型集合
     */
    private val setDiscardToken = mutableSetOf<TokenType>()

    /**
     * 设置要分析的内容
     *
     * @param context 文法推导式
     */
    override var context: String
        get() = super.context
        set(context) {
            super.context = context
            position.column = 0
            position.line = 0
            data.current = '\u0000'
            data.index = 0
            data.meta = MetaType.END
            stackIndex.clear()
            stackPosition.clear()
        }

    init {
        initialize()
    }

    /**
     * 获取一个单词
     *
     * @return 单词
     */
    fun nextToken(): Token? {
        val token = Token.transfer(algorithmCollections.scan())
        return if (setDiscardToken.contains(token.type)) {// 需要丢弃
            null
        } else token
    }

    /**
     * 设置丢弃符号
     *
     * @param type 要丢弃的符号类型（不建议丢弃EOF，因为需要用来判断结束）
     */
    fun discard(type: TokenType) {
        setDiscardToken.add(type)
    }

    override fun setFilter(alg: ITokenAlgorithm) {
        tokenAlgorithm = alg
    }

    override fun transform() {
        super.transform()
        if (tokenAlgorithm != null) {
            val type = tokenAlgorithm!!.metaHash[data.current]
            if (type != null)
                data.meta = type
        }
    }

    /**
     * 初始化（添加组件）
     *
     * @throws RegexException 正则表达式错误
     */
    @Throws(RegexException::class)
    private fun initialize() {
        // ### 算法容器中装载解析组件是有一定顺序的 ###
        //
        // 组件调用原理：
        // 每个组件有自己的正则表达式匹配字符串
        // （可选）有自己的过滤方法，如字符串中的转义过滤
        //
        // 解析时，分别按序调用解析组件，若组件解析失败，则调用下一组件
        // 若某一组件解析成功，即返回匹配结果
        // 若全部解析失败，则调用出错处理（默认为前进一字符）
        //
        algorithmCollections.attach(WhitespaceTokenizer())// 空白字符解析组件
        algorithmCollections.attach(CommentTokenizer())// 注释解析组件
        algorithmCollections.attach(PropertyTokenizer())// 属性解析组件
        algorithmCollections.attach(ActionTokenizer())// 语义动作解析组件
        algorithmCollections.attach(TerminalTokenizer())// 终结符解析组件
        algorithmCollections.attach(NonTerminalTokenizer())// 非终结符解析组件
        algorithmCollections.attach(NumberTokenizer())// 存储序号解析组件
        algorithmCollections.attach(OperatorTokenizer())// 操作符解析组件
    }
}
