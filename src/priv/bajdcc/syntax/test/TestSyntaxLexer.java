package priv.bajdcc.syntax.test;

import java.util.Scanner;

import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.syntax.lexer.SyntaxLexer;
import priv.bajdcc.syntax.token.Token;
import priv.bajdcc.syntax.token.TokenType;

public class TestSyntaxLexer {

	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			String str = scanner.nextLine();
			SyntaxLexer lexer = new SyntaxLexer();
			lexer.setContext(str);
			Token token;
			for (;;) {
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
