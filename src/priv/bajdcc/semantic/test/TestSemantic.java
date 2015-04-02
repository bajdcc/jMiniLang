package priv.bajdcc.semantic.test;

import java.util.Scanner;

import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.token.OperatorType;
import priv.bajdcc.lexer.token.TokenType;
import priv.bajdcc.semantic.Semantic;
import priv.bajdcc.syntax.Syntax;
import priv.bajdcc.syntax.error.SyntaxException;

@SuppressWarnings("unused")
public class TestSemantic {

	public static void main(String[] args) {
		// System.out.println("Z -> `a`<,> | B | [`a` `b` Z B]");
		try {
			// Scanner scanner = new Scanner(System.in);
			// String expr = "( i )";
			String expr = "i";
			Semantic semantic = new Semantic(expr);
			semantic.addTerminal("PLUS", TokenType.OPERATOR, OperatorType.PLUS);
			semantic.addTerminal("MINUS", TokenType.OPERATOR,
					OperatorType.MINUS);
			semantic.addTerminal("TIMES", TokenType.OPERATOR,
					OperatorType.TIMES);
			semantic.addTerminal("DIVIDE", TokenType.OPERATOR,
					OperatorType.DIVIDE);
			semantic.addTerminal("LPA", TokenType.OPERATOR, OperatorType.LPARAN);
			semantic.addTerminal("RPA", TokenType.OPERATOR, OperatorType.RPARAN);
			semantic.addTerminal("SYMBOL", TokenType.ID, "i");
			semantic.addNonTerminal("E");
			semantic.addNonTerminal("T");
			semantic.addNonTerminal("F");
			semantic.addErrorHandler("sample", null);
			// syntax.infer("E -> T `PLUS`<+> E | T `MINUS`<-> E | T");
			// syntax.infer("T -> F `TIMES`<*> T | F `DIVIDE`</> T | F");
			// syntax.infer("F -> `LPA`<(> E `RPA`<)>  | `SYMBOL`<i>");
			semantic.infer("E -> T");
			semantic.infer("E -> E `PLUS`<+> T");
			semantic.infer("E -> E `MINUS`<-> T");
			semantic.infer("T -> F");
			semantic.infer("T -> T `TIMES`<*> F");
			semantic.infer("T -> T `DIVIDE`</> F");
			semantic.infer("F -> `SYMBOL`<i>");
			semantic.infer("F -> `LPA`<(> E `RPA`<)>");
			semantic.initialize("E");
			semantic.compile();
			System.out.println(semantic.toString());
			System.out.println(semantic.getNGAString());
			System.out.println(semantic.getNPAString());
			System.out.println(semantic.getInst());
			System.out.println(semantic.getError());
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
