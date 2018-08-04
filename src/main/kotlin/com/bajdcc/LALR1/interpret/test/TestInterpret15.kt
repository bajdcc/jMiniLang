package com.bajdcc.LALR1.interpret.test

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage
import com.bajdcc.LALR1.grammar.runtime.RuntimeException
import com.bajdcc.LALR1.interpret.Interpreter
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.lexer.error.RegexException

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object TestInterpret15 {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val codes = arrayOf("""
import "sys.base";
import "sys.proc";
g_proc_exec("
import \"user.base\";
var w = g_window(\"test\");
");
""".trimIndent())

            println(codes[codes.size - 1])
            val interpreter = Interpreter()
            val grammar = Grammar(codes[codes.size - 1])
            println(grammar.toString())
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
            System.err.println(String.format("模块名：%s. 位置：%s. 错误：%s-%s(%s:%d)",
                    e.pageName, e.position, e.message,
                    e.info, e.fileName, e.position.line + 1))
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
