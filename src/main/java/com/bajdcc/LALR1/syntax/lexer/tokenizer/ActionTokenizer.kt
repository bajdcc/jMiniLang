package com.bajdcc.LALR1.syntax.lexer.tokenizer

import com.bajdcc.util.lexer.algorithm.TokenAlgorithm
import com.bajdcc.util.lexer.algorithm.filter.StringFilter
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.token.MetaType
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 动作解析
 *
 * @author bajdcc
 */
class ActionTokenizer @Throws(RegexException::class)
constructor() : TokenAlgorithm(regexString, StringFilter(MetaType.SHARP)) {

    override fun getToken(string: String, token: Token, iterator: IRegexStringIterator): Token? {
        token.type = TokenType.RESERVE
        token.obj = string
        return token
    }

    companion object {

        val regexString: String
            get() = "#.*#"
    }
}
