package com.bajdcc.util.lexer.algorithm.impl

import com.bajdcc.util.lexer.algorithm.TokenAlgorithm
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.token.MetaType
import com.bajdcc.util.lexer.token.OperatorType
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 操作符解析
 *
 * @author bajdcc
 */
class OperatorTokenizer @Throws(RegexException::class)
constructor() : TokenAlgorithm(regexString, null) {

    /**
     * 关键字哈希表
     */
    private val hashOperator = mutableMapOf<String, OperatorType>()

    override val greedMode: Boolean
        get() = true

    init {
        initializeHashMap()
    }

    /**
     * 初始化关键字哈希表
     */
    private fun initializeHashMap() {
        OperatorType.values().forEach { operator ->
            // 关键字
            hashOperator[operator.desc] = operator
        }
    }

    /*
	 * （非 Javadoc）
	 *
	 * @see
	 * com.bajdcc.lexer.algorithm.ITokenAlgorithm#getToken(java.lang.String,
	 * com.bajdcc.lexer.token.Token)
	 */
    override fun getToken(string: String, token: Token, iterator: IRegexStringIterator): Token? {
        token.type = TokenType.OPERATOR
        val op = hashOperator[string]
        if (op === OperatorType.MINUS) {
            if (iterator.available()) {
                val ch = iterator.current()
                if (ch == '.' || Character.isDigit(ch)) { // 判定是数字
                    return null
                }
            }
        }
        token.obj = op
        return token
    }

    companion object {

        val regexString: String
            get() {
                val metaTypes = arrayOf(MetaType.LPARAN, MetaType.RPARAN,
                        MetaType.STAR, MetaType.PLUS, MetaType.LSQUARE,
                        MetaType.RSQUARE, MetaType.LBRACE, MetaType.RBRACE,
                        MetaType.DOT, MetaType.BAR, MetaType.QUERY)
                val sb = StringBuilder()
                OperatorType.values().forEach { type ->
                    var op = type.desc
                    metaTypes.forEach { meta ->
                        op = op.replace(meta.char + "", "\\" + meta.char)
                    }
                    if (type === OperatorType.ESCAPE)
                        op += op
                    sb.append(op).append("|")
                }
                if (sb.isNotEmpty()) {
                    sb.deleteCharAt(sb.length - 1)
                }
                return sb.toString()
            }
    }
}
