package priv.bajdcc.util.lexer.test;

import java.util.Scanner;

import priv.bajdcc.util.lexer.Lexer;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

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
				if (token.kToken == TokenType.EOF) {
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
