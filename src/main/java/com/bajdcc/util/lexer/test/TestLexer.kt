package com.bajdcc.util.lexer.test

import com.bajdcc.util.lexer.Lexer
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType
import java.util.*

object TestLexer {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val scanner = Scanner(System.`in`)
            val str = scanner.nextLine()
            scanner.close()
            val lexer = Lexer(str)
            var token: Token
            while (true) {
                lexer.scan()
                token = lexer.token()
                if (token.type === TokenType.EOF) {
                    break
                }
                println(token.toString())
            }
        } catch (e: RegexException) {
            System.err.println(e.position.toString() + "," + e.message)
            e.printStackTrace()
        }

    }
}
