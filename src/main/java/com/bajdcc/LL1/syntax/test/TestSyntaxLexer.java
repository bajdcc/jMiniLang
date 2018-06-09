package com.bajdcc.LL1.syntax.test;

import com.bajdcc.LL1.syntax.lexer.SyntaxLexer;
import com.bajdcc.LL1.syntax.token.Token;
import com.bajdcc.LL1.syntax.token.TokenType;
import com.bajdcc.util.lexer.error.RegexException;

import java.util.Scanner;

public class TestSyntaxLexer {

	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			String str = scanner.nextLine();
			SyntaxLexer lexer = new SyntaxLexer();
			lexer.setContext(str);
			Token token;
			for (; ; ) {
				token = lexer.nextToken();
				if (token.kToken == TokenType.EOF) {
					break;
				}
				System.out.println(token.toString());
			}
			scanner.close();
		} catch (RegexException e) {
			System.err.println(e.getPosition() + "," + e.getMessage());
			e.printStackTrace();
		}
	}
}