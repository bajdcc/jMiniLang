package priv.bajdcc.LALR1.grammar.error;

import priv.bajdcc.LALR1.syntax.handler.IErrorHandler;
import priv.bajdcc.util.TrackerErrorBag;
import priv.bajdcc.util.lexer.regex.IRegexStringIterator;

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
		String def = "缺少：" + message + System.lineSeparator();
		String snapshot = iterator.ex().getErrorSnapshot(iterator.position());
		if (snapshot != null) {
			return def + snapshot;
		}
		return def;
	}
}
