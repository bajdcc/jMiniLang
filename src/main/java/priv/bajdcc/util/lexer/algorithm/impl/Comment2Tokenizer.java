package priv.bajdcc.util.lexer.algorithm.impl;

import priv.bajdcc.util.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.regex.IRegexStringIterator;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

/**
 * 注释解析
 * 注：不将两种注释形式分开的话，由于正则表达式只支持整体的贪婪或非贪婪模式
 * 因此会将下面这种注释看作贪婪的，所以会导致匹配过度
 *
 * @author bajdcc
 */
public class Comment2Tokenizer extends TokenAlgorithm {

	public Comment2Tokenizer() throws RegexException {
		super(getRegexString(), null);
	}

	public static String getRegexString() {
		return "/\\*.*\\*/";
	}

	@Override
	public boolean getGreedMode() {
		return false;
	}

	/*
	 * （非 Javadoc）
	 *
	 * @see
	 * priv.bajdcc.lexer.algorithm.ITokenAlgorithm#getToken(java.lang.String,
	 * priv.bajdcc.lexer.token.Token)
	 */
	@Override
	public Token getToken(String string, Token token, IRegexStringIterator iterator) {
		token.kToken = TokenType.COMMENT;
		token.object = string.trim();
		return token;
	}
}
