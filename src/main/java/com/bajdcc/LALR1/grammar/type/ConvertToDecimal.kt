package com.bajdcc.LALR1.grammar.type

import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 【类型转换】转换为定点数类型
 *
 * @author bajdcc
 */
class ConvertToDecimal : ITokenConventer {

    override fun convert(token: Token): Token {
        val type = token.type
        when (token.type) {
            TokenType.BOOL, TokenType.STRING, TokenType.DECIMAL, TokenType.INTEGER -> {
                token.obj = getDecimalValue(token)
                if (token.type === TokenType.ERROR) {
                    token.type = type
                } else {
                    token.type = TokenType.DECIMAL
                }
            }
            else -> {
            }
        }
        return token
    }

    /**
     * 强制定点数转换（值）
     *
     * @param token 操作数
     * @return 转换结果
     */
    private fun getDecimalValue(token: Token): Double {
        when (token.type) {
            TokenType.BOOL -> {
                val bool = token.obj as Boolean
                return if (bool) 1.0 else 0.0
            }
            TokenType.STRING -> {
                val str = token.obj as String?
                try {
                    return java.lang.Double.parseDouble(str!!)
                } catch (e: NumberFormatException) {
                    token.type = TokenType.ERROR
                }

            }
            TokenType.INTEGER -> {
                val integer = token.obj as Long
                return integer.toDouble()
            }
            TokenType.DECIMAL -> return token.obj as Double
            else -> {
            }
        }
        return 0.0
    }
}
