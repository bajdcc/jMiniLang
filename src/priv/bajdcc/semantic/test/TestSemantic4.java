package priv.bajdcc.semantic.test;

import java.util.Scanner;

import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.token.OperatorType;
import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.lexer.token.TokenType;
import priv.bajdcc.semantic.Semantic;
import priv.bajdcc.semantic.token.IIndexedData;
import priv.bajdcc.semantic.token.ISemanticHandler;
import priv.bajdcc.semantic.token.ITokenFactory;
import priv.bajdcc.syntax.Syntax;
import priv.bajdcc.syntax.error.SyntaxException;

@SuppressWarnings("unused")
public class TestSemantic4 {

	public static void main(String[] args) {
		// System.out.println("Z -> `a`<,> | B | [`a` `b` Z B]");
		try {
			// Scanner scanner = new Scanner(System.in);
			// String expr = "( i )";
			String expr = "a b c a";
			Semantic semantic = new Semantic(expr);
			semantic.addTerminal("a", TokenType.ID, "a");
			semantic.addTerminal("b", TokenType.ID, "b");
			semantic.addTerminal("c", TokenType.ID, "c");
			semantic.addNonTerminal("START");			
			// syntax.infer("E -> T `PLUS`<+> E | T `MINUS`<-> E | T");
			// syntax.infer("T -> F `TIMES`<*> T | F `DIVIDE`</> T | F");
			// syntax.infer("F -> `LPA`<(> E `RPA`<)>  | `SYMBOL`<i>");
			semantic.infer("START -> @a [@b] [@c] @a");
			semantic.initialize("START");
			System.out.println(semantic.toString());
			System.out.println(semantic.getNGAString());
			System.out.println(semantic.getNPAString());
			System.out.println(semantic.getInst());
			System.out.println(semantic.getError());
			System.out.println(semantic.getTokenList());
			System.out.println(semantic.getObject());
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
