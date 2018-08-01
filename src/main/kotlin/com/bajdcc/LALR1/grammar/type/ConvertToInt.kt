package com.bajdcc.LALR1.grammar.type

import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 【类型转换】转换为整数类型
 *
 * @author bajdcc
 */
class ConvertToInt : ITokenConventer {

    override fun convert(token: Token): Token {
        val type = token.type
        when (token.type) {
            TokenType.BOOL, TokenType.CHARACTER, TokenType.STRING, TokenType.DECIMAL, TokenType.INTEGER -> {
                token.obj = getIntValue(token)
                if (token.type === TokenType.ERROR) {
                    token.type = type
                } else {
                    token.type = TokenType.INTEGER
                }
            }
            else -> {
            }
        }
        return token
    }

    /**
     * 强制整数转换（值）
     *
     * @param token 操作数
     * @return 转换结果
     */
    private fun getIntValue(token: Token): Long {
        when (token.type) {
            TokenType.BOOL -> {
                val bool = token.obj as Boolean
                return if (bool) 1L else 0L
            }
            TokenType.CHARACTER -> {
                val ch = token.obj as Char
                return ch.toLong()
            }
            TokenType.STRING -> {
                val str = token.obj as String?
                try {
                    return java.lang.Long.parseLong(str!!)
                } catch (e: NumberFormatException) {
                    token.type = TokenType.ERROR
                }

            }
            TokenType.INTEGER -> return token.obj as Long
            TokenType.DECIMAL -> {
                val decimal = token.obj as Double
                return decimal.toLong()
            }
            else -> {
            }
        }
        return 0L
    }
}
