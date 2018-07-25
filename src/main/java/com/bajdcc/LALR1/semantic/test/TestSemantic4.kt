package com.bajdcc.LALR1.semantic.test

import com.bajdcc.LALR1.semantic.Semantic
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.token.TokenType

object TestSemantic4 {

    @JvmStatic
    fun main(args: Array<String>) {
        // System.out.println("Z -> `a`<,> | B | [`a` `b` Z B]");
        try {
            // Scanner scanner = new Scanner(System.in);
            // String expr = "( i )";
            val expr = "a b c a"
            val semantic = Semantic(expr)
            semantic.addTerminal("a", TokenType.ID, "a")
            semantic.addTerminal("b", TokenType.ID, "b")
            semantic.addTerminal("c", TokenType.ID, "c")
            semantic.addNonTerminal("START")
            // syntax.infer("E -> T `PLUS`<+> E | T `MINUS`<-> E | T");
            // syntax.infer("T -> F `TIMES`<*> T | F `DIVIDE`</> T | F");
            // syntax.infer("F -> `LPA`<(> E `RPA`<)>  | `SYMBOL`<i>");
            semantic.infer("START -> @a [@b] [@c] @a")
            semantic.initialize("START")
            println(semantic.toString())
            println(semantic.ngaString)
            println(semantic.npaString)
            println(semantic.inst)
            println(semantic.trackerError)
            println(semantic.tokenList)
            println(semantic.getObject())
            // scanner.close();
        } catch (e: RegexException) {
            System.err.println(e.position.toString() + "," + e.message)
            e.printStackTrace()
        } catch (e: SyntaxException) {
            System.err.println(e.position.toString() + "," + e.message + " "
                    + e.info)
            e.printStackTrace()
        }

    }
}
