package com.bajdcc.LALR1.syntax.lexer.tokenizer

import com.bajdcc.util.lexer.algorithm.TokenAlgorithm
import com.bajdcc.util.lexer.algorithm.filter.StringPairFilter
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.token.MetaType
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 数字解析
 *
 * @author bajdcc
 */
class NumberTokenizer @Throws(RegexException::class)
constructor() : TokenAlgorithm(regexString, StringPairFilter(MetaType.LSQUARE, MetaType.RSQUARE)) {

    override val greedMode: Boolean
        get() = true

    override fun getToken(string: String, token: Token, iterator: IRegexStringIterator): Token? {
        token.type = TokenType.INTEGER
        token.obj = Integer.parseInt(string)
        return token
    }

    companion object {

        val regexString: String
            get() = "-1|\\[\\d+\\]"
    }
}
