package com.bajdcc.util.lexer

import com.bajdcc.util.Position
import com.bajdcc.util.lexer.algorithm.ITokenAlgorithm
import com.bajdcc.util.lexer.algorithm.TokenAlgorithmCollection
import com.bajdcc.util.lexer.algorithm.impl.*
import com.bajdcc.util.lexer.algorithm.impl.StringTokenizer
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.regex.IRegexStringFilterHost
import com.bajdcc.util.lexer.regex.IRegexStringIteratorEx
import com.bajdcc.util.lexer.regex.RegexStringIterator
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType
import java.util.*

/**
 * 【词法分析】词法分析器
 *
 * @author bajdcc
 */
open class Lexer @Throws(RegexException::class)
constructor(context: String) : RegexStringIterator(context), IRegexStringFilterHost, IRegexStringIteratorEx, Cloneable {

    /**
     * 算法集合（正则表达式匹配）
     */
    private var algorithmCollection = TokenAlgorithmCollection(this, this)

    /**
     * 字符转换算法
     */
    private var tokenAlgorithm: ITokenAlgorithm? = null

    /**
     * 丢弃的类型集合
     */
    private val setDiscardToken = HashSet<TokenType>()

    /**
     * 记录当前的单词
     */
    protected var token: Token? = null

    /**
     * 上次位置
     */
    private var lastPosition = Position()

    override val isEOF: Boolean
        get() = token!!.type === TokenType.EOF

    init {
        initialize()
    }

    /**
     * 获取一个单词
     *
     * @return 单词
     */
    private fun scanInternal(): Token? {
        token = algorithmCollection.scan()
        return if (setDiscardToken.contains(token!!.type)) {// 需要丢弃
            null
        } else token
    }

    /**
     * 获取一个单词
     *
     * @return 单词
     */
    override fun scan() {
        do {
            token = scanInternal()
        } while (token == null)
        lastPosition = token!!.position
    }

    /**
     * 返回当前单词
     *
     * @return 当前单词
     */
    override fun token(): Token {
        return token!!
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
     * @throws RegexException 正则表达式异常
     */
    @Throws(RegexException::class)
    private fun initialize() {
        //
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
        algorithmCollection.attach(WhitespaceTokenizer())// 空白字符解析组件
        algorithmCollection.attach(CommentTokenizer())// 注释解析组件
        algorithmCollection.attach(Comment2Tokenizer())// 注释解析组件
        algorithmCollection.attach(MacroTokenizer())// 宏解析组件
        algorithmCollection.attach(OperatorTokenizer())// 操作符解析组件
        algorithmCollection.attach(StringTokenizer())// 字符串解析组件
        algorithmCollection.attach(CharacterTokenizer())// 字符解析组件
        algorithmCollection.attach(IdentifierTokenizer())// 标识符/关键字解析组件
        algorithmCollection.attach(NumberTokenizer())// 数字解析组件
    }

    override fun ex(): IRegexStringIteratorEx {
        return this
    }

    override fun saveToken() {

    }

    override fun lastPosition(): Position {
        return lastPosition
    }

    public override fun clone(): Any {
        val o = super<RegexStringIterator>.clone() as Lexer
        o.algorithmCollection = algorithmCollection.copy(o, o)
        return o
    }

    override fun tokenList(): List<Token> {
        throw NotImplementedError()
    }

    override fun getErrorSnapshot(position: Position): String {
        if (position.line < 0 || position.line >= arrLinesNo.size) {
            return ""
        }
        val str: String
        val start = arrLinesNo[position.line] + 1
        if (position.line == arrLinesNo.size - 1) {
            if (start < context.length)
                str = context.substring(start, Math.min(context.length, start + position.column))
            else
                str = ""
        } else {
            str = context.substring(start, arrLinesNo[position.line + 1])
        }
        if (position.column < 0 || position.column >= str.length) {
            val sb = StringBuilder()
            sb.append(str)
            sb.append(System.lineSeparator())
            for (i in 0 until str.length) {
                sb.append('^')
            }
            return sb.toString()
        } else {
            val sb = StringBuilder()
            sb.append(str)
            sb.append(System.lineSeparator())
            for (i in 0 until position.column - 1) {
                sb.append('^')
            }
            sb.append('^')
            return sb.toString()
        }
    }
}