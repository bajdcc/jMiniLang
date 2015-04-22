package priv.bajdcc.util.lexer.algorithm;

import priv.bajdcc.util.lexer.error.IErrorHandler;
import priv.bajdcc.util.lexer.regex.IRegexStringIterator;

/**
 * 错误处理器基类
 * 
 * @author bajdcc
 *
 */
public abstract class TokenErrorHandler implements IErrorHandler {
	
	/**
	 * 迭代器接口
	 */
	protected IRegexStringIterator iterator = null;

	public TokenErrorHandler(IRegexStringIterator iterator) {
		this.iterator = iterator;
	}
}
