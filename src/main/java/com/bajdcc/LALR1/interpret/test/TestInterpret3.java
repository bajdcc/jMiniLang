package com.bajdcc.LALR1.interpret.test;

import com.bajdcc.LALR1.grammar.Grammar;
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import com.bajdcc.LALR1.grammar.runtime.RuntimeException;
import com.bajdcc.LALR1.interpret.Interpreter;
import com.bajdcc.LALR1.syntax.handler.SyntaxException;
import com.bajdcc.util.lexer.error.RegexException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@SuppressWarnings("unused")
public class TestInterpret3 {

	public static void main(String[] args) {
		try {
			String[] codes = new String[]{
					"import \"sys.base\";\n"
							+ "var a = true;\n"
							+ "if (a) {call g_print(\"ok\");}\n"
							+ "else {call g_print(\"failed\");}",
					"import \"sys.base\";\n"
							+ "call g_print(\n"
							+ "    call (func~(a,b,c) -> call a(b,c))(\"g_max\",5,6));\n",
					"import \"sys.base\";\n"
							+ "var t = 0;\n"
							+ "for (var i = 0; i < 10; i++) {\n"
							+ "    if (i % 2 == 0) {\n"
							+ "        continue;\n"
							+ "    }\n"
							+ "    let t = t + i;\n"
							+ "}\n"
							+ "call g_print(t);\n",
					"import \"sys.base\";\n"
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
							+ "call g_print(sum);\n",
					"import \"sys.base\";" +
							"var a=func~(){var f=func~()->call g_print(\"af\");call f();};" +
							"var b=func~(){var f=func~()->call g_print(\"bf\");call f();};" +
							"call a();call b();"
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
