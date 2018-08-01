package com.bajdcc.LALR1.semantic.test

import com.bajdcc.LALR1.grammar.semantic.ISemanticAction
import com.bajdcc.LALR1.grammar.semantic.ISemanticAnalyzer
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.symbol.IManageSymbol
import com.bajdcc.LALR1.grammar.symbol.IQuerySymbol
import com.bajdcc.LALR1.semantic.Semantic
import com.bajdcc.LALR1.semantic.token.IIndexedData
import com.bajdcc.LALR1.semantic.token.IRandomAccessOfTokens
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.token.TokenType

object TestSemantic3 {

    @JvmStatic
    fun main(args: Array<String>) {
        // System.out.println("Z -> `a`<,> | B | [`a` `b` Z B]");
        try {
            // Scanner scanner = new Scanner(System.in);
            // String expr = "( i )";
            val expr = "a a a a"
            val semantic = Semantic(expr)
            semantic.addTerminal("a", TokenType.ID, "a")
            semantic.addTerminal("b", TokenType.EPSILON, null)
            semantic.addNonTerminal("START")
            semantic.addNonTerminal("Z")
            val handleValue = object : ISemanticAnalyzer {
                override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                    return 1
                }
            }
            val handleCopy = object : ISemanticAnalyzer {
                override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                    return indexed.get(0).obj!!
                }
            }
            val handleRec = object : ISemanticAnalyzer {
                override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                    val lop = Integer
                            .parseInt(indexed.get(0).obj!!.toString())
                    return lop + 1
                }
            }
            val action = object : ISemanticAction {
                override fun handle(indexed: IIndexedData, manage: IManageSymbol, access: IRandomAccessOfTokens, recorder: ISemanticRecorder) {
                    println("ok")
                }
            }
            // syntax.infer("E -> T `PLUS`<+> E | T `MINUS`<-> E | T");
            // syntax.infer("T -> F `TIMES`<*> T | F `DIVIDE`</> T | F");
            // syntax.infer("F -> `LPA`<(> E `RPA`<)>  | `SYMBOL`<i>");
            semantic.addActionHandler("Action", action)
            semantic.infer(handleCopy, "START -> Z[0]")
            semantic.infer(handleRec, "Z -> @a[1] Z[0]#Action# @b")
            semantic.infer(handleValue, "Z -> @a[0]")
            semantic.initialize("START")
            println(semantic.toString())
            println(semantic.ngaString)
            println(semantic.npaString)
            semantic.parse()
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
