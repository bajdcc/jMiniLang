package com.bajdcc.LALR1.syntax.lexer.tokenizer

import com.bajdcc.util.lexer.algorithm.TokenAlgorithm
import com.bajdcc.util.lexer.algorithm.filter.StringPairFilter
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.token.MetaType
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 注释解析
 *
 * @author bajdcc
 */
class CommentTokenizer @Throws(RegexException::class)
constructor() : TokenAlgorithm(regexString, StringPairFilter(MetaType.LT, MetaType.GT)) {

    override fun getToken(string: String, token: Token, iterator: IRegexStringIterator): Token? {
        token.type = TokenType.COMMENT
        token.obj = string.trim { it <= ' ' }
        return token
    }

    companion object {

        val regexString: String
            get() = "<.*>"
    }
}
