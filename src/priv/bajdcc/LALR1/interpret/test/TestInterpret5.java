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
public class TestInterpret5 {

	public static void main(String[] args) {
		try {
			String[] codes = new String[] {
					
					"import \"sys.base\";\n"
							+ "var to_list = func ~(a, b, c) {\n"
							+ "    foreach (var i : call a(b, c)) {\n"
							+ "        call g_printn(i);\n"
							+ "    }\n"
							+ "};\n"
							+ "call to_list(\"g_range\", 1, 100);\n"
							+ "//call g_range(1, 6);\n"
							+ "\n",
							
					"import \"sys.base\";\n"
							+ "var a = 1;"
							+ "if (a == 1) {"
							+ "    call g_printn('x');"
							+ "} else if (a == 2) {"
							+ "    call g_printn('y');"
							+ "}"
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
