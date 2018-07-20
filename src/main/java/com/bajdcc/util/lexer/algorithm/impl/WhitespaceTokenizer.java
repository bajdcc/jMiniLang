package com.bajdcc.util.lexer.algorithm.impl;

import com.bajdcc.util.lexer.algorithm.TokenAlgorithm;
import com.bajdcc.util.lexer.error.RegexException;
import com.bajdcc.util.lexer.regex.IRegexStringIterator;
import com.bajdcc.util.lexer.token.Token;
import com.bajdcc.util.lexer.token.TokenType;

/**
 * 空白字符解析
 *
 * @author bajdcc
 */
public class WhitespaceTokenizer extends TokenAlgorithm {

	public WhitespaceTokenizer() throws RegexException {
		super(getRegexString(), null);
	}

	public static String getRegexString() {
		return "\\s+";
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
		token.setType(TokenType.WHITESPACE);
		return token;
	}
}
