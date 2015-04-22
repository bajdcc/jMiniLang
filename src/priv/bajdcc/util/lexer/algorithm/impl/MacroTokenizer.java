package priv.bajdcc.util.lexer.algorithm.impl;

import priv.bajdcc.util.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.util.lexer.algorithm.filter.LineFilter;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

/**
 * 宏语句解析
 * 
 * @author bajdcc
 *
 */
public class MacroTokenizer extends TokenAlgorithm {

	public MacroTokenizer() throws RegexException {
		super(getRegexString(), new LineFilter());
	}

	public static String getRegexString() {
		return "#(([^\\r\\n])*)([\\r\\n]{1,2})";
	}

	@Override
	public boolean getGreedMode() {
		return true;
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
		token.kToken = TokenType.MACRO;
		token.object = string.trim();
		return token;
	}
}
