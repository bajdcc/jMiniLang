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
public class TestInterpret2 {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			Interpreter interpreter = new Interpreter();
			int i = 0;
			while (true) {
				String code = scanner.nextLine();
				try {
					Grammar grammar = new Grammar(code);
					System.out.println(grammar.toString());
					RuntimeCodePage page = grammar.getCodePage();
					System.out.println(page.toString());
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					RuntimeCodePage.exportFromStream(page, baos);
					ByteArrayInputStream bais = new ByteArrayInputStream(
							baos.toByteArray());
					interpreter.run("test_" + i++, bais);
				} catch (RegexException e) {
					System.err.println(e.getPosition() + "," + e.getMessage());
					e.printStackTrace();
				} catch (RuntimeException e) {
					System.err.println(e.getPosition() + ": " + e.getInfo());
					e.printStackTrace();
				} catch (Exception e) {
					System.err.println(e.getMessage());
					e.printStackTrace();
				}
			}
		} catch (SyntaxException e) {
			System.err.println(e.getPosition() + "," + e.getMessage() + " "
					+ e.getInfo());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
