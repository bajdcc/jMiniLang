package priv.bajdcc.syntax.lexer.tokenizer;

import priv.bajdcc.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.lexer.algorithm.filter.StringFilter;
import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.token.MetaType;
import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.lexer.token.TokenType;

/**
 * ÖÕ½á·û½âÎö
 * 
 * @author bajdcc
 *
 */
public class TerminalTokenizer extends TokenAlgorithm {

	public TerminalTokenizer() throws RegexException {
		super(getRegexString(), new StringFilter(MetaType.TERMINAL));
	}

	public static String getRegexString() {
		return "`.*`";
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
