package priv.bajdcc.lexer.algorithm.impl;

import priv.bajdcc.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.lexer.algorithm.filter.CharacterFilter;
import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.lexer.token.TokenType;

/**
 * ×Ö·û½âÎö
 * 
 * @author bajdcc
 *
 */
public class CharacterTokenizer extends TokenAlgorithm {

	public CharacterTokenizer() throws RegexException {
		super(getRegexString(), new CharacterFilter());
	}

	public static String getRegexString() {
		return "\'[^\"]{1}\'";
	}

	/* £¨·Ç Javadoc£©
	 * @see priv.bajdcc.lexer.algorithm.ITokenAlgorithm#getToken(java.lang.String, priv.bajdcc.lexer.token.Token)
	 */
	@Override
	public Token getToken(String string, Token token) {
		token.m_kToken = TokenType.CHARACTER;
		token.m_Object = string;
		return token;
	}
}
