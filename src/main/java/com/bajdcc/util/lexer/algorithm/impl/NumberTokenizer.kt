package com.bajdcc.util.lexer.algorithm.impl

import com.bajdcc.util.lexer.algorithm.TokenAlgorithm
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

import java.math.BigDecimal


/**
 * 数字解析
 *
 * @author bajdcc
 */
class NumberTokenizer @Throws(RegexException::class)
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
        if (string.startsWith("0x")) {
            try {
                token.obj = java.lang.Long.parseLong(string.substring(2).toLowerCase(), 0x10)
                token.type = TokenType.INTEGER
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                return null
            }
        } else {
            try {
                val decimal = BigDecimal(string)
                token.obj = decimal
                if (string.indexOf('.') == -1) {
                    token.obj = decimal.toBigIntegerExact().toLong()
                    token.type = TokenType.INTEGER
                } else {
                    token.obj = decimal.toDouble()
                    token.type = TokenType.DECIMAL
                }
            } catch (e: ArithmeticException) {
                e.printStackTrace()
                return null
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                return null
            }
        }
        return token
    }

    companion object {

        val regexString: String
            get() = "[+-]?(\\d*\\.?\\d+|\\d+\\.?\\d*|0x[0-9ABCDEFabcdef]{1,4})([eE][+-]?\\d+)?"
    }
}
