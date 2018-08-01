package com.bajdcc.LALR1.semantic.test

import com.bajdcc.LALR1.grammar.semantic.ISemanticAction
import com.bajdcc.LALR1.grammar.semantic.ISemanticAnalyzer
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.symbol.IManageSymbol
import com.bajdcc.LALR1.grammar.symbol.IQuerySymbol
import com.bajdcc.LALR1.semantic.Semantic
import com.bajdcc.LALR1.semantic.token.IIndexedData
import com.bajdcc.LALR1.semantic.token.IRandomAccessOfTokens
import com.bajdcc.LALR1.syntax.handler.IErrorHandler
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.TrackerErrorBag
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.token.OperatorType
import com.bajdcc.util.lexer.token.TokenType

object TestOperator {

    @JvmStatic
    fun main(args: Array<String>) =// System.out.println("Z -> `a`<,> | B | [`a` `b` Z B]");
            try {
                // Scanner scanner = new Scanner(System.in);
                // String expr = "( i )";
                // String expr = "((3))+4*5+66/(3-8)";
                // String expr = "(4 * 7 - 13) * (3 + 18 / 3 - 3)";
                val expr = "(5 + 5)"
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
                semantic.addTerminal("INTEGER", TokenType.INTEGER, null)
                semantic.addNonTerminal("Z")
                semantic.addNonTerminal("E")
                semantic.addNonTerminal("T")
                semantic.addNonTerminal("F")
                semantic.addActionHandler("enter_paran", object : ISemanticAction {
                    override fun handle(indexed: IIndexedData, manage: IManageSymbol, access: IRandomAccessOfTokens, recorder: ISemanticRecorder) {
                        println("enter")
                    }
                })
                semantic.addActionHandler("leave_paran", object : ISemanticAction {
                    override fun handle(indexed: IIndexedData, manage: IManageSymbol, access: IRandomAccessOfTokens, recorder: ISemanticRecorder) {
                        println("leave")
                    }
                })
                semantic.addErrorHandler("lost_exp", object : IErrorHandler {
                    override fun handle(iterator: IRegexStringIterator, bag: TrackerErrorBag): String {
                        bag.read = false
                        bag.pass = true
                        return "表达式不完整"
                    }
                })
                semantic.addErrorHandler("lost_exp_right", object : IErrorHandler {
                    override fun handle(iterator: IRegexStringIterator, bag: TrackerErrorBag): String {
                        bag.read = false
                        bag.pass = true
                        return "缺少右括号"
                    }
                })
                val handleCopy = object : ISemanticAnalyzer {
                    override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                        return indexed[0].obj!!
                    }
                }
                val handleBinop = object : ISemanticAnalyzer {
                    override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                        val lop = Integer
                                .parseInt(indexed[0].obj!!.toString())
                        val rop = Integer
                                .parseInt(indexed[2].obj!!.toString())
                        val op = indexed[1].token
                        if (op!!.type === TokenType.OPERATOR) {
                            val kop = op!!.obj as OperatorType
                            when (kop) {
                                OperatorType.PLUS -> return lop + rop
                                OperatorType.MINUS -> return lop - rop
                                OperatorType.TIMES -> return lop * rop
                                OperatorType.DIVIDE -> return if (rop == 0) {
                                    lop
                                } else {
                                    lop / rop
                                }
                                else -> return 0
                            }
                        } else {
                            return 0
                        }
                    }
                }
                val handleValue = object : ISemanticAnalyzer {
                    override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                        return indexed[0].token!!.obj!!
                    }
                }
                // syntax.infer("E -> T `PLUS`<+> E | T `MINUS`<-> E | T");
                // syntax.infer("T -> F `TIMES`<*> T | F `DIVIDE`</> T | F");
                // syntax.infer("F -> `LPA`<(> E `RPA`<)>  | `SYMBOL`<i>");
                semantic.infer(handleCopy, "Z -> E[0]")
                semantic.infer(handleCopy, "E -> T[0]")
                semantic.infer(handleBinop,
                        "E -> E[0] ( @PLUS[1]<+> | @MINUS[1]<-> ) T[2]{lost_exp}")
                semantic.infer(handleCopy, "T -> F[0]")
                semantic.infer(handleBinop,
                        "T -> T[0] ( @TIMES[1]<*> | @DIVIDE[1]</> ) F[2]{lost_exp}")
                semantic.infer(handleValue, "F -> @INTEGER[0]<integer>")
                semantic.infer(handleCopy,
                        "F -> @LPA#enter_paran#<(> E[0]{lost_exp} @RPA#leave_paran#{lost_exp_right}<)>")
                semantic.initialize("Z")
                // System.out.println(semantic.toString());
                // System.out.println(semantic.getNGAString());
                // System.out.println(semantic.getNPAString());
                // System.out.println(semantic.getInst());
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
