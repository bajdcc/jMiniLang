package com.bajdcc.util.lexer.algorithm;

import com.bajdcc.util.lexer.regex.IRegexStringIterator;

/**
 * 向前进一步的错误处理器
 *
 * @author bajdcc
 */
public class TokenErrorAdvanceHandler extends TokenErrorHandler {

	public TokenErrorAdvanceHandler(IRegexStringIterator iterator) {
		super(iterator);
	}

	@Override
	public void handleError() {
		iterator.advance();
	}
}