package priv.bajdcc.LALR1.interpret.test;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeException;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeException.RuntimeError;
import priv.bajdcc.LALR1.interpret.Interpreter;
import priv.bajdcc.util.lexer.error.RegexException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Scanner;

@SuppressWarnings("unused")
public class TestInterpret2 {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			Interpreter interpreter = new Interpreter();
			int i = 0;
			while (true) {
				System.out.print(">> ");
				String code = scanner.nextLine();
				try {
					Grammar grammar = new Grammar(code);
					// System.out.println(grammar.toString());
					RuntimeCodePage page = grammar.getCodePage();
					// System.out.println(page.toString());
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					RuntimeCodePage.exportFromStream(page, baos);
					ByteArrayInputStream bais = new ByteArrayInputStream(
							baos.toByteArray());
					interpreter.run("test_" + i++, bais);
				} catch (RegexException e) {
					System.err.println(e.getPosition() + "," + e.getMessage());
					//e.printStackTrace();
				} catch (RuntimeException e) {
					System.err.println(e.getPosition() + ": " + e.getInfo());
					if (e.getkError() == RuntimeError.EXIT) {
						break;
					}
					//e.printStackTrace();
				} catch (Exception e) {
					System.err.println(e.getMessage());
					//e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			//e.printStackTrace();
		}
	}
}
