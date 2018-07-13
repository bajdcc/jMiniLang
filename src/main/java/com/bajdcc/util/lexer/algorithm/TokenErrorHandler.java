package com.bajdcc.util.lexer.algorithm;

import com.bajdcc.util.lexer.error.IErrorHandler;
import com.bajdcc.util.lexer.regex.IRegexStringIterator;

/**
 * 错误处理器基类
 *
 * @author bajdcc
 */
public abstract class TokenErrorHandler implements IErrorHandler {

	/**
	 * 迭代器接口
	 */
	protected IRegexStringIterator iterator;

	public TokenErrorHandler(IRegexStringIterator iterator) {
		this.iterator = iterator;
	}
}
