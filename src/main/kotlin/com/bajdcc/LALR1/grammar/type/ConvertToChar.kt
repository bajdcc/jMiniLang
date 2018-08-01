package com.bajdcc.LALR1.grammar.type

import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 【类型转换】转换为字符类型
 *
 * @author bajdcc
 */
class ConvertToChar : ITokenConventer {

    override fun convert(token: Token): Token {
        when (token.type) {
            TokenType.STRING, TokenType.DECIMAL, TokenType.INTEGER -> {
                token.obj = getCharValue(token)
                token.type = TokenType.CHARACTER
            }
            else -> {
            }
        }
        return token
    }

    /**
     * 强制字符转换（值）
     *
     * @param token 操作数
     * @return 转换结果
     */
    private fun getCharValue(token: Token): Char {
        when (token.type) {
            TokenType.STRING -> {
                val str = token.obj as String?
                return if (str!!.isEmpty()) '\u0000' else str[0]
            }
            TokenType.DECIMAL -> {
                val decimal = token.obj as Double
                return decimal.toChar()
            }
            TokenType.INTEGER -> {
                val integer = token.obj as Long
                return integer.toChar()
            }
            else -> {
            }
        }
        return '\u0000'
    }
}
