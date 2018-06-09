package com.bajdcc.LALR1.semantic.test;

import com.bajdcc.LALR1.semantic.Semantic;
import com.bajdcc.LALR1.syntax.handler.SyntaxException;
import com.bajdcc.util.lexer.error.RegexException;
import com.bajdcc.util.lexer.token.OperatorType;
import com.bajdcc.util.lexer.token.TokenType;

@SuppressWarnings("unused")
public class TestSemantic {

	public static void main(String[] args) {
		// System.out.println("Z -> `a`<,> | B | [`a` `b` Z B]");
		try {
			// Scanner scanner = new Scanner(System.in);
			// String expr = "( i )";
			String expr = "((i))+i*i+i/(i-i)";
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
			semantic.addNonTerminal("Z");
			semantic.addNonTerminal("E");
			semantic.addNonTerminal("T");
			semantic.addNonTerminal("F");
			semantic.addErrorHandler("sample", null);
			// syntax.infer("E -> T `PLUS`<+> E | T `MINUS`<-> E | T");
			// syntax.infer("T -> F `TIMES`<*> T | F `DIVIDE`</> T | F");
			// syntax.infer("F -> `LPA`<(> E `RPA`<)>  | `SYMBOL`<i>");
			semantic.infer("Z -> E");
			semantic.infer("E -> T");
			semantic.infer("E -> E @PLUS<+> T");
			semantic.infer("E -> E @MINUS<-> T");
			semantic.infer("T -> F");
			semantic.infer("T -> T @TIMES<*> F");
			semantic.infer("T -> T @DIVIDE</> F");
			semantic.infer("F -> @SYMBOL<i>");
			semantic.infer("F -> @LPA<(> E @RPA<)>");
			semantic.initialize("Z");
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
