package priv.bajdcc.lexer.algorithm.impl;

import java.util.HashMap;

import priv.bajdcc.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.token.KeywordType;
import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.lexer.token.TokenType;

/**
 * 标识符/关键字解析
 * 
 * @author bajdcc
 *
 */
public class IdentifierTokenizer extends TokenAlgorithm {

	/**
	 * 关键字的哈希表
	 */
	private HashMap<String, KeywordType> mapKeywords = new HashMap<String, KeywordType>();

	public IdentifierTokenizer() throws RegexException {
		super(getRegexString(), null);
		initKeywords();
	}

	/**
	 * 初始化关键字哈希表
	 */
	private void initKeywords() {
		for (KeywordType keyword : KeywordType.values()) {// 关键字
			mapKeywords.put(keyword.getName(), keyword);
		}
	}

	public static String getRegexString() {
		return "(\\a|_)\\w*";
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
		if (mapKeywords.containsKey(string)) {
			token.kToken = TokenType.KEYWORD;
			token.object = mapKeywords.get(string);			
		} else {
			token.kToken = TokenType.ID;
			token.object = string;
		}
		return token;
	}
}
