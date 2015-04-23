package priv.bajdcc.OP.syntax.test;

import java.util.Scanner;

import priv.bajdcc.OP.syntax.lexer.SyntaxLexer;
import priv.bajdcc.OP.syntax.token.Token;
import priv.bajdcc.OP.syntax.token.TokenType;
import priv.bajdcc.util.lexer.error.RegexException;

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
