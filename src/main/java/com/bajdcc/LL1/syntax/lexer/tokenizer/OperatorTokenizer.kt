package com.bajdcc.LL1.syntax.lexer.tokenizer

import com.bajdcc.LL1.syntax.token.OperatorType
import com.bajdcc.util.lexer.algorithm.TokenAlgorithm
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.regex.IRegexStringIterator
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
        for (operator in OperatorType.values()) {// 关键字
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
        token.obj = hashOperator[string]
        return token
    }

    companion object {

        val regexString: String
            get() = "->|\\||<|>"
    }
}
