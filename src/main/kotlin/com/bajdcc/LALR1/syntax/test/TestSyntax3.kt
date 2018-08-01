package com.bajdcc.LALR1.syntax.test

import com.bajdcc.LALR1.syntax.Syntax
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.token.TokenType

object TestSyntax3 {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            //Scanner scanner = new Scanner(System.in);
            val syntax = Syntax()
            syntax.addTerminal("a", TokenType.ID, "a")
            syntax.addTerminal("c", TokenType.ID, "c")
            syntax.addTerminal("d", TokenType.ID, "d")
            syntax.addNonTerminal("S")
            syntax.addNonTerminal("A")
            syntax.infer("S -> @c A @d")
            syntax.infer("A -> @a")
            syntax.infer("A -> A @a")
            syntax.initialize("S")
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
