package com.bajdcc.util.lexer.algorithm.impl

import com.bajdcc.util.lexer.algorithm.TokenAlgorithm
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.token.KeywordType
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 标识符/关键字解析
 *
 * @author bajdcc
 */
class IdentifierTokenizer @Throws(RegexException::class)
constructor() : TokenAlgorithm(regexString, null) {

    /**
     * 关键字的哈希表
     */
    private val mapKeywords = mutableMapOf<String, KeywordType>()

    override val greedMode: Boolean
        get() = true

    init {
        initKeywords()
    }

    /**
     * 初始化关键字哈希表
     */
    private fun initKeywords() {
        KeywordType.values().forEach { keyword ->
            // 关键字
            mapKeywords[keyword.desc] = keyword
        }
    }

    /*
	 * （非 Javadoc）
	 *
	 * @see
	 * com.bajdcc.lexer.algorithm.ITokenAlgorithm#getToken(java.lang.String,
	 * com.bajdcc.lexer.token.Token)
	 */
    override fun getToken(string: String, token: Token, iterator: IRegexStringIterator): Token {
        if (mapKeywords.containsKey(string)) {
            val kw = mapKeywords[string]
            when (kw) {
                KeywordType.TRUE -> {
                    token.type = TokenType.BOOL
                    token.obj = true
                }
                KeywordType.FALSE -> {
                    token.type = TokenType.BOOL
                    token.obj = false
                }
                else -> {
                    token.type = TokenType.KEYWORD
                    token.obj = kw
                }
            }
        } else {
            token.type = TokenType.ID
            token.obj = string
        }
        return token
    }

    companion object {

        val regexString: String
            get() = "(\\a|_)\\w*"
    }
}
