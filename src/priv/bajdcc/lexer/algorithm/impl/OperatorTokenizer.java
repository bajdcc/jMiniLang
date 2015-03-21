package priv.bajdcc.lexer.algorithm.impl;

import java.util.HashMap;

import priv.bajdcc.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.token.MetaType;
import priv.bajdcc.lexer.token.OperatorType;
import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.lexer.token.TokenType;

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
	private HashMap<String, OperatorType> m_HashOperator = new HashMap<String, OperatorType>();

	public OperatorTokenizer() throws RegexException {
		super(getRegexString(), null);
		initializeHashMap();
	}

	public static String getRegexString() {
		String exp = "=|+|-|*|/|%|&|\\||~|!|<|>|(|)|{|}|[|]|,|.|;|:|==|!=|++|--|+=|-=|*=|/=|<=|>=|&&|\\|\\|";
		MetaType[] metaTypes = new MetaType[] { MetaType.LPARAN,
				MetaType.RPARAN, MetaType.STAR, MetaType.PLUS,
				MetaType.LSQUARE, MetaType.RSQUARE, MetaType.LBRACE,
				MetaType.RBRACE, MetaType.DOT };
		for (MetaType meta : metaTypes) {
			exp = exp.replace(meta.getChar() + "", "\\" + meta.getChar());
		}
		return exp;
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
			m_HashOperator.put(operator.getName(), operator);
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
		token.m_kToken = TokenType.OPERATOR;
		token.m_Object = m_HashOperator.get(string);
		return token;
	}
}
