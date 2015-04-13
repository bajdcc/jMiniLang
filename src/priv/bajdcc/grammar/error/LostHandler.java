package priv.bajdcc.grammar.error;

import priv.bajdcc.lexer.regex.IRegexStringIterator;
import priv.bajdcc.syntax.error.IErrorHandler;
import priv.bajdcc.utility.TrackerErrorBag;

/**
 * 【语法分析】应对丢失符号的错误处理器
 *
 * @author bajdcc
 */
public class LostHandler implements IErrorHandler {

	/**
	 * 错误信息
	 */
	private String message = "";

	public LostHandler(String message) {
		this.message = message;
	}

	@Override
	public String handle(IRegexStringIterator iterator, TrackerErrorBag bag) {
		bag.bRead = false;
		bag.bPass = true;
		return "缺少符号：" + message;
	}
}
