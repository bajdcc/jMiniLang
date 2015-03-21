package priv.bajdcc.lexer.regex;

import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.error.RegexException.RegexError;
import priv.bajdcc.lexer.token.MetaType;
import priv.bajdcc.lexer.utility.Position;

/**
 * 字符串迭代器接口
 * 
 * @author bajdcc
 *
 */
public interface IRegexStringIterator {

	/**
	 * 抛出错误
	 * 
	 * @param error
	 *            错误类型
	 * @throws RegexException
	 */
	public abstract void err(RegexError error) throws RegexException;

	/**
	 * 处理下一个字符
	 */
	public abstract void next();

	/**
	 * 翻译当前字符
	 */
	public abstract void translate();

	/**
	 * 判断当前位置不是末尾
	 * 
	 */
	public abstract boolean available();

	/**
	 * 前进一个字符（look forward）
	 * 
	 */
	public abstract void advance();

	/**
	 * 获得当前字符
	 * 
	 */
	public abstract char current();

	/**
	 * 获得字符类型
	 */
	public abstract MetaType meta();

	/**
	 * 获得当前位置
	 */
	public abstract int index();

	/**
	 * 获得当前位置
	 */
	public abstract Position position();

	/**
	 * 确认当前字符
	 * 
	 * @param meta
	 *            类型
	 * @param error
	 *            抛出的错误
	 * @throws RegexException
	 */
	public abstract void expect(MetaType meta, RegexError error)
			throws RegexException;

	/**
	 * 保存当前位置
	 */
	public abstract void snapshot();

	/**
	 * 覆盖当前位置
	 */
	public abstract void cover();

	/**
	 * 恢复至上次位置
	 */
	public abstract void restore();

	/**
	 * 丢弃上次位置
	 */
	public abstract void discard();

	/**
	 * 获得解析组件
	 */
	public abstract RegexStringUtility utility();
}