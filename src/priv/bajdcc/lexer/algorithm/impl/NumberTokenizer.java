package priv.bajdcc.lexer.algorithm.impl;

import priv.bajdcc.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.lexer.token.TokenType;

/**
 * Êý×Ö½âÎö
 * 
 * @author bajdcc
 *
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
	 * £¨·Ç Javadoc£©
	 * 
	 * @see
	 * priv.bajdcc.lexer.algorithm.ITokenAlgorithm#getToken(java.lang.String,
	 * priv.bajdcc.lexer.token.Token)
	 */
	@Override
	public Token getToken(String string, Token token) {
		try {
			token.m_kToken = TokenType.INTEGER;
			token.m_Object = Integer.parseInt(string);
			return token;
		} catch (NumberFormatException e) {
			token.m_kToken = TokenType.REAL;
			token.m_Object = Double.parseDouble(string);
		}
		return token;
	}
}
