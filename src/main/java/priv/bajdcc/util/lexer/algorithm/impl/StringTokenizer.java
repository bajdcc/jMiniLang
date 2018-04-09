package priv.bajdcc.util.lexer.algorithm.impl;

import priv.bajdcc.util.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.util.lexer.algorithm.filter.StringFilter;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.regex.IRegexStringIterator;
import priv.bajdcc.util.lexer.token.MetaType;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

/**
 * 字符串解析
 *
 * @author bajdcc
 */
public class StringTokenizer extends TokenAlgorithm {

	public StringTokenizer() throws RegexException {
		super(getRegexString(), new StringFilter(MetaType.DOUBLE_QUOTE));
	}

	public static String getRegexString() {
		return "\".*\"";
	}

	/* （非 Javadoc）
	 * @see priv.bajdcc.lexer.algorithm.ITokenAlgorithm#getToken(java.lang.String, priv.bajdcc.lexer.token.Token)
	 */
	@Override
	public Token getToken(String string, Token token, IRegexStringIterator iterator) {
		token.kToken = TokenType.STRING;
		token.object = string;
		return token;
	}
}
