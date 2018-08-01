package com.bajdcc.LALR1.semantic.lexer

import com.bajdcc.util.lexer.Lexer
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.token.Token

/**
 * 词法分析器
 *
 * @author bajdcc
 */
class TokenFactory @Throws(RegexException::class)
constructor(context: String) : Lexer(context) {

    /**
     * 保存当前分析的单词流
     */
    private var arrTokens = mutableListOf<Token>()

    override fun copy(): IRegexStringIterator {
        return super.clone() as TokenFactory
    }

    override fun tokenList(): List<Token> {
        return arrTokens
    }

    override fun saveToken() {
        arrTokens.add(super.token!!)
    }
}
