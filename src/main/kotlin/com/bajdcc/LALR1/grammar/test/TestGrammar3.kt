package com.bajdcc.LALR1.grammar.test

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.*
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.lexer.error.RegexException

object TestGrammar3 {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val a = ("\n"
                    + "var g_a = func ~(x, y) { return x + y;};\n"
                    + "export \"g_a\";")

            val b = ("import \"test1\";\n"
                    + "var d = call g_a(1,2);\n"
                    + "var c = g_gk;\n"
                    + "call g_print(c);\n"
                    + "var t = call g_print(c);")

            /*
			 * BufferedReader br = new BufferedReader(new
			 * FileReader("E:/http.c")); String line = ""; StringBuffer sb = new
			 * StringBuffer(); while ((line = br.readLine()) != null) {
			 * sb.append(line + System.lineSeparator()); }
			 * br.close();
			 */
            val grammar = Grammar(a)
            println(grammar.toString())
            val page = grammar.codePage
            println(page.toString())
            val machine = RuntimeMachine()
            machine.run("test1", page)

            val grammar2 = Grammar(b)
            println(grammar2.toString())
            val page2 = grammar2.codePage
            page2.info.addExternalFunc("g_print", RuntimeDebugExec("Print", arrayOf(RuntimeObjectType.kObject)) { arg: List<RuntimeObject>, _: IRuntimeStatus ->
                run {
                    println(arg[0].obj)
                    null
                }
            })
            println(page2.toString())
            machine.run("test2", page2)

            // FileWriter fw = new FileWriter("E:/testgrammar.txt");
            // fw.append(grammar.toString());
            // fw.close();
        } catch (e: RegexException) {
            System.err.println(e.position.toString() + "," + e.message)
            e.printStackTrace()
        } catch (e: SyntaxException) {
            System.err.println(e.position.toString() + "," + e.message + " "
                    + e.info)
            e.printStackTrace()
            // } catch (IOException e) {
            // System.err.println(e.getMessage());
            // e.printStackTrace();
        } catch (e: RuntimeException) {
            System.err.println(e.position.toString() + ": " + e.info)
            e.printStackTrace()
            // } catch (IOException e) {
            // System.err.println(e.getMessage());
            // e.printStackTrace();
        } catch (e: Exception) {
            System.err.println(e.message)
            e.printStackTrace()
            // } catch (IOException e) {
            // System.err.println(e.getMessage());
            // e.printStackTrace();
        }

    }
}
