package com.bajdcc.OP.syntax.token

import com.bajdcc.util.Position
import com.bajdcc.util.lexer.token.Token as LexerToken
import com.bajdcc.util.lexer.token.TokenType as LexerTokenType

/**
 * 单词
 *
 * @author bajdcc
 */
class Token {

    /**
     * 单词类型
     */
    var kToken = TokenType.ERROR

    /**
     * 数据
     */
    var `object`: Any? = null

    /**
     * 位置
     */
    var position = Position()

    override fun toString(): String {
        return String.format("%04d,%03d: %s %s", position.line,
                position.column, kToken.desc,
                if (`object` == null) "(null)" else `object`!!.toString())
    }

    companion object {

        fun transfer(token: LexerToken): Token {
            val tk = Token()
            tk.`object` = token.obj
            tk.position = token.position
            when (token.type) {
                LexerTokenType.COMMENT -> tk.kToken = TokenType.COMMENT
                LexerTokenType.EOF -> tk.kToken = TokenType.EOF
                LexerTokenType.ERROR -> tk.kToken = TokenType.ERROR
                LexerTokenType.ID -> tk.kToken = TokenType.NONTERMINAL
                LexerTokenType.OPERATOR -> tk.kToken = TokenType.OPERATOR
                LexerTokenType.STRING -> tk.kToken = TokenType.TERMINAL
                LexerTokenType.WHITESPACE -> tk.kToken = TokenType.WHITSPACE
                else -> {
                }
            }
            return tk
        }
    }
}