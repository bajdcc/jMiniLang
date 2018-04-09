package priv.bajdcc.LALR1.syntax.test;

import priv.bajdcc.LALR1.syntax.Syntax;
import priv.bajdcc.LALR1.syntax.handler.SyntaxException;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.token.OperatorType;
import priv.bajdcc.util.lexer.token.TokenType;

@SuppressWarnings("unused")
public class TestSyntax {

	public static void main(String[] args) {
		//System.out.println("Z -> `a`<,> | B | [`a` `b` Z B]");
		try {
			//Scanner scanner = new Scanner(System.in);
			Syntax syntax = new Syntax();
			syntax.addTerminal("PLUS", TokenType.OPERATOR, OperatorType.PLUS);
			syntax.addTerminal("MINUS", TokenType.OPERATOR, OperatorType.MINUS);
			syntax.addTerminal("TIMES", TokenType.OPERATOR, OperatorType.TIMES);
			syntax.addTerminal("DIVIDE", TokenType.OPERATOR, OperatorType.DIVIDE);
			syntax.addTerminal("LPA", TokenType.OPERATOR, OperatorType.LPARAN);
			syntax.addTerminal("RPA", TokenType.OPERATOR, OperatorType.RPARAN);
			syntax.addTerminal("SYMBOL", TokenType.ID, "i");
			syntax.addNonTerminal("E");
			syntax.addNonTerminal("T");
			syntax.addNonTerminal("F");
			syntax.addErrorHandler("sample", null);
			//syntax.infer("E -> T `PLUS`<+> E | T `MINUS`<-> E | T");
			//syntax.infer("T -> F `TIMES`<*> T | F `DIVIDE`</> T | F");
			//syntax.infer("F -> `LPA`<(> E `RPA`<)>  | `SYMBOL`<i>");
			syntax.infer("E -> E @PLUS<+> T");
			syntax.infer("E -> E @MINUS<-> T");
			syntax.infer("E -> T");
			syntax.infer("T -> T @TIMES<*> F");
			syntax.infer("T -> T @DIVIDE</> F");
			syntax.infer("T -> F");
			syntax.infer("F -> @LPA<(> E @RPA<)>");
			syntax.infer("F -> @SYMBOL<i>");
			syntax.initialize("E");
			System.out.println(syntax.toString());
			System.out.println(syntax.getNGAString());
			System.out.println(syntax.getNPAString());
			//scanner.close();
		} catch (RegexException e) {
			System.err.println(e.getPosition() + "," + e.getMessage());
			e.printStackTrace();
		} catch (SyntaxException e) {
			System.err.println(e.getPosition() + "," + e.getMessage() + " " + e.getInfo());
			e.printStackTrace();
		}
	}
}
