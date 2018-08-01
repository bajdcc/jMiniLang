package com.bajdcc.LALR1.semantic.test

import com.bajdcc.LALR1.semantic.Semantic
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.token.OperatorType
import com.bajdcc.util.lexer.token.TokenType

object TestSemantic {

    @JvmStatic
    fun main(args: Array<String>) {
        // System.out.println("Z -> `a`<,> | B | [`a` `b` Z B]");
        try {
            // Scanner scanner = new Scanner(System.in);
            // String expr = "( i )";
            val expr = "((i))+i*i+i/(i-i)"
            val semantic = Semantic(expr)
            semantic.addTerminal("PLUS", TokenType.OPERATOR, OperatorType.PLUS)
            semantic.addTerminal("MINUS", TokenType.OPERATOR,
                    OperatorType.MINUS)
            semantic.addTerminal("TIMES", TokenType.OPERATOR,
                    OperatorType.TIMES)
            semantic.addTerminal("DIVIDE", TokenType.OPERATOR,
                    OperatorType.DIVIDE)
            semantic.addTerminal("LPA", TokenType.OPERATOR, OperatorType.LPARAN)
            semantic.addTerminal("RPA", TokenType.OPERATOR, OperatorType.RPARAN)
            semantic.addTerminal("SYMBOL", TokenType.ID, "i")
            semantic.addNonTerminal("Z")
            semantic.addNonTerminal("E")
            semantic.addNonTerminal("T")
            semantic.addNonTerminal("F")
            // syntax.infer("E -> T `PLUS`<+> E | T `MINUS`<-> E | T");
            // syntax.infer("T -> F `TIMES`<*> T | F `DIVIDE`</> T | F");
            // syntax.infer("F -> `LPA`<(> E `RPA`<)>  | `SYMBOL`<i>");
            semantic.infer("Z -> E")
            semantic.infer("E -> T")
            semantic.infer("E -> E @PLUS<+> T")
            semantic.infer("E -> E @MINUS<-> T")
            semantic.infer("T -> F")
            semantic.infer("T -> T @TIMES<*> F")
            semantic.infer("T -> T @DIVIDE</> F")
            semantic.infer("F -> @SYMBOL<i>")
            semantic.infer("F -> @LPA<(> E @RPA<)>")
            semantic.initialize("Z")
            println(semantic.toString())
            println(semantic.ngaString)
            println(semantic.npaString)
            println(semantic.inst)
            println(semantic.trackerError)
            println(semantic.tokenList)
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
