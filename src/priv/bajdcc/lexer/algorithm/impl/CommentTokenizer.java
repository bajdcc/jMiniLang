package priv.bajdcc.lexer.algorithm.impl;

import priv.bajdcc.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.lexer.token.TokenType;

/**
 * 注释解析
 * 
 * @author bajdcc
 *
 */
public class CommentTokenizer extends TokenAlgorithm {

	public CommentTokenizer() throws RegexException {
		super(getRegexString(), null);
	}

	public static String getRegexString() {
		return "((//[^\\r\\n]*[\\r\\n]{1,2})|(/\\*.*\\*/))";
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
		token.m_kToken = TokenType.COMMENT;
		token.m_Object = string.trim();
		return token;
	}
}
