package com.bajdcc.OP.grammar.test;

import com.bajdcc.OP.grammar.Grammar;
import com.bajdcc.OP.grammar.error.GrammarException;
import com.bajdcc.OP.grammar.handler.IPatternHandler;
import com.bajdcc.OP.syntax.handler.SyntaxException;
import com.bajdcc.util.lexer.error.RegexException;
import com.bajdcc.util.lexer.token.OperatorType;
import com.bajdcc.util.lexer.token.Token;
import com.bajdcc.util.lexer.token.TokenType;

import java.util.List;

@SuppressWarnings("unused")
public class TestGrammar {

	public static void main(String[] args) {
		// System.out.println("Z -> `a`<,> | B | [`a` `b` Z B]");
		try {
			// Scanner scanner = new Scanner(System.in);
			Grammar grammar = new Grammar("3 - (28 / (4 * 7)) * (2 + 4) + 5");
			grammar.addTerminal("i", TokenType.INTEGER, null);
			grammar.addTerminal("PLUS", TokenType.OPERATOR, OperatorType.PLUS);
			grammar.addTerminal("MINUS", TokenType.OPERATOR, OperatorType.MINUS);
			grammar.addTerminal("TIMES", TokenType.OPERATOR, OperatorType.TIMES);
			grammar.addTerminal("DIVIDE", TokenType.OPERATOR,
					OperatorType.DIVIDE);
			grammar.addTerminal("LPA", TokenType.OPERATOR, OperatorType.LPARAN);
			grammar.addTerminal("RPA", TokenType.OPERATOR, OperatorType.RPARAN);
			String[] nons = new String[]{"E", "T", "F"};
			for (String non : nons) {
				grammar.addNonTerminal(non);
			}
			grammar.addPatternHandler("1", new IPatternHandler() {
				@Override
				public Object handle(List<Token> tokens, List<Object> symbols) {
					return Integer.parseInt(tokens.get(0).getObj().toString());
				}

				@Override
				public String getPatternName() {
					return "操作数转换";
				}
			});
			grammar.addPatternHandler("010", new IPatternHandler() {
				@Override
				public Object handle(List<Token> tokens, List<Object> symbols) {
					int lop = (int) symbols.get(0);
					int rop = (int) symbols.get(1);
					Token op = tokens.get(0);
					if (op.getType() == TokenType.OPERATOR) {
						OperatorType kop = (OperatorType) op.getObj();
						switch (kop) {
							case PLUS:
								return lop + rop;
							case MINUS:
								return lop - rop;
							case TIMES:
								return lop * rop;
							case DIVIDE:
								if (rop == 0) {
									return lop;
								} else {
									return lop / rop;
								}
							default:
								return 0;
						}
					} else {
						return 0;
					}
				}

				@Override
				public String getPatternName() {
					return "二元运算";
				}
			});
			grammar.addPatternHandler("101", new IPatternHandler() {
				@Override
				public Object handle(List<Token> tokens, List<Object> symbols) {
					Token ltok = tokens.get(0);
					Token rtok = tokens.get(1);
					Object exp = symbols.get(0);
					if (ltok.getObj() == OperatorType.LPARAN
							&& rtok.getObj() == OperatorType.RPARAN) {// 判断括号
						return exp;
					}
					return null;
				}

				@Override
				public String getPatternName() {
					return "括号运算";
				}
			});
			grammar.infer("E -> E @PLUS T | E @MINUS T | T");
			grammar.infer("T -> T @TIMES F | T @DIVIDE F | F");
			grammar.infer("F -> @LPA E @RPA | @i");
			grammar.initialize("E");
			System.out.println(grammar.getPrecedenceString());
			System.out.println(grammar.toString());
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
