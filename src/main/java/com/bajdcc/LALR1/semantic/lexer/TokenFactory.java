package com.bajdcc.LALR1.semantic.lexer;

import com.bajdcc.util.lexer.Lexer;
import com.bajdcc.util.lexer.error.RegexException;
import com.bajdcc.util.lexer.regex.IRegexStringIterator;
import com.bajdcc.util.lexer.token.Token;

import java.util.ArrayList;

/**
 * 词法分析器
 *
 * @author bajdcc
 */
public class TokenFactory extends Lexer {

	/**
	 * 保存当前分析的单词流
	 */
	private ArrayList<Token> arrTokens = new ArrayList<>();

	public TokenFactory(String context) throws RegexException {
		super(context);
	}

	@Override
	public IRegexStringIterator copy() {
		TokenFactory o = (TokenFactory) super.clone();
		o.arrTokens = new ArrayList<>(arrTokens);
		return o;
	}

	@Override
	public ArrayList<Token> tokenList() {
		return arrTokens;
	}

	@Override
	public void saveToken() {
		arrTokens.add(getToken());
	}
}
