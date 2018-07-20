package com.bajdcc.util.lexer.algorithm.impl

import com.bajdcc.util.lexer.algorithm.TokenAlgorithm
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 注释解析
 * 注：不将两种注释形式分开的话，由于正则表达式只支持整体的贪婪或非贪婪模式
 * 因此会将下面这种注释看作贪婪的，所以会导致匹配过度
 *
 * @author bajdcc
 */
class Comment2Tokenizer @Throws(RegexException::class)
constructor() : TokenAlgorithm(regexString, null) {

    override val greedMode: Boolean
        get() = false

    /*
	 * （非 Javadoc）
	 *
	 * @see
	 * com.bajdcc.lexer.algorithm.ITokenAlgorithm#getToken(java.lang.String,
	 * com.bajdcc.lexer.token.Token)
	 */
    override fun getToken(string: String, token: Token, iterator: IRegexStringIterator): Token {
        token.type = TokenType.COMMENT
        token.obj = string.trim { it <= ' ' }
        return token
    }

    companion object {

        val regexString: String
            get() = "/\\*.*\\*/"
    }
}
