package priv.bajdcc.OP.syntax.test;

import priv.bajdcc.OP.syntax.Syntax;
import priv.bajdcc.OP.syntax.handler.SyntaxException;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.token.OperatorType;
import priv.bajdcc.util.lexer.token.TokenType;

@SuppressWarnings("unused")
public class TestSyntax {

	public static void main(String[] args) {
		// System.out.println("Z -> `a`<,> | B | [`a` `b` Z B]");
		try {
			// Scanner scanner = new Scanner(System.in);
			Syntax syntax = new Syntax();
			syntax.addTerminal("SYMBOL", TokenType.ID, "i");
			syntax.addTerminal("PLUS", TokenType.OPERATOR, OperatorType.PLUS);
			syntax.addTerminal("MINUS", TokenType.OPERATOR, OperatorType.MINUS);
			syntax.addTerminal("TIMES", TokenType.OPERATOR, OperatorType.TIMES);
			syntax.addTerminal("DIVIDE", TokenType.OPERATOR,
					OperatorType.DIVIDE);
			syntax.addTerminal("LPA", TokenType.OPERATOR, OperatorType.LPARAN);
			syntax.addTerminal("RPA", TokenType.OPERATOR, OperatorType.RPARAN);
			String[] nons = new String[]{
					"E", "T", "F"
			};
			for (String non : nons) {
				syntax.addNonTerminal(non);
			}
			syntax.infer("E -> E @PLUS T | E @MINUS T | T");
			syntax.infer("T -> T @MINUS F | T @DIVIDE F | F");
			syntax.infer("F -> @LPA E @RPA | @SYMBOL");
			syntax.initialize("E");
			System.out.println(syntax.toString());
			// scanner.close();
		} catch (RegexException e) {
			System.err.println(e.getPosition() + "," + e.getMessage());
			e.printStackTrace();
		} catch (SyntaxException e) {
			System.err.println(e.getPosition() + "," + e.getMessage() + " "
					+ e.getInfo());
			e.printStackTrace();
		}
	}
}
