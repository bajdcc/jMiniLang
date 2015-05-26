package priv.bajdcc.LALR1.interpret.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeDebugExec;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeDebugValue;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeException;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeMachine;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeObjectType;
import priv.bajdcc.LALR1.interpret.Interpreter;
import priv.bajdcc.LALR1.syntax.handler.SyntaxException;
import priv.bajdcc.util.lexer.error.RegexException;

@SuppressWarnings("unused")
public class TestInterpret4 {

	public static void main(String[] args) {
		try {
			String[] codes = new String[] {
					
							// 高级语言特性之迭代返回，即协程
					"import \"sys.base\";\n"
							+ "var get = yield ~(a, b, c) {\n"
							+ "    for (var i = a; i < b; i++) {\n"
							+ "        if (i % c == 0) {\n"
							+ "            continue;\n"
							+ "        }\n"
							+ "        yield i;\n"
							+ "    }\n"
							+ "};\n"
							+ "foreach (var i : call get(0, 10, 2) + 1) {\n"
							+ "   call g_print(i);\n"
							+ "}\n",
							
							// 高级语言特性之函数闭包
					"import \"sys.base\";\n"
							+ "var gen_eq = func ~(a) {\n"
							+ "    var eq = func ~(b) -> a == b;\n"
							+ "    return eq;\n"
							+ "};\n"
							+ "var ex_eq = call gen_eq(5);\n" // 函数Curry，保存词法上下文
							+ "call g_print(call ex_eq(6));\n"
							+ "call g_print(call ex_eq(6));\n",
							
					"import \"sys.base\";\n"
							+ "var lt = call g_curry_1(\"g_lt\", 5);\n"
							+ "call g_print(call lt(7));\n"
							+ "call g_println();\n"
							+ "call g_print(call lt(4));\n"
							+ "call g_println();\n"
							+ "\n",
							
					"import \"sys.base\";\n"
							+ "var get = yield ~(a, b, c) {\n"
							+ "    for (var i = a; i < b; i++) {\n"
							+ "        if (i % c == 0) {\n"
							+ "            continue;\n"
							+ "        }\n"
							+ "        yield i;\n"
							+ "    }\n"
							+ "};\n"
							+ "foreach (var i : call get(0, 10, 2) * 10) {\n"
							+ "   call g_print(i);\n"
							+ "   call g_println();\n"
							+ "}\n",
							
					"import \"sys.base\";\n"
							+ "var a = 5;\n"
							+ "var b = 4;\n"
							+ "\n"
							+ "    if (a == 5) {\n"
							+ "       var b = 7;\n"
							+ "       call g_print(b);\n"
							+ "    }\n"
							+ "call g_print(b);\n",
							
					"import \"sys.base\";\n"
							+ "var mod_swap = call g_swap(\"g_mod\");\n"
							+ "var mod_3 = call g_curry_1(mod_swap, 3);\n"
							+ "foreach (var i : call g_range(1, 10) * 8) {\n"
							+ "    if (call mod_3(i)) {\n"
							+ "       call g_print(i);\n"
							+ "       call g_println();\n"
							+ "    }\n"
							+ "}\n",
							
							// 打印质数版本1
					"import \"sys.base\";\n"
							+ "call g_print(\"输入下限：\");\n"
							+ "var lower_bound = call g_stdin_read_int();\n"
							+ "call g_print(\"输入上限：\");\n"
							+ "var upper_bound = call g_stdin_read_int();\n"
							+ "var prime = func ~(a) {\n"
							+ "    if (a <= 1) {\n"
							+ "        return false;\n"
							+ "    }\n"
							+ "    var mod_a = call g_curry_1(\"g_mod\", a);\n"
							+ "    var mod_any = func ~(x) -> call mod_a(x);\n"
							+ "    if (!call g_range_any(2, a - 1, mod_any)) {\n"
							+ "        call g_print(a);\n"
							+ "        call g_println();\n"
							+ "    }\n"
							+ "};\n"
							+ "call g_range_foreach(lower_bound, upper_bound, prime);\n"
							+ "\n",
							
							// 打印质数版本2
					"import \"sys.base\";\n"
							+ "call g_print(\"输入下限：\");\n"
							+ "var lower_bound = call g_stdin_read_int();\n"
							+ "call g_print(\"输入上限：\");\n"
							+ "var upper_bound = call g_stdin_read_int();\n"
							+ "var mod_swap = call g_swap(\"g_mod\");\n"
							+ "var prime = func ~(a) {\n"
							+ "    if (a <= 1) {\n"
							+ "        return false;\n"
							+ "    }\n"
							+ "    foreach (var i : call g_range(2, a - 1)) {\n"
							+ "        var mod_i = call g_curry_1(mod_swap, i);\n"
							+ "        if (call mod_i(a)) {\n"
							+ "            return;"
							+ "        }\n"
							+ "    }\n"
							+ "    call g_print(a);\n"
							+ "    call g_println();\n"
							+ "};\n"
							+ "call g_range_foreach(lower_bound, upper_bound, prime);\n"
							+ "\n",
							
							// 打印质数版本3
					"import \"sys.base\";\nimport \"sys.math\";\n"
							+ "call g_print(\"输入下限：\");\n"
							+ "var lower_bound = call g_stdin_read_int();\n"
							+ "call g_print(\"输入上限：\");\n"
							+ "var upper_bound = call g_stdin_read_int();\n"
							+ "var print_prime = func ~(a, b) {\n"
							+ "    for (var i = a; i <= b; i++) {\n"
							+ "        if (i <= 1) {\n"
							+ "            continue;\n"
							+ "        }\n"
							+ "        var sq = call g_sqrt(i);\n"
							+ "        for (var j = 2; j <= sq; j++) {\n"
							+ "            if (i % j == 0) {\n"
							+ "                break;\n"
							+ "            }\n"
							+ "        }\n"
							+ "        if (j > sq) {\n"
							+ "            call g_print(i);\n"
							+ "            call g_println();\n"
							+ "        }\n"
							+ "    }\n"
							+ "};\n"
							+ "call print_prime(lower_bound, upper_bound);\n"
							+ "\n",
							
							};

			Interpreter interpreter = new Interpreter();
			Grammar grammar = new Grammar(codes[codes.length - 1]);
			System.out.println(grammar.toString());
			RuntimeCodePage page = grammar.getCodePage();
			System.out.println(page.toString());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			RuntimeCodePage.exportFromStream(page, baos);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			interpreter.run("test_1", bais);

		} catch (RegexException e) {
			System.err.println();
			System.err.println(e.getPosition() + "," + e.getMessage());
			e.printStackTrace();
		} catch (SyntaxException e) {
			System.err.println();
			System.err.println(e.getPosition() + "," + e.getMessage() + " "
					+ e.getInfo());
			e.printStackTrace();
		} catch (RuntimeException e) {
			System.err.println();
			System.err.println(e.getPosition() + ": " + e.getInfo());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println();
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
