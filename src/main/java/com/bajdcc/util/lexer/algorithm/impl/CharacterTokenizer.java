package com.bajdcc.util.lexer.algorithm.impl;

import com.bajdcc.util.lexer.algorithm.TokenAlgorithm;
import com.bajdcc.util.lexer.algorithm.filter.CharacterFilter;
import com.bajdcc.util.lexer.error.RegexException;
import com.bajdcc.util.lexer.regex.IRegexStringIterator;
import com.bajdcc.util.lexer.token.Token;
import com.bajdcc.util.lexer.token.TokenType;

/**
 * 字符解析
 *
 * @author bajdcc
 */
public class CharacterTokenizer extends TokenAlgorithm {

	public CharacterTokenizer() throws RegexException {
		super(getRegexString(), new CharacterFilter());
	}

	public static String getRegexString() {
		return "\'.\'";
	}

	/* （非 Javadoc）
	 * @see com.bajdcc.lexer.algorithm.ITokenAlgorithm#getToken(java.lang.String, com.bajdcc.lexer.token.Token)
	 */
	@Override
	public Token getToken(String string, Token token, IRegexStringIterator iterator) {
		token.setType(TokenType.CHARACTER);
		token.setObj(string.charAt(0));
		return token;
	}
}
