package com.bajdcc.LALR1.interpret.test

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage
import com.bajdcc.LALR1.grammar.runtime.RuntimeException
import com.bajdcc.LALR1.grammar.runtime.RuntimeException.RuntimeError
import com.bajdcc.LALR1.interpret.Interpreter
import com.bajdcc.util.lexer.error.RegexException
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*

object TestInterpret2 {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val scanner = Scanner(System.`in`)
            val interpreter = Interpreter()
            var i = 0
            while (true) {
                print(">> ")
                val code = scanner.nextLine()
                try {
                    val grammar = Grammar(code)
                    // System.out.println(grammar.toString());
                    val page = grammar.codePage
                    // System.out.println(page.toString());
                    val baos = ByteArrayOutputStream()
                    RuntimeCodePage.exportFromStream(page, baos)
                    val bais = ByteArrayInputStream(
                            baos.toByteArray())
                    interpreter.run("test_" + i++, bais)
                } catch (e: RegexException) {
                    System.err.println(e.position.toString() + "," + e.message)
                    //e.printStackTrace();
                } catch (e: RuntimeException) {
                    System.err.println(e.position.toString() + ": " + e.info)
                    if (e.error === RuntimeError.EXIT) {
                        break
                    }
                    //e.printStackTrace();
                } catch (e: Exception) {
                    System.err.println(e.message)
                    //e.printStackTrace();
                }

            }
        } catch (e: Exception) {
            System.err.println(e.message)
            //e.printStackTrace();
        }

    }
}
