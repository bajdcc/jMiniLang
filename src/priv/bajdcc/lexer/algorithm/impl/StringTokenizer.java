package priv.bajdcc.lexer.algorithm.impl;

import priv.bajdcc.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.lexer.algorithm.filter.StringFilter;
import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.lexer.token.TokenType;

/**
 * ×Ö·û´®½âÎö
 * 
 * @author bajdcc
 *
 */
public class StringTokenizer extends TokenAlgorithm {

	public StringTokenizer() throws RegexException {
		super(getRegexString(), new StringFilter());
	}

	public static String getRegexString() {
		return "\"[^\"]*\"";
	}

	/* £¨·Ç Javadoc£©
	 * @see priv.bajdcc.lexer.algorithm.ITokenAlgorithm#getToken(java.lang.String, priv.bajdcc.lexer.token.Token)
	 */
	@Override
	public Token getToken(String string, Token token) {
		token.m_kToken = TokenType.STRING;
		token.m_Object = string;
		return token;
	}
}
