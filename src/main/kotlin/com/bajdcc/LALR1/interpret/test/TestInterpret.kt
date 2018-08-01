package com.bajdcc.LALR1.interpret.test

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage
import com.bajdcc.LALR1.grammar.runtime.RuntimeException
import com.bajdcc.LALR1.interpret.Interpreter
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.lexer.error.RegexException

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object TestInterpret {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val codes = arrayOf("import \"sys.base\";" + "call g_print(call g_is_null(g_null));",

                    "import \"sys.base\";"
                            + "call g_print(\"Hello World!\\\n\");"
                            + "call g_print_err(\"Hello World!\\\n\");",

                    "import \"sys.base\";\n"
                            + "call g_print(\"请输入：\");\n"
                            + "call g_print(call g_stdin_read_line() + g_endl);\n"
                            + "call g_print(\"输入两个数字：\");\n"
                            + "call g_print(\n"
                            + "  \"较大数是：\" +"
                            + "  call g_to_string(\n"
                            + "    call g_max(call g_stdin_read_int(), call g_stdin_read_int()))\n"
                            + "    + \"\\n\"\n"
                            + ");\n"
                            + "call g_print(\"输入两个数字：\");\n"
                            + "call g_print(\n"
                            + "  \"较小数是：\" +"
                            + "  call g_to_string(\n"
                            + "    call g_min(call g_stdin_read_int(), call g_stdin_read_int()))\n"
                            + "    + \"\\n\"\n"
                            + ");\n",

                    "import \"sys.base\";\n"
                            + "call g_print(call g_doc(\"g_author\") + g_endl);\n"
                            + "call g_print(call g_doc(\"g_print\") + g_endl);\n"
                            + "call g_print(call g_doc(\"g_stdin_read_int\") + g_endl);\n"
                            + "call g_print(call g_doc(\"g_new\") + g_endl);\n"
                            + "call g_print(call g_to_string(call g_new(g_endl)));\n"
                            + "call g_print(call g_to_string(call g_new(5)));\n")//"import \"sys.base\";\n"
            //+ "var f = func ~(n) ->\n"
            //+ "    n <= 2 ? 1 : call f(n-1) + call f(n-2);"
            //+ "call g_print(call f(6));\n"
            //,
            //"import \"sys.base\";\n"
            //+ "call g_print(call g_load_func(\"g_max\"));\n"
            //,

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
