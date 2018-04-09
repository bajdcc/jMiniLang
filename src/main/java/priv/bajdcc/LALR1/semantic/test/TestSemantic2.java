package priv.bajdcc.LALR1.semantic.test;

import priv.bajdcc.LALR1.semantic.Semantic;
import priv.bajdcc.LALR1.syntax.handler.SyntaxException;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.token.OperatorType;
import priv.bajdcc.util.lexer.token.TokenType;

@SuppressWarnings("unused")
public class TestSemantic2 {

	public static void main(String[] args) {
		// System.out.println("Z -> `a`<,> | B | [`a` `b` Z B]");
		try {
			// Scanner scanner = new Scanner(System.in);
			// String expr = "( i )";
			String expr = "v + d * d";
			Semantic semantic = new Semantic(expr);
			semantic.addTerminal("PLUS", TokenType.OPERATOR, OperatorType.PLUS);
			semantic.addTerminal("TIMES", TokenType.OPERATOR,
					OperatorType.TIMES);
			semantic.addTerminal("LPA", TokenType.OPERATOR, OperatorType.LPARAN);
			semantic.addTerminal("RPA", TokenType.OPERATOR, OperatorType.RPARAN);
			semantic.addTerminal("v", TokenType.ID, "v");
			semantic.addTerminal("d", TokenType.ID, "d");
			semantic.addNonTerminal("S");
			semantic.addNonTerminal("E");
			semantic.addNonTerminal("T");
			semantic.addNonTerminal("F");
			semantic.addErrorHandler("sample", null);
			// syntax.infer("E -> T `PLUS`<+> E | T `MINUS`<-> E | T");
			// syntax.infer("T -> F `TIMES`<*> T | F `DIVIDE`</> T | F");
			// syntax.infer("F -> `LPA`<(> E `RPA`<)>  | `SYMBOL`<i>");
			semantic.infer("S -> E");
			semantic.infer("E -> E @PLUS<+> T");
			semantic.infer("E -> T");
			semantic.infer("T -> T @TIMES<*> F");
			semantic.infer("T -> F");
			semantic.infer("F -> @v<v>");
			semantic.infer("F -> @d<d>");
			semantic.infer("F -> @LPA@<(> E @RPA<)>");
			semantic.initialize("S");
			System.out.println(semantic.toString());
			System.out.println(semantic.getNGAString());
			System.out.println(semantic.getNPAString());
			System.out.println(semantic.getInst());
			System.out.println(semantic.getTrackerError());
			System.out.println(semantic.getTokenList());
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
