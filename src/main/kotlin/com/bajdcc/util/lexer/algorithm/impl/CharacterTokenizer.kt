package com.bajdcc.util.lexer.algorithm.impl

import com.bajdcc.util.lexer.algorithm.TokenAlgorithm
import com.bajdcc.util.lexer.algorithm.filter.CharacterFilter
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 字符解析
 *
 * @author bajdcc
 */
class CharacterTokenizer @Throws(RegexException::class)
constructor() : TokenAlgorithm(regexString, CharacterFilter()) {

    /* （非 Javadoc）
	 * @see com.bajdcc.lexer.algorithm.ITokenAlgorithm#getToken(java.lang.String, com.bajdcc.lexer.token.Token)
	 */
    override fun getToken(string: String, token: Token, iterator: IRegexStringIterator): Token {
        token.type = TokenType.CHARACTER
        token.obj = string[0]
        return token
    }

    companion object {

        val regexString: String
            get() = "\'.\'"
    }
}
