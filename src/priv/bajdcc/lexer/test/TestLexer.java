package priv.bajdcc.lexer.test;

import java.util.Scanner;

import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.lexer.Lexer;
import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.lexer.token.TokenType;

public class TestLexer {

	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			String str = scanner.nextLine();
			scanner.close();
			Lexer lexer = new Lexer(str);
			Token token;
			for (;;) {
				token = lexer.scan();
				if (token.m_kToken == TokenType.EOF) {
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
