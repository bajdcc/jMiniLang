package com.bajdcc.LALR1.interpret.test

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage
import com.bajdcc.LALR1.grammar.runtime.RuntimeException
import com.bajdcc.LALR1.interpret.Interpreter
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.lexer.error.RegexException

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object TestInterpret7 {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val codes = arrayOf(

                    "import \"sys.base\";\n"
                            + "var f = func ~(a) -> a;\n"
                            + "call g_printn(call g_get_type(f));"
                            + "\n",

                    "import \"sys.base\";\n" +
                            "//a var i = 0;\n" +
                            "//let i=7;\n" +
                            "var i=0;\n" +
                            "//for(;i<10;i++){\n" +
                            "//call g_printn(\"\" + i);\n" +
                            "//}\n" +
                            "while (i < 10) {call g_printn(\"\"+ ++i);}",

                    "import \"sys.base\";\n" +
                            "var move = func ~(i, x, y) {\n" +
                            "    call g_printn(\"\" + i + \": \" + x + \" -> \" + y);\n" +
                            "};\n" +
                            "var hanoi = func ~(f) {\n" +
                            "    var fk = func ~(i, a, b, c) {\n" +
                            "        if (i == 1) {\n" +
                            "            call move(i, a, c);\n" +
                            "        } else {\n" +
                            "            call f(i - 1, a, c, b);\n" +
                            "            call move(i, a, c);\n" +
                            "            call f(i - 1, b, a, c);\n" +
                            "        }\n" +
                            "    };\n" +
                            "    return fk;\n" +
                            "};\n" +
                            "var h = call (func ~(f) {\n" +
                            "    var fx = func ~(x) {\n" +
                            "        var fn = func ~(i, a, b, c) {\n" +
                            "            var vf = call f(call x(x));\n" +
                            "            return call vf(i, a, b, c);\n" +
                            "        };\n" +
                            "        return fn;\n" +
                            "    };\n" +
                            "    return call (func ~(h) -> call h(h))(fx);\n" +
                            "})(hanoi);\n" +
                            "call h(3, 'A', 'B', 'C');\n" +
                            "\n",

                    "import \"sys.base\";\n" +
                            "call g_printn(\"Trampoline example:\");\n" +
                            "var repeat = func ~(f) {\n" +
                            "    var repeat0 = func ~(operation, count) {\n" +
                            "        var k = func ~() {\n" +
                            "            if (count <= 0) { return; }\n" +
                            "            call operation();\n" +
                            "            return call f(operation, --count);\n" +
                            "        };\n" +
                            "        return k;\n" +
                            "    };\n" +
                            "    return repeat0;\n" +
                            "};\n" +
                            "var REPEAT = call (func ~(f) {\n" +
                            "    var fx = func ~(x) {\n" +
                            "        var fn = func ~(operation, count) {\n" +
                            "            var vf = call f(call x(x));\n" +
                            "            return call vf(operation, count);\n" +
                            "        };\n" +
                            "        return fn;\n" +
                            "    };\n" +
                            "    return call (func ~(h) -> call h(h))(fx);\n" +
                            "})(repeat);" +
                            "var trampoline = func ~(f) {\n" +
                            "    while (!(call g_is_null(f)) && (call g_get_type(f) == \"函数\")) {\n" +
                            "        let f = call f();\n" +
                            "    }\n" +
                            "};\n" +
                            "var print = func ~() -> call g_printn(\"\"+5);\n" +
                            "var tfun = func ~() -> call REPEAT(print, 10);\n" +
                            "call trampoline(tfun);" +
                            "\n",

                    "import \"sys.base\";\n" +
                            "call g_printn(\"Trampoline example:\");\n" +
                            "var repeat = func ~(operation, count) {\n" +
                            "    var repeat0 = func ~() {\n" +
                            "        if (count <= 0) { return; }\n" +
                            "        call operation(count);\n" +
                            "        return call repeat(operation, --count);\n" +
                            "    };\n" +
                            "    return repeat0;\n" +
                            "};\n" +
                            "var print = func ~(n) -> call g_printn(\"n: \" + n);\n" +
                            "var tfun = func ~() -> call repeat(print, 5);\n" +
                            "call(func ~(f) {\n" +
                            "    while (!(call g_is_null(f)) && (call g_get_type(f) == \"函数\")) {\n" +
                            "        let f = call f(); // Trampoline, like CPS.\n" +
                            "    }\n" +
                            "})(tfun);\n" +
                            "\n", "import \"sys.base\";\n" +
                    "import \"sys.list\";\n" +
                    "import \"sys.proc\";\n" +
                    "import \"sys.task\";\n" +
                    "import \"sys.string\";\n" +
                    "/* 创建场景 */\n" +
                    "var create_stage = func ~(f) -> call f();\n" +
                    "\n" +
                    "/* 重复操作 */\n" +
                    "var real_repeat = func ~(_operation, _arg, _start, _end) {\n" +
                    "    var repeat = func ~(operation, arg, start, end) {\n" +
                    "        var index = start;\n" +
                    "        var repeat0 = func ~() {\n" +
                    "            if (index >= end) { return; }\n" +
                    "            call g_print(\"ttt\");call operation(arg, index);\n" +
                    "            return call repeat(operation, arg, ++index, end);\n" +
                    "        };\n" +
                    "        return repeat0;\n" +
                    "    };\n" +
                    "    var repear_f = func ~() -> call repeat(_operation, _arg, _start, _end);\n" +
                    "     call g_print(\"ttt2\");call(func ~(f) {\n" +
                    "call g_print(call g_to_string(call g_get_type_ordinal(f)));" +
                    "        while (!(call g_is_null(f)) && (call g_get_type_ordinal(f) == 8)) {\n" +
                    "            let f = call f();\n" +
                    "        }\n" +
                    "    })(repear_f); call g_print(\"tttr\");\n" +
                    "};\n" +
                    "    \n" +
                    "/* 打字效果 */\n" +
                    "var word_fadein = func ~(str, span) {\n" +
                    "    var print = func ~(a, n) {\n" +
                    "        call g_print(call g_string_char(a, n));\n" +
                    "        call g_sleep(span);\n" +
                    "    };\n" +
                    "    call real_repeat(print, str, 0, call g_string_length(str));\n" +
                    "};\n" +
                    "\n" +
                    "/* 场景一 */\n" +
                    "var stage_1 = func ~() {\n" +
                    "    call word_fadein(\"Hello world!\\n\", 200);\n" +
                    "};\n" +
                    "\n" +
                    "call create_stage(stage_1);")

            println(codes[codes.size - 1])
            val interpreter = Interpreter()
            val grammar = Grammar(codes[codes.size - 1])
            //System.out.println(grammar.toString());
            val page = grammar.codePage
            //System.out.println(page.toString());
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
