package priv.bajdcc.LL1.grammar.test;

import java.util.Scanner;

import priv.bajdcc.LL1.grammar.Grammar;
import priv.bajdcc.LL1.grammar.error.GrammarException;
import priv.bajdcc.LL1.syntax.Syntax;
import priv.bajdcc.LL1.syntax.handler.SyntaxException;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.token.OperatorType;
import priv.bajdcc.util.lexer.token.TokenType;

@SuppressWarnings("unused")
public class TestGrammar {

	public static void main(String[] args) {
		// System.out.println("Z -> `a`<,> | B | [`a` `b` Z B]");
		try {
			// Scanner scanner = new Scanner(System.in);
			//Grammar grammar = new Grammar("(i * i) * (i + i) - i");
			Grammar grammar = new Grammar("i + i * i");
			grammar.addTerminal("SYMBOL", TokenType.ID, "i");
			grammar.addTerminal("PLUS", TokenType.OPERATOR, OperatorType.PLUS);
			grammar.addTerminal("MINUS", TokenType.OPERATOR, OperatorType.MINUS);
			grammar.addTerminal("TIMES", TokenType.OPERATOR, OperatorType.TIMES);
			grammar.addTerminal("DIVIDE", TokenType.OPERATOR,
					OperatorType.DIVIDE);
			grammar.addTerminal("LPA", TokenType.OPERATOR, OperatorType.LPARAN);
			grammar.addTerminal("RPA", TokenType.OPERATOR, OperatorType.RPARAN);
			grammar.setEpsilonName("epsilon");
			String[] nons = new String[] {
					"E", "E1", "T", "T1", "F", "A", "M"
			};
			for (String non : nons){
				grammar.addNonTerminal(non);
			}
			grammar.infer("E -> T E1");
			grammar.infer("E1 -> A T E1 | @epsilon");
			grammar.infer("T -> F T1");
			grammar.infer("T1 -> M F T1 | @epsilon");
			grammar.infer("F -> @LPA E @RPA | @SYMBOL");
			grammar.infer("A -> @PLUS | @MINUS");
			grammar.infer("M -> @TIMES | @DIVIDE");
			grammar.initialize("E");
			System.out.println(grammar.toString());
			System.out.println(grammar.getPredictionString());
			grammar.run();
			System.out.println(grammar.getTokenString());
			// scanner.close();
		} catch (RegexException e) {
			System.err.println(e.getPosition() + "," + e.getMessage());
			e.printStackTrace();
		} catch (SyntaxException e) {
			System.err.println(e.getPosition() + "," + e.getMessage() + " "
					+ e.getInfo());
			e.printStackTrace();
		} catch (GrammarException e) {
			System.err.println(e.getPosition() + "," + e.getMessage() + " "
					+ e.getInfo());
			e.printStackTrace();
		}
	}
}
