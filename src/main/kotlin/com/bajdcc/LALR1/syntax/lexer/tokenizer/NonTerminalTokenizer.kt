package com.bajdcc.LALR1.syntax.lexer.tokenizer

import com.bajdcc.util.lexer.algorithm.TokenAlgorithm
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 非终结符解析
 *
 * @author bajdcc
 */
class NonTerminalTokenizer @Throws(RegexException::class)
constructor() : TokenAlgorithm(regexString, null) {

    override val greedMode: Boolean
        get() = true

    override fun getToken(string: String, token: Token, iterator: IRegexStringIterator): Token? {
        token.type = TokenType.ID
        token.obj = string
        return token
    }

    companion object {

        val regexString: String
            get() = "(\\a|_)\\w*"
    }
}
