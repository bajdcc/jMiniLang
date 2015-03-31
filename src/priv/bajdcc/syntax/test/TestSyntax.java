package priv.bajdcc.syntax.test;

import java.util.Scanner;

import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.syntax.Syntax;
import priv.bajdcc.syntax.error.SyntaxException;

@SuppressWarnings("unused")
public class TestSyntax {

	public static void main(String[] args) {
		//System.out.println("Z -> `a`<,> | B | [`a` `b` Z B]");
		try {
			//Scanner scanner = new Scanner(System.in);
			Syntax syntax = new Syntax();
			syntax.addTerminal("PLUS", "+");
			syntax.addTerminal("MINUS", "-");
			syntax.addTerminal("TIMES", "*");
			syntax.addTerminal("DIVIDE", "/");
			syntax.addTerminal("LPA", "(");
			syntax.addTerminal("RPA", ")");
			syntax.addTerminal("SYMBOL", "i");
			syntax.addNonTerminal("E");
			syntax.addNonTerminal("T");
			syntax.addNonTerminal("F");
			syntax.addErrorHandler("sample", null);
			syntax.infer("E -> T `PLUS`<+> E | T `MINUS`<-> E | T");
			syntax.infer("T -> F `TIMES`<*> T | F `DIVIDE`</> T | F");
			syntax.infer("F -> `LPA`<(> E `RPA`<)>  | `SYMBOL`<i>");
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
