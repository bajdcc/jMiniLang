package com.bajdcc.LALR1.interpret.test

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage
import com.bajdcc.LALR1.grammar.runtime.RuntimeException
import com.bajdcc.LALR1.interpret.Interpreter
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.lexer.error.RegexException

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object TestInterpret11 {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val codes = arrayOf("import \"sys.base\";\n" +
                    "import \"sys.func\";\n" +
                    "import \"sys.list\";\n" +
                    "import \"sys.string\";\n" +
                    "\n" +
                    "var a = call g_array_range(1, 10);\n" +
                    "var b = call g_func_apply(\"g_func_add\", a);\n" +
                    "call g_printn(b);\n" +
                    "var b1 = call g_func_length(a);\n" +
                    "call g_printn(b1);\n" +
                    "var c = call g_func_apply(\"g_func_add\", a);\n" +
                    "call g_printn(c);\n" +
                    "var c1 = call g_func_map(a, \"g_to_string\");\n" +
                    "var c2 = call g_func_applyr(\"g_func_add\", c1);\n" +
                    "call g_printn(c2);\n" +
                    "let c1 = call g_func_mapr(a, \"g_to_string\");\n" +
                    "let c2 = call g_func_apply(\"g_func_add\", c1);\n" +
                    "call g_printn(c2);\n" +
                    "var c3 = call g_func_applyr(\"g_func_sub\", a);\n" +
                    "call g_printn(c3);\n" +
                    "var f4 = func ~(x) -> x % 2 == 0;\n" +
                    "var c4 = call g_func_filter(a, f4);\n" +
                    "let c4 = call g_func_map(c4, \"g_to_string\");\n" +
                    "let c4 = call g_func_apply(\"g_func_add\", c4);\n" +
                    "call g_printn(c4);\n" +
                    "var c5 = call g_func_take(c1, 5);\n" +
                    "let c5 = call g_func_apply(\"g_func_add\", c5);\n" +
                    "call g_printn(c5);\n" +
                    "let c5 = call g_func_taker(c1, 5);\n" +
                    "let c5 = call g_func_apply(\"g_func_add\", c5);\n" +
                    "call g_printn(c5);\n" +
                    "let c5 = call g_func_drop(c1, 5);\n" +
                    "let c5 = call g_func_apply(\"g_func_add\", c5);\n" +
                    "call g_printn(c5);\n" +
                    "let c5 = call g_func_dropr(c1, 5);\n" +
                    "let c5 = call g_func_apply(\"g_func_add\", c5);\n" +
                    "call g_printn(c5);\n" +
                    "var c6 = call g_func_zip(\"g_func_mul\" ,a, a);\n" +
                    "let c6 = call g_func_map(c6, \"g_to_string\");\n" +
                    "let c6 = call g_func_apply(\"g_func_add\", c6);\n" +
                    "call g_printn(c6);\n" +
                    "var d = call g_func_apply_arg(\"g_func_add\", call g_string_split(\"12345\", \"\"), \"g_func_swap\");\n" +
                    "call g_printn(d);\n" +
                    "call g_func_import_string_module();\n" +
                    "var e = call g_func_applicative(\"g_func_eq\", \"12321\", \"g_string_reverse\");\n" +
                    "call g_printn(e);\n" +
                    "call g_printn(call g_doc(\"g_func_fold\"));\n" +
                    "var xx = func ~(l) {\n" +
                    "    var idx = call g_array_size(l) - 1;\n" +
                    "    var _xsr = func ~() ->\n" +
                    "        idx < 0 ? g_null : call g_array_get(l, idx--);\n" +
                    "    return _xsr;\n" +
                    "};\n" +
                    "var x1 = call xx(a);\n" +
                    "var x2 = call x1();\n" +
                    "while (!call g_is_null(x2)) {\n" +
                    "    call g_printn(x2);\n" +
                    "    let x2 = call x1();\n" +
                    "}\n" +
                    "")

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
