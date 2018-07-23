package com.bajdcc.LALR1.syntax.token

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
    var type = TokenType.ERROR

    /**
     * 数据
     */
    var obj: Any? = null

    /**
     * 位置
     */
    var position = Position()

    override fun toString(): String {
        return String.format("%04d,%03d: %s %s", position.line,
                position.column, type.desc,
                obj?.toString() ?: "(null)")
    }

    companion object {

        fun transfer(token: LexerToken): Token {
            val tk = Token()
            tk.obj = token.obj
            tk.position = token.position
            when (token.type) {
                LexerTokenType.COMMENT -> tk.type = TokenType.COMMENT
                LexerTokenType.EOF -> tk.type = TokenType.EOF
                LexerTokenType.ERROR -> tk.type = TokenType.ERROR
                LexerTokenType.ID -> tk.type = TokenType.NONTERMINAL
                LexerTokenType.MACRO -> tk.type = TokenType.HANDLER
                LexerTokenType.OPERATOR -> tk.type = TokenType.OPERATOR
                LexerTokenType.STRING -> tk.type = TokenType.TERMINAL
                LexerTokenType.WHITESPACE -> tk.type = TokenType.WHITESPACE
                LexerTokenType.INTEGER -> tk.type = TokenType.STORAGE
                LexerTokenType.RESERVE -> tk.type = TokenType.ACTION
                else -> {
                }
            }
            return tk
        }
    }
}