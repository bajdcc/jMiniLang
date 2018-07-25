package com.bajdcc.LALR1.syntax.test

import com.bajdcc.LALR1.syntax.lexer.SyntaxLexer
import com.bajdcc.LALR1.syntax.token.Token
import com.bajdcc.LALR1.syntax.token.TokenType
import com.bajdcc.util.lexer.error.RegexException
import java.util.*

object TestSyntaxLexer {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val scanner = Scanner(System.`in`)
            val str = scanner.nextLine()
            val lexer = SyntaxLexer()
            lexer.context = str
            var token: Token?
            while (true) {
                token = lexer.nextToken()
                if (token!!.type === TokenType.EOF) {
                    break
                }
                println(token!!.toString())
            }
            scanner.close()
        } catch (e: RegexException) {
            System.err.println(e.position.toString() + "," + e.message)
            e.printStackTrace()
        }

    }
}
