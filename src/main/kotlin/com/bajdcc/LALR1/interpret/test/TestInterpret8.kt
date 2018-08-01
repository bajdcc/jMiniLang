package com.bajdcc.LALR1.interpret.test

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage
import com.bajdcc.LALR1.grammar.runtime.RuntimeException
import com.bajdcc.LALR1.interpret.Interpreter
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.lexer.error.RegexException

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object TestInterpret8 {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val codes = arrayOf(

                    "import \"sys.base\"; import \"sys.list\";\n" +
                            "var a = [];\n" +
                            "call g_array_add(a, 5);\n" +
                            "call g_array_set(a, 0, 4);\n" +
                            "call g_printn(call g_array_get(a, 0));\n" +
                            "call g_array_remove(a, 0);\n" +
                            "call g_array_add(a, 50);\n" +
                            "call g_array_add(a, 100);\n" +
                            "call g_array_set(a, 1, 400);\n" +
                            "call g_printn(call g_array_get(a, 1));\n" +
                            "call g_array_pop(a);\n" +
                            "call g_array_pop(a);\n" +
                            "call g_printn(call g_array_size(a));\n" +
                            "\n" +
                            "let a = {};\n" +
                            "call g_map_put(a, \"x\", 5);\n" +
                            "call g_map_put(a, \"y\", 10);\n" +
                            "call g_map_put(a, \"x\", 50);\n" +
                            "call g_printn(call g_map_size(a));\n" +
                            "call g_printn(call g_map_get(a, \"x\"));\n" +
                            "call g_printn(call g_map_get(a, \"y\"));\n" +
                            "call g_printn(call g_map_contains(a, \"x\"));\n" +
                            "call g_map_remove(a, \"x\");\n" +
                            "call g_printn(call g_map_contains(a, \"x\"));\n" +
                            "call g_printn(call g_map_size(a));\n" +
                            "\n",

                    "import \"sys.base\"; import \"sys.list\";\n" +
                            "var create_node = func ~(data) {\n" +
                            "    var new_node = g_new_map;\n" +
                            "    call g_map_put(new_node, \"data\", data);\n" +
                            "    call g_map_put(new_node, \"prev\", g_null);\n" +
                            "    call g_map_put(new_node, \"next\", g_null);\n" +
                            "    return new_node;\n" +
                            "};\n" +
                            "var append = func ~(head, obj) {\n" +
                            "    var new_node = call create_node(obj);\n" +
                            "    call g_map_put(new_node, \"next\", head);\n" +
                            "    call g_map_put(head, \"prev\", new_node);\n" +
                            "    return new_node;" +
                            "};\n" +
                            "var head = call create_node(0);\n" +
                            "foreach (var i : call g_range(1, 10)) {\n" +
                            "    let head = call append(head, i);\n" +
                            "}\n" +
                            "var p = head;\n" +
                            "while (!call g_is_null(p)) {\n" +
                            "    call g_printn(call g_map_get(p, \"data\"));\n" +
                            "    let p = call g_map_get(p, \"next\");\n" +
                            "}\n" +
                            "\n")

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
