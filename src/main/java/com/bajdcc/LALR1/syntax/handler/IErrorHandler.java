package com.bajdcc.LALR1.syntax.handler;

import com.bajdcc.util.TrackerErrorBag;
import com.bajdcc.util.lexer.regex.IRegexStringIterator;

/**
 * 语法错误处理接口
 *
 * @author bajdcc
 */
public interface IErrorHandler {
	/**
	 * 处理错误
	 *
	 * @param iterator 迭代器
	 * @param bag      参数信息
	 * @return 错误信息
	 */
	String handle(IRegexStringIterator iterator, TrackerErrorBag bag);
}
