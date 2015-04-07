package priv.bajdcc.semantic.test;

import java.util.Scanner;

import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.regex.IRegexStringIterator;
import priv.bajdcc.lexer.token.OperatorType;
import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.lexer.token.TokenType;
import priv.bajdcc.semantic.Semantic;
import priv.bajdcc.semantic.token.IIndexedData;
import priv.bajdcc.semantic.token.ISemanticHandler;
import priv.bajdcc.semantic.token.ITokenFactory;
import priv.bajdcc.semantic.token.TokenBag;
import priv.bajdcc.syntax.Syntax;
import priv.bajdcc.syntax.error.IErrorHandler;
import priv.bajdcc.syntax.error.SyntaxException;
import priv.bajdcc.utility.TrackerErrorBag;

@SuppressWarnings("unused")
public class TestOperator {

	public static void main(String[] args) {
		// System.out.println("Z -> `a`<,> | B | [`a` `b` Z B]");
		try {
			// Scanner scanner = new Scanner(System.in);
			// String expr = "( i )";
			//String expr = "((3))+4*5+66/(3-8)";
			String expr = "(4 * 7 + 35) * (3 + 18 / 3 - 3)";
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
			semantic.addTerminal("INTEGER", TokenType.INTEGER, null);
			semantic.addNonTerminal("Z");
			semantic.addNonTerminal("E");
			semantic.addNonTerminal("T");
			semantic.addNonTerminal("F");
			semantic.addErrorHandler("lost_exp", new IErrorHandler() {
				@Override
				public String handle(IRegexStringIterator iterator,
						TrackerErrorBag bag) {
					bag.m_bRead = false;
					bag.m_bPass = true;
					return "表达式不完整";
				}
			});
			semantic.addErrorHandler("lost_exp_right", new IErrorHandler() {
				@Override
				public String handle(IRegexStringIterator iterator,
						TrackerErrorBag bag) {
					bag.m_bRead = false;
					bag.m_bPass = true;
					return "缺少右括号";
				}
			});
			ISemanticHandler handleCopy = new ISemanticHandler() {
				@Override
				public Object handle(IIndexedData indexed,
						ITokenFactory factory, Object obj) {
					return indexed.get(0).m_Object;
				}
			};
			ISemanticHandler handleBinop = new ISemanticHandler() {
				@Override
				public Object handle(IIndexedData indexed,
						ITokenFactory factory, Object obj) {
					int lop = Integer.parseInt(indexed.get(0).m_Object
							.toString());
					int rop = Integer.parseInt(indexed.get(2).m_Object
							.toString());
					Token op = indexed.get(1).m_Token;
					if (op.m_kToken == TokenType.OPERATOR) {
						OperatorType kop = (OperatorType) op.m_Object;
						switch (kop) {
						case PLUS:
							return lop + rop;
						case MINUS:
							return lop - rop;
						case TIMES:
							return lop * rop;
						case DIVIDE:
							return lop / rop;
						default:
							return 0;
						}
					} else {
						return 0;
					}
				}
			};
			ISemanticHandler handleValue = new ISemanticHandler() {
				@Override
				public Object handle(IIndexedData indexed,
						ITokenFactory factory, Object obj) {
					return indexed.get(0).m_Token.m_Object;
				}
			};
			// syntax.infer("E -> T `PLUS`<+> E | T `MINUS`<-> E | T");
			// syntax.infer("T -> F `TIMES`<*> T | F `DIVIDE`</> T | F");
			// syntax.infer("F -> `LPA`<(> E `RPA`<)>  | `SYMBOL`<i>");
			semantic.infer(handleCopy, "Z -> E[0]");
			semantic.infer(handleCopy, "E -> T[0]");
			semantic.infer(handleBinop, "E -> E[0] ( `PLUS`[1]<+> | `MINUS`[1]<-> ) T[2]{lost_exp}");
			semantic.infer(handleCopy, "T -> F[0]");
			semantic.infer(handleBinop, "T -> T[0] ( `TIMES`[1]<*> |`DIVIDE`[1]</> ) F[2]{lost_exp}");
			semantic.infer(handleValue, "F -> `INTEGER`[0]<integer>");
			semantic.infer(handleCopy, "F -> `LPA`<(> E[0]{lost_exp} `RPA`{lost_exp_right}<)>");
			semantic.initialize("Z");
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
