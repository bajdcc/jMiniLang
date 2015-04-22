package priv.bajdcc.util.lexer.algorithm.impl;

import java.util.HashMap;

import priv.bajdcc.util.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.token.MetaType;
import priv.bajdcc.util.lexer.token.OperatorType;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

/**
 * 操作符解析
 * 
 * @author bajdcc
 *
 */
public class OperatorTokenizer extends TokenAlgorithm {

	/**
	 * 关键字哈希表
	 */
	private HashMap<String, OperatorType> hashOperator = new HashMap<String, OperatorType>();

	public OperatorTokenizer() throws RegexException {
		super(getRegexString(), null);
		initializeHashMap();
	}

	public static String getRegexString() {
		MetaType[] metaTypes = new MetaType[] { MetaType.LPARAN,
				MetaType.RPARAN, MetaType.STAR, MetaType.PLUS,
				MetaType.LSQUARE, MetaType.RSQUARE, MetaType.LBRACE,
				MetaType.RBRACE, MetaType.DOT, MetaType.BAR, MetaType.QUERY };
		StringBuilder sb = new StringBuilder();
		for (OperatorType type : OperatorType.values()) {
			String op = type.getName();
			for (MetaType meta : metaTypes) {
				op = op.replace(meta.getChar() + "", "\\" + meta.getChar());
			}
			sb.append(op + "|");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	@Override
	public boolean getGreedMode() {
		return true;
	}

	/**
	 * 初始化关键字哈希表
	 */
	private void initializeHashMap() {
		for (OperatorType operator : OperatorType.values()) {// 关键字
			hashOperator.put(operator.getName(), operator);
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * priv.bajdcc.lexer.algorithm.ITokenAlgorithm#getToken(java.lang.String,
	 * priv.bajdcc.lexer.token.Token)
	 */
	@Override
	public Token getToken(String string, Token token) {
		token.kToken = TokenType.OPERATOR;
		token.object = hashOperator.get(string);
		return token;
	}
}
