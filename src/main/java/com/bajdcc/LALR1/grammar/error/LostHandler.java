package com.bajdcc.LALR1.grammar.error;

import com.bajdcc.LALR1.syntax.handler.IErrorHandler;
import com.bajdcc.util.TrackerErrorBag;
import com.bajdcc.util.lexer.regex.IRegexStringIterator;

/**
 * 【语法分析】应对丢失符号的错误处理器
 *
 * @author bajdcc
 */
public class LostHandler implements IErrorHandler {

	/**
	 * 错误信息
	 */
	private String message;

	public LostHandler(String message) {
		this.message = message;
	}

	@Override
	public String handle(IRegexStringIterator iterator, TrackerErrorBag bag) {
		bag.setRead(false);
		bag.setPass(true);
		String def = "缺少：" + message + System.lineSeparator();
		String snapshot = iterator.ex().getErrorSnapshot(bag.getPosition());
		if (snapshot != null) {
			return def + snapshot;
		}
		return def;
	}
}
