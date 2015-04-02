package priv.bajdcc.syntax.error;

import priv.bajdcc.lexer.regex.IRegexStringIterator;
import priv.bajdcc.utility.TrackerErrorBag;

/**
 * 语法错误处理接口
 *
 * @author bajdcc
 */
public interface IErrorHandler {
	/**
	 * 处理错误
	 * 
	 * @param iterator
	 *            迭代器
	 * @param bag
	 *            参数信息
	 * @return 错误信息
	 */
	public String handle(IRegexStringIterator iterator, TrackerErrorBag bag);
}
