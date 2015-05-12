package priv.bajdcc.LALR1.interpret.test;

import java.io.BufferedReader;
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
public class TestInterpret {

	public static void main(String[] args) {
		try {
			String[] codes = new String[] {
					"import \"base\";"
							+ "call g_print(call g_is_null(g_null));",
					"import \"base\";"
							+ "call g_print(\"Hello World!\");",
							};

			Interpreter interpreter = new Interpreter();
			Grammar grammar = new Grammar(codes[codes.length - 1]);
			System.out.println(grammar.toString());
			RuntimeCodePage page = grammar.getCodePage();
			System.out.println(page.toString());
			interpreter.run("test_1", page);

		} catch (RegexException e) {
			System.err.println(e.getPosition() + "," + e.getMessage());
			e.printStackTrace();
		} catch (SyntaxException e) {
			System.err.println(e.getPosition() + "," + e.getMessage() + " "
					+ e.getInfo());
			e.printStackTrace();
		} catch (RuntimeException e) {
			System.err.println(e.getPosition() + ": " + e.getInfo());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
