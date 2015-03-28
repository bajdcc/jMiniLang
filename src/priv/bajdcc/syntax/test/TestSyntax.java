package priv.bajdcc.syntax.test;

import java.util.Scanner;

import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.syntax.Syntax;
import priv.bajdcc.syntax.error.SyntaxException;

public class TestSyntax {

	public static void main(String[] args) {
		System.out.println("Z -> `a`<,> | B | [`a` `b` Z B]");
		try {
			Scanner scanner = new Scanner(System.in);
			Syntax syntax = new Syntax();
			syntax.addTerminal("a", "a");
			syntax.addTerminal("b", "b");
			syntax.addNonTerminal("Z");
			syntax.addNonTerminal("B");
			syntax.addErrorHandler("abc", null);
			syntax.infer(scanner.nextLine());
			System.out.println(syntax.toString());
			scanner.close();
		} catch (RegexException e) {
			System.err.println(e.getPosition() + "," + e.getMessage());
			e.printStackTrace();
		} catch (SyntaxException e) {
			System.err.println(e.getPosition() + "," + e.getMessage() + " " + e.getInfo());
			e.printStackTrace();
		}
	}
}
