package com.bajdcc.LL1.syntax.lexer.tokenizer

import com.bajdcc.util.lexer.algorithm.TokenAlgorithm
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 终结符解析
 *
 * @author bajdcc
 */
class TerminalTokenizer @Throws(RegexException::class)
constructor() : TokenAlgorithm(regexString, null) {

    override val greedMode: Boolean
        get() = true

    /*
	 * （非 Javadoc）
	 *
	 * @see
	 * com.bajdcc.lexer.algorithm.ITokenAlgorithm#getToken(java.lang.String,
	 * com.bajdcc.lexer.token.Token)
	 */
    override fun getToken(string: String, token: Token, iterator: IRegexStringIterator): Token? {
        token.type = TokenType.STRING
        token.obj = string.substring(1)
        return token
    }

    companion object {

        val regexString: String
            get() = "@(\\a|_)\\w*"
    }
}
