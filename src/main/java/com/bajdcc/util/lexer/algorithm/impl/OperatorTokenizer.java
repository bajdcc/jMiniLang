package com.bajdcc.util.lexer.algorithm.impl;

import com.bajdcc.util.lexer.algorithm.TokenAlgorithm;
import com.bajdcc.util.lexer.error.RegexException;
import com.bajdcc.util.lexer.regex.IRegexStringIterator;
import com.bajdcc.util.lexer.token.MetaType;
import com.bajdcc.util.lexer.token.OperatorType;
import com.bajdcc.util.lexer.token.Token;
import com.bajdcc.util.lexer.token.TokenType;

import java.util.HashMap;

/**
 * 操作符解析
 *
 * @author bajdcc
 */
public class OperatorTokenizer extends TokenAlgorithm {

	/**
	 * 关键字哈希表
	 */
	private HashMap<String, OperatorType> hashOperator = new HashMap<>();

	public OperatorTokenizer() throws RegexException {
		super(getRegexString(), null);
		initializeHashMap();
	}

	public static String getRegexString() {
		MetaType[] metaTypes = new MetaType[]{MetaType.LPARAN,
				MetaType.RPARAN, MetaType.STAR, MetaType.PLUS,
				MetaType.LSQUARE, MetaType.RSQUARE, MetaType.LBRACE,
				MetaType.RBRACE, MetaType.DOT, MetaType.BAR, MetaType.QUERY};
		StringBuilder sb = new StringBuilder();
		for (OperatorType type : OperatorType.values()) {
			String op = type.getName();
			for (MetaType meta : metaTypes) {
				op = op.replace(meta.getChar() + "", "\\" + meta.getChar());
			}
			if (type == OperatorType.ESCAPE)
				op += op;
			sb.append(op).append("|");
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
	 * com.bajdcc.lexer.algorithm.ITokenAlgorithm#getToken(java.lang.String,
	 * com.bajdcc.lexer.token.Token)
	 */
	@Override
	public Token getToken(String string, Token token, IRegexStringIterator iterator) {
		token.kToken = TokenType.OPERATOR;
		OperatorType op = hashOperator.get(string);
		if (op == OperatorType.MINUS) {
			if (iterator.available()) {
				char ch = iterator.current();
				if (ch == '.' || Character.isDigit(ch)) { // 判定是数字
					return null;
				}
			}
		}
		token.object = op;
		return token;
	}
}
