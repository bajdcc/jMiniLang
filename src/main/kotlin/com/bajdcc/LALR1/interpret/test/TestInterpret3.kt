package com.bajdcc.LALR1.interpret.test

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage
import com.bajdcc.LALR1.grammar.runtime.RuntimeException
import com.bajdcc.LALR1.interpret.Interpreter
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.lexer.error.RegexException

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object TestInterpret3 {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val codes = arrayOf("import \"sys.base\";\n"
                    + "var a = true;\n"
                    + "if (a) {call g_print(\"ok\");}\n"
                    + "else {call g_print(\"failed\");}", "import \"sys.base\";\n"
                    + "call g_print(\n"
                    + "    call (func~(a,b,c) -> call a(b,c))(\"g_max\",5,6));\n", "import \"sys.base\";\n"
                    + "var t = 0;\n"
                    + "for (var i = 0; i < 10; i++) {\n"
                    + "    if (i % 2 == 0) {\n"
                    + "        continue;\n"
                    + "    }\n"
                    + "    let t = t + i;\n"
                    + "}\n"
                    + "call g_print(t);\n", "import \"sys.base\";\n"
                    + "var enumerator = func ~(f, t, v) {\n"
                    + "    for (var i = f; i < t; i++) {\n"
                    + "        if (i % 2 == 0) {\n"
                    + "            continue;\n"
                    + "        }\n"
                    + "        call v(i);\n"
                    + "    }\n"
                    + "};\n"
                    + "var sum = 0;\n"
                    + "var set = func ~(v) {\n"
                    + "   let sum = sum + v;\n"
                    + "};\n"
                    + "call enumerator(0, 10, set);\n"
                    + "call g_print(sum);\n", "import \"sys.base\";" +
                    "var a=func~(){var f=func~()->call g_print(\"af\");call f();};" +
                    "var b=func~(){var f=func~()->call g_print(\"bf\");call f();};" +
                    "call a();call b();")

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
