package com.bajdcc.LALR1.interpret.test

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage
import com.bajdcc.LALR1.grammar.runtime.RuntimeException
import com.bajdcc.LALR1.interpret.Interpreter
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.lexer.error.RegexException

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object TestInterpret14 {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val codes = arrayOf("""
import "sys.base";
import "sys.proc";
g_proc_exec("import \"user.base\";
import \"user.cparser\";
var code = \"1 1. -2 e () (8 - -1)\n+-*/()[]{};,.:=_%^&|?!`~
===+=-=*=/=++=--=%%=~=^=<<<=>>>=
\\\"ab\\\\f\\\\xff\\\\Uffff\\\\077\\\"
\\\"ab\\\\k\\\\Xfff\\\\ufffff\\\\777\\\\\\\"
'a''ab''\\\\a''\\\\fb''\\\\kb''
123e1 123.e1 123.4e1 -123e1 123.e-1 123.4e-1
-123e-1 -123.e-1 -123.4e-1 123456787654321 //abc
- .1 -.1 .e1 -.e-1 -123e -123e- /*123
456*/\";
var s = g_new_class(\"clib::c::scanner\", [], [[\"init\", code]]);
var token; while (!(token := s.\"scan\"()).\"eof\"()) { g_printn(token.\"to_string\"()); }
g_printn(\"Errors: \" + s.\"ERROR\"());
");
            """.trimIndent(),

                    """
import "sys.base";
import "sys.proc";
g_proc_exec("import \"user.base\";
import \"user.cparser\";
var code = \"int a,b,c[5];
float d,e,f[5];
char d;
int; int char; char a [ ]; 5 [5]; int b[c];
int a1(int b, float c);
int a2(int b, float c;
int a3(; int (; int a4(a, b); int a5(float a,; int a6(float); int a7(char d, int);
\";
var s = g_new_class(\"clib::c::parser\", [], [[\"init\", code]]);
g_printn(s.\"parse\"());
g_printn(\"Errors: \" + s.\"ERROR\"());
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
