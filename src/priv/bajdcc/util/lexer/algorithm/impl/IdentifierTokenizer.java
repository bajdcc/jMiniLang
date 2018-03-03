package priv.bajdcc.util.lexer.algorithm.impl;

import java.util.HashMap;

import priv.bajdcc.util.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.regex.IRegexStringIterator;
import priv.bajdcc.util.lexer.token.KeywordType;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

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
	private HashMap<String, KeywordType> mapKeywords = new HashMap<>();

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
	public Token getToken(String string, Token token, IRegexStringIterator iterator) {
		if (mapKeywords.containsKey(string)) {
			KeywordType kw = mapKeywords.get(string);
			switch (kw) {
			case TRUE:
				token.kToken = TokenType.BOOL;
				token.object = true;
				break;
			case FALSE:
				token.kToken = TokenType.BOOL;
				token.object = false;
				break;
			default:
				token.kToken = TokenType.KEYWORD;
				token.object = kw;
				break;
			}
		} else {
			token.kToken = TokenType.ID;
			token.object = string;
		}
		return token;
	}
}
