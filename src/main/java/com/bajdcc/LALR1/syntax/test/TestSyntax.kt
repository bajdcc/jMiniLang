package com.bajdcc.LALR1.syntax.test

import com.bajdcc.LALR1.syntax.Syntax
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.token.OperatorType
import com.bajdcc.util.lexer.token.TokenType

object TestSyntax {

    @JvmStatic
    fun main(args: Array<String>) {
        //System.out.println("Z -> `a`<,> | B | [`a` `b` Z B]");
        try {
            //Scanner scanner = new Scanner(System.in);
            val syntax = Syntax()
            syntax.addTerminal("PLUS", TokenType.OPERATOR, OperatorType.PLUS)
            syntax.addTerminal("MINUS", TokenType.OPERATOR, OperatorType.MINUS)
            syntax.addTerminal("TIMES", TokenType.OPERATOR, OperatorType.TIMES)
            syntax.addTerminal("DIVIDE", TokenType.OPERATOR, OperatorType.DIVIDE)
            syntax.addTerminal("LPA", TokenType.OPERATOR, OperatorType.LPARAN)
            syntax.addTerminal("RPA", TokenType.OPERATOR, OperatorType.RPARAN)
            syntax.addTerminal("SYMBOL", TokenType.ID, "i")
            syntax.addNonTerminal("E")
            syntax.addNonTerminal("T")
            syntax.addNonTerminal("F")
            //syntax.infer("E -> T `PLUS`<+> E | T `MINUS`<-> E | T");
            //syntax.infer("T -> F `TIMES`<*> T | F `DIVIDE`</> T | F");
            //syntax.infer("F -> `LPA`<(> E `RPA`<)>  | `SYMBOL`<i>");
            syntax.infer("E -> E @PLUS<+> T")
            syntax.infer("E -> E @MINUS<-> T")
            syntax.infer("E -> T")
            syntax.infer("T -> T @TIMES<*> F")
            syntax.infer("T -> T @DIVIDE</> F")
            syntax.infer("T -> F")
            syntax.infer("F -> @LPA<(> E @RPA<)>")
            syntax.infer("F -> @SYMBOL<i>")
            syntax.initialize("E")
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
