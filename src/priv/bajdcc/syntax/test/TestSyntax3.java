package priv.bajdcc.syntax.test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.syntax.Syntax;
import priv.bajdcc.syntax.error.SyntaxException;

@SuppressWarnings("unused")
public class TestSyntax3 {

	public static void main(String[] args) {
		try {
			//Scanner scanner = new Scanner(System.in);
			Syntax syntax = new Syntax();
			syntax.addTerminal("a", "a");
			syntax.addTerminal("c", "c");
			syntax.addTerminal("d", "d");
			syntax.addNonTerminal("S");
			syntax.addNonTerminal("A");
			syntax.addErrorHandler("sample", null);
			syntax.infer("S -> `c` A `d`");
			syntax.infer("A -> `a`");
			syntax.infer("A -> A `a`");
			syntax.initialize("S");
			System.out.println(syntax.toString());
			System.out.println(syntax.getNGAString());
			System.out.println(syntax.getNPAString());
			//scanner.close();
		} catch (RegexException e) {
			System.err.println(e.getPosition() + "," + e.getMessage());
			e.printStackTrace();
		} catch (SyntaxException e) {
			System.err.println(e.getPosition() + "," + e.getMessage() + " " + e.getInfo());
			e.printStackTrace();
		}
	}
}
