package com.bajdcc.LALR1.grammar.type

import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 【类型转换】转换为布尔类型
 *
 * @author bajdcc
 */
class ConvertToBoolean : ITokenConventer {

    override fun convert(token: Token): Token {
        when (token.type) {
            TokenType.CHARACTER, TokenType.STRING, TokenType.DECIMAL, TokenType.INTEGER -> {
                token.obj = getBooleanValue(token)
                token.type = TokenType.BOOL
            }
            else -> {
            }
        }
        return token
    }

    /**
     * 强制布尔转换（值）
     *
     * @param token 操作数
     * @return 转换结果
     */
    private fun getBooleanValue(token: Token): Boolean {
        when (token.type) {
            TokenType.BOOL -> return token.obj as Boolean
            TokenType.CHARACTER -> {
                val ch = token.obj as Char
                return ch.toInt() != 0
            }
            TokenType.STRING -> return true
            TokenType.DECIMAL -> {
                val decimal = token.obj as Double
                return decimal != 0.0
            }
            TokenType.INTEGER -> {
                val integer = token.obj as Long
                return integer != 0L
            }
            else -> {
            }
        }
        return false
    }
}
