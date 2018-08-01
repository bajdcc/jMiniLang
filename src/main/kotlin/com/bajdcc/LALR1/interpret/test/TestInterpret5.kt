package com.bajdcc.LALR1.interpret.test

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage
import com.bajdcc.LALR1.grammar.runtime.RuntimeException
import com.bajdcc.LALR1.interpret.Interpreter
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.lexer.error.RegexException

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object TestInterpret5 {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val codes = arrayOf(

                    "import \"sys.base\";\n"
                            + "var to_list = func ~(a, b, c) {\n"
                            + "    foreach (var i : call a(b, c)) {\n"
                            + "        call g_printn(i);\n"
                            + "    }\n"
                            + "};\n"
                            + "call to_list(\"g_range\", 1, 100);\n"
                            + "//call g_range(1, 6);\n"
                            + "\n",

                    "import \"sys.base\";\n"
                            + "var a = 1;"
                            + "if (a == 1) {"
                            + "    call g_printn('x');"
                            + "} else if (a == 2) {"
                            + "    call g_printn('y');"
                            + "}"
                            + "\n")

            val interpreter = Interpreter()
            val grammar = Grammar(codes[codes.size - 1])
            println(grammar.toString())
            val page = grammar.codePage
            println(page.toString())
            val baos = ByteArrayOutputStream()
            RuntimeCodePage.exportFromStream(page, baos)
            val bais = ByteArrayInputStream(baos.toByteArray())
            interpreter.run("test_1", bais)

        } catch (e: RegexException) {
            System.err.println()
            System.err.println(e.position.toString() + "," + e.message)
            e.printStackTrace()
        } catch (e: SyntaxException) {
            System.err.println()
            System.err.println(e.position.toString() + "," + e.message + " "
                    + e.info)
            e.printStackTrace()
        } catch (e: RuntimeException) {
            System.err.println()
            System.err.println(e.position.toString() + ": " + e.info)
            e.printStackTrace()
        } catch (e: Exception) {
            System.err.println()
            System.err.println(e.message)
            e.printStackTrace()
        }

    }
}
