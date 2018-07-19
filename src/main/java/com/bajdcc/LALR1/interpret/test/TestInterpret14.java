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
public class TestInterpret14 {

	public static void main(String[] args) {
		try {
			String[] codes = new String[]{
					"import \"sys.base\";\n" +
					"import \"sys.proc\";\n" +
							"g_proc_exec(\"" +
							"import \\\"user.base\\\";\n" +
							"import \\\"user.cparser\\\";\n" +
							"var code = \\\"1 1. -2 e () (8 - -1)\\n" +
							"\\\\\\\"ab\\\\\\\\f\\\\\\\\xff\\\\\\\\Uffff\\\\\\\\077\\\\\\\" \\n" +
							"\\\\\\\"ab\\\\\\\\k\\\\\\\\Xfff\\\\\\\\ufffff\\\\\\\\777\\\\\\\\\\\\\\\" \\n" +
							"'a''ab''\\\\\\\\a''\\\\\\\\fb''\\\\\\\\kb''\\n" +
							"123e1 123.e1 123.4e1 -123e1 123.e-1 123.4e-1\\n" +
							"-123e-1 -123.e-1 -123.4e-1 123456787654321 //abc\\n" +
							"- .1 -.1 .e1 -.e-1 -123e -123e- /*123\n456*/\\\";\n" +
							"var s = g_new_class(\\\"clib::c::scanner\\\", [], [[\\\"init\\\", code]]);\n" +
							"var token; while (!(token := s.\\\"scan\\\"()).\\\"eof\\\"()) { g_printn(token.\\\"to_string\\\"()); }\n" +
							"g_printn(\\\"Errors: \\\" + s.\\\"ERROR\\\"());\n" +
							"\");\n",
			};

			System.out.println(codes[codes.length - 1]);
			Interpreter interpreter = new Interpreter();
			Grammar grammar = new Grammar(codes[codes.length - 1]);
			System.out.println(grammar.toString());
			RuntimeCodePage page = grammar.getCodePage();
			//System.out.println(page.toString());
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
			System.err.println(String.format("模块名：%s. 位置：%s. 错误：%s-%s(%s:%d)",
					e.getPageName(), e.getPosition(), e.getMessage(),
					e.getInfo(), e.getFileName(), e.getPosition().iLine + 1));
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
