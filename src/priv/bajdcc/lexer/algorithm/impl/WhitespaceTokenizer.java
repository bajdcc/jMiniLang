package priv.bajdcc.lexer.algorithm.impl;

import priv.bajdcc.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.lexer.token.TokenType;

/**
 * ¿Õ°××Ö·û½âÎö
 * 
 * @author bajdcc
 *
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
	 * £¨·Ç Javadoc£©
	 * 
	 * @see
	 * priv.bajdcc.lexer.algorithm.ITokenAlgorithm#getToken(java.lang.String,
	 * priv.bajdcc.lexer.token.Token)
	 */
	@Override
	public Token getToken(String string, Token token) {
		token.m_kToken = TokenType.WHITESPACE;
		return token;
	}
}
