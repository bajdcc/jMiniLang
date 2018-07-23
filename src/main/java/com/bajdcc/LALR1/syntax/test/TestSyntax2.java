package com.bajdcc.LALR1.syntax.test;

import com.bajdcc.LALR1.syntax.Syntax;
import com.bajdcc.LALR1.syntax.handler.SyntaxException;
import com.bajdcc.util.lexer.error.RegexException;
import com.bajdcc.util.lexer.token.TokenType;

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
			syntax.infer("B -> @a B");
			syntax.infer("B -> @b");
			syntax.initialize("Z");
			System.out.println(syntax.toString());
			System.out.println(syntax.getNgaString());
			System.out.println(syntax.getNpaString());
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
