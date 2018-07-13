package com.bajdcc.LL1.grammar;

import com.bajdcc.LALR1.semantic.lexer.TokenFactory;
import com.bajdcc.LL1.grammar.error.GrammarException;
import com.bajdcc.LL1.syntax.Syntax;
import com.bajdcc.LL1.syntax.handler.SyntaxException;
import com.bajdcc.LL1.syntax.prediction.PredictionTable;
import com.bajdcc.util.lexer.error.RegexException;
import com.bajdcc.util.lexer.token.Token;
import com.bajdcc.util.lexer.token.TokenType;

import java.util.ArrayList;

/**
 * 【语法分析】语法分析
 *
 * @author bajdcc
 */
public class Grammar extends Syntax {

	/**
	 * 单词流工厂
	 */
	private TokenFactory tokenFactory;

	/**
	 * 单词流
	 */
	private ArrayList<Token> arrTokens = null;

	/**
	 * 预测分析表
	 */
	private PredictionTable table = null;

	public Grammar(String context) throws RegexException {
		super(true);
		tokenFactory = new TokenFactory(context);// 用于分析的文本
		tokenFactory.discard(TokenType.COMMENT);
		tokenFactory.discard(TokenType.WHITESPACE);
		tokenFactory.discard(TokenType.ERROR);
		tokenFactory.discard(TokenType.MACRO);
		tokenFactory.scan();
	}

	/**
	 * 初始化
	 *
	 * @param startSymbol 开始符号
	 * @throws SyntaxException 词法错误
	 */
	public void initialize(String startSymbol) throws SyntaxException {
		super.initialize(startSymbol);
		generateTable();
	}

	/**
	 * 生成预测分析表
	 */
	private void generateTable() {
		table = new PredictionTable(arrNonTerminals, arrTerminals,
				mapNonTerminals.get(beginRuleName).rule,
				mapTerminals.get(epsilonName), tokenFactory);
	}

	/**
	 * 进行语法分析
	 *
	 * @throws GrammarException 语法错误
	 */
	public void run() throws GrammarException {
		table.run();
		arrTokens = tokenFactory.tokenList();
	}

	/**
	 * 获得预测分析表描述
	 *
	 * @return 预测分析表描述
	 */
	public String getPredictionString() {
		return table.toString();
	}

	/**
	 * 获得单词流描述
	 *
	 * @return 单词流描述
	 */
	public String getTokenString() {
		StringBuilder sb = new StringBuilder();
		sb.append("#### 单词流 ####");
		sb.append(System.lineSeparator());
		for (Token token : arrTokens) {
			sb.append(token.toString());
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
}
