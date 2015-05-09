package priv.bajdcc.util.lexer.algorithm.impl;

import priv.bajdcc.util.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.util.lexer.algorithm.filter.CharacterFilter;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

/**
 * 字符解析
 * 
 * @author bajdcc
 *
 */
public class CharacterTokenizer extends TokenAlgorithm {

	public CharacterTokenizer() throws RegexException {
		super(getRegexString(), new CharacterFilter());
	}

	public static String getRegexString() {
		return "\'.\'";
	}

	/* （非 Javadoc）
	 * @see priv.bajdcc.lexer.algorithm.ITokenAlgorithm#getToken(java.lang.String, priv.bajdcc.lexer.token.Token)
	 */
	@Override
	public Token getToken(String string, Token token) {
		token.kToken = TokenType.CHARACTER;
		token.object = string.charAt(0);
		return token;
	}
}
