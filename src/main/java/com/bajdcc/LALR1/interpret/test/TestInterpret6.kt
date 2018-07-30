package com.bajdcc.LALR1.interpret.test

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage
import com.bajdcc.LALR1.grammar.runtime.RuntimeException
import com.bajdcc.LALR1.interpret.Interpreter
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.lexer.error.RegexException

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object TestInterpret6 {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val codes = arrayOf(

                    "import \"sys.base\";\n"
                            + "var ff = func ~(f) {\n"
                            + "    var fh = func ~(h) {\n"
                            + "        return call h(h);\n"
                            + "    };\n"
                            + "    var fx = func ~(x) {\n"
                            + "        var fn = func ~(n) {\n"
                            + "            var vx = call x(x);\n"
                            + "            var vf = call f(vx);\n"
                            + "            return call vf(n);\n"
                            + "        };\n"
                            + "        return fn;\n"
                            + "    };\n"
                            + "    return call fh(fx);\n"
                            + "};\n"
                            + "var fact = func ~(f) {\n"
                            + "    var fn = func ~(n) -> (n > 0) ? (n * call f(n - 1)) : 1;\n"
                            + "    return fn;\n"
                            + "};\n"
                            + "var ffact = call ff(fact);\n"
                            + "var fact_5 = call ffact(5);\n"
                            + "call g_printn(fact_5);\n"
                            + "\n",

                    "import \"sys.base\";\n"
                            + "var ff = func ~(f) {\n"
                            + "    var fh = func ~(h) {\n"
                            + "        return call h(h);\n"
                            + "    };\n"
                            + "    var fx = func ~(x) {\n"
                            + "        var fn = func ~(n) {\n"
                            + "            var vx = call x(x);\n"
                            + "            var vf = call f(vx);\n"
                            + "            return call vf(n);\n"
                            + "        };\n"
                            + "        return fn;\n"
                            + "    };\n"
                            + "    return call fh(fx);\n"
                            + "};\n"
                            + "var fact = func ~(f) {\n"
                            + "    var fk = func ~(n) {\n"
                            + "        if (n > 0) {\n"
                            + "            return n * call f(n - 1);\n"
                            + "        } else {\n"
                            + "            return 1;\n"
                            + "        };\n"
                            + "    };\n"
                            + "    return fk;\n"
                            + "};\n"
                            + "var ffact = call ff(fact);\n"
                            + "var fact_5 = call ffact(5);\n"
                            + "call g_printn(fact_5);\n"
                            + "\n")

            val interpreter = Interpreter()
            val grammar = Grammar(codes[codes.size - 1])
            println(codes[codes.size - 1])
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
