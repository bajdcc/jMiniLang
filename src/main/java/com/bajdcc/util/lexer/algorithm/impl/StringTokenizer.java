package com.bajdcc.util.lexer.algorithm.impl;

import com.bajdcc.util.lexer.algorithm.TokenAlgorithm;
import com.bajdcc.util.lexer.algorithm.filter.StringFilter;
import com.bajdcc.util.lexer.error.RegexException;
import com.bajdcc.util.lexer.regex.IRegexStringIterator;
import com.bajdcc.util.lexer.token.MetaType;
import com.bajdcc.util.lexer.token.Token;
import com.bajdcc.util.lexer.token.TokenType;

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
	 * @see com.bajdcc.lexer.algorithm.ITokenAlgorithm#getToken(java.lang.String, com.bajdcc.lexer.token.Token)
	 */
	@Override
	public Token getToken(String string, Token token, IRegexStringIterator iterator) {
		token.kToken = TokenType.STRING;
		token.object = string;
		return token;
	}
}
