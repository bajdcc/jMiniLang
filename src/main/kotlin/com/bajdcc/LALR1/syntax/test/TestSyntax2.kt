package com.bajdcc.LALR1.syntax.test

import com.bajdcc.LALR1.syntax.Syntax
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.token.TokenType

object TestSyntax2 {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            //Scanner scanner = new Scanner(System.in);
            val syntax = Syntax()
            syntax.addTerminal("a", TokenType.ID, "a")
            syntax.addTerminal("b", TokenType.ID, "b")
            syntax.addNonTerminal("Z")
            syntax.addNonTerminal("S")
            syntax.addNonTerminal("B")
            syntax.infer("Z -> S")
            syntax.infer("S -> B B")
            syntax.infer("B -> @a B")
            syntax.infer("B -> @b")
            syntax.initialize("Z")
            println(syntax.toString())
            println(syntax.ngaString)
            println(syntax.npaString)
            //scanner.close();
        } catch (e: RegexException) {
            System.err.println(e.position.toString() + "," + e.message)
            e.printStackTrace()
        } catch (e: SyntaxException) {
            System.err.println(e.position.toString() + "," + e.message + " " + e.info)
            e.printStackTrace()
        }

    }
}
