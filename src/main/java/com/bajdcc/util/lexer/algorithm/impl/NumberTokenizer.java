package com.bajdcc.util.lexer.algorithm.impl;

import com.bajdcc.util.lexer.algorithm.TokenAlgorithm;
import com.bajdcc.util.lexer.error.RegexException;
import com.bajdcc.util.lexer.regex.IRegexStringIterator;
import com.bajdcc.util.lexer.token.Token;
import com.bajdcc.util.lexer.token.TokenType;

import java.math.BigDecimal;


/**
 * 数字解析
 *
 * @author bajdcc
 */
public class NumberTokenizer extends TokenAlgorithm {

	public NumberTokenizer() throws RegexException {
		super(getRegexString(), null);
	}

	public static String getRegexString() {
		return "[+-]?(\\d*\\.?\\d+|\\d+\\.?\\d*)([eE][+-]?\\d+)?";
	}

	@Override
	public boolean getGreedMode() {
		return true;
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
		try {
			BigDecimal decimal = new BigDecimal(string);
			token.object = decimal;
			if (string.indexOf('.') == -1) {
				token.object = decimal.toBigIntegerExact().longValue();
				token.kToken = TokenType.INTEGER;
			} else {
				token.object = decimal.doubleValue();
				token.kToken = TokenType.DECIMAL;
			}
		} catch (ArithmeticException e) {
			token.kToken = TokenType.DECIMAL;
		}
		return token;
	}
}
