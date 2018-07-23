package com.bajdcc.LALR1.syntax.lexer.tokenizer

import com.bajdcc.LALR1.syntax.token.OperatorType
import com.bajdcc.util.lexer.algorithm.TokenAlgorithm
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.token.MetaType
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 操作符解析
 *
 * @author bajdcc
 */
class OperatorTokenizer @Throws(RegexException::class)
constructor() : TokenAlgorithm(regexString, null) {

    override val greedMode: Boolean
        get() = true

    override fun getToken(string: String, token: Token, iterator: IRegexStringIterator): Token? {
        token.type = TokenType.OPERATOR
        token.obj = hashOperator[string]
        return token
    }

    companion object {

        /**
         * 关键字哈希表
         */
        private val hashOperator = mutableMapOf<String, OperatorType>()

        init {
            for (operator in OperatorType.values()) {// 关键字
                hashOperator[operator.desc] = operator
            }
        }

        val regexString: String
            get() {
                var exp = "->|\\||(|)|[|]|{|}|<|>"
                val metaTypes = arrayOf(MetaType.LPARAN, MetaType.RPARAN, MetaType.STAR, MetaType.PLUS, MetaType.LSQUARE, MetaType.RSQUARE, MetaType.LBRACE, MetaType.RBRACE, MetaType.DOT)
                for (meta in metaTypes) {
                    exp = exp.replace(meta.char + "", "\\" + meta.char)
                }
                return exp
            }
    }
}
