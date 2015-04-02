package priv.bajdcc.syntax.test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.token.TokenType;
import priv.bajdcc.syntax.Syntax;
import priv.bajdcc.syntax.error.SyntaxException;

@SuppressWarnings("unused")
public class TestSyntax2 {

	public static void main(String[] args) {
		try {
			//Scanner scanner = new Scanner(System.in);
			Syntax syntax = new Syntax();
			syntax.addTerminal("a", TokenType.ID, "a");
			syntax.addTerminal("b", TokenType.ID, "b");
			syntax.addNonTerminal("Z");
			syntax.addNonTerminal("S");
			syntax.addNonTerminal("B");
			syntax.addErrorHandler("sample", null);
			syntax.infer("Z -> S");
			syntax.infer("S -> B B");
			syntax.infer("B -> `a` B");
			syntax.infer("B -> `b`");
			syntax.initialize("Z");
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
