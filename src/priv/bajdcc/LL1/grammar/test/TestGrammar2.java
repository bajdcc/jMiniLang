package priv.bajdcc.LL1.grammar.test;

import priv.bajdcc.LL1.grammar.Grammar;
import priv.bajdcc.LL1.grammar.error.GrammarException;
import priv.bajdcc.LL1.syntax.handler.SyntaxException;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.token.TokenType;

@SuppressWarnings("unused")
public class TestGrammar2 {

	public static void main(String[] args) {
		// System.out.println("Z -> `a`<,> | B | [`a` `b` Z B]");
		try {
			// Scanner scanner = new Scanner(System.in);
			Grammar syntax = new Grammar("b b b b ");
			syntax.addTerminal("a", TokenType.ID, "a");
			syntax.addTerminal("b", TokenType.ID, "b");
			syntax.setEpsilonName("epsilon");
			String[] nons = new String[] {
					"S", "A", "B"
			};
			for (String non : nons){
				syntax.addNonTerminal(non);
			}
			syntax.infer("S -> A B A B");
			syntax.infer("A -> @a A | @epsilon");
			syntax.infer("B -> @b B | @epsilon");
			syntax.initialize("S");
			System.out.println(syntax.toString());
			System.out.println(syntax.getPredictionString());
			syntax.run();
			System.out.println(syntax.getTokenString());
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
