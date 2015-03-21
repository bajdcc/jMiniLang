package priv.bajdcc.lexer.algorithm;

import priv.bajdcc.lexer.error.IErrorHandler;
import priv.bajdcc.lexer.regex.IRegexStringIterator;

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
	protected IRegexStringIterator m_Iterator = null;

	public TokenErrorHandler(IRegexStringIterator iterator) {
		m_Iterator = iterator;
	}
}
