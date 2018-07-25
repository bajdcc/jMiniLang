package com.bajdcc.LALR1.grammar.type

import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 【类型转换】转换为字符串类型
 *
 * @author bajdcc
 */
class ConvertToString : ITokenConventer {

    override fun convert(token: Token): Token {
        token.type = TokenType.STRING
        token.obj = token.obj.toString()
        return token
    }
}
