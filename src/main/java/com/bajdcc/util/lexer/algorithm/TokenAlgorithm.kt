package com.bajdcc.util.lexer.algorithm

import com.bajdcc.util.Position
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.regex.IRegexStringAttribute
import com.bajdcc.util.lexer.regex.IRegexStringFilter
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.regex.Regex
import com.bajdcc.util.lexer.token.MetaType
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

abstract class TokenAlgorithm @Throws(RegexException::class)
constructor(regex: String, filter: IRegexStringFilter?) : ITokenAlgorithm, IRegexStringAttribute {

    /**
     * 用来匹配的正则表达式
     */
    protected var regex: Regex = Regex(regex)

    /**
     * 匹配结果
     */
    override var result = ""

    /**
     * 字符过滤接口
     */
    override val regexStringFilter: IRegexStringFilter? = filter

    /**
     * 字符类型哈段表
     */
    final override var metaHash = mutableMapOf<Char, MetaType>()
        protected set

    override// 默认为非贪婪模式
    val greedMode: Boolean
        get() = false

    override val regexDescription: String
        get() = regex.context

    init {
        if (filter != null) {
            this.regex.filter = filter
            val metaTypes = filter.filterMeta.metaTypes
            metaTypes.forEach { metaType ->
                metaHash[metaType.char] = metaType
            }
        }
    }

    override fun accept(iterator: IRegexStringIterator, token: Token): Boolean {
        if (!iterator.available()) {
            token.type = TokenType.EOF
            return true
        }
        token.position = Position(iterator.position())
        iterator.snapshot()
        if (regex.match(iterator, this)) {// 匹配成功
            if (getToken(result, token, iterator) != null) {// 自动转换单词
                iterator.discard()
                return true
            }
        }
        iterator.restore()
        return false
    }
}
