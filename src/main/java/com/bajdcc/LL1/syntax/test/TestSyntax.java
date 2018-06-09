package com.bajdcc.LL1.syntax.test;

import com.bajdcc.LL1.syntax.Syntax;
import com.bajdcc.LL1.syntax.handler.SyntaxException;
import com.bajdcc.util.lexer.error.RegexException;
import com.bajdcc.util.lexer.token.OperatorType;
import com.bajdcc.util.lexer.token.TokenType;

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
			syntax.setEpsilonName("epsilon");
			String[] nons = new String[]{
					"E", "E1", "T", "T1", "F", "A", "M"
			};
			for (String non : nons) {
				syntax.addNonTerminal(non);
			}
			syntax.infer("E -> T E1");
			syntax.infer("E1 -> A T E1 | @epsilon");
			syntax.infer("T -> F T1");
			syntax.infer("T1 -> M F T1 | @epsilon");
			syntax.infer("F -> @LPA E @RPA | @SYMBOL");
			syntax.infer("A -> @PLUS | @MINUS");
			syntax.infer("M -> @TIMES | @DIVIDE");
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
