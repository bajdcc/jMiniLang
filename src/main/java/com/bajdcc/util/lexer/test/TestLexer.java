package com.bajdcc.util.lexer.test;

import com.bajdcc.util.lexer.Lexer;
import com.bajdcc.util.lexer.error.RegexException;
import com.bajdcc.util.lexer.token.Token;
import com.bajdcc.util.lexer.token.TokenType;

import java.util.Scanner;

public class TestLexer {

	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			String str = scanner.nextLine();
			scanner.close();
			Lexer lexer = new Lexer(str);
			Token token;
			for (; ; ) {
				lexer.scan();
				token = lexer.token();
				if (token.getType() == TokenType.EOF) {
					break;
				}
				System.out.println(token.toString());
			}
		} catch (RegexException e) {
			System.err.println(e.getPosition() + "," + e.getMessage());
			e.printStackTrace();
		}
	}
}
