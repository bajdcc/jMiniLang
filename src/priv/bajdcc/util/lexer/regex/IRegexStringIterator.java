package priv.bajdcc.util.lexer.regex;

import priv.bajdcc.util.Position;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.error.RegexException.RegexError;
import priv.bajdcc.util.lexer.token.MetaType;
import priv.bajdcc.util.lexer.token.Token;

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
	void err(RegexError error) throws RegexException;

	/**
	 * 处理下一个字符
	 */
	void next();
	
	/**
	 * 处理下一个字符（会丢弃字符直到获得合法字符）
	 */
	Token scan();

	/**
	 * 翻译当前字符
	 */
	void translate();

	/**
	 * 判断当前位置不是末尾
	 * 
	 */
	boolean available();

	/**
	 * 前进一个字符（look forward）
	 * 
	 */
	void advance();

	/**
	 * 获得当前字符
	 * 
	 */
	char current();

	/**
	 * 获得字符类型
	 */
	MetaType meta();

	/**
	 * 获得当前位置
	 */
	int index();

	/**
	 * 获得当前位置
	 */
	Position position();

	/**
	 * 确认当前字符
	 * 
	 * @param meta
	 *            类型
	 * @param error
	 *            抛出的错误
	 * @throws RegexException
	 */
	void expect(MetaType meta, RegexError error)
			throws RegexException;

	/**
	 * 保存当前位置
	 */
	void snapshot();

	/**
	 * 覆盖当前位置
	 */
	void cover();

	/**
	 * 恢复至上次位置
	 */
	void restore();

	/**
	 * 丢弃上次位置
	 */
	void discard();

	/**
	 * 获得解析组件
	 */
	RegexStringUtility utility();
	
	/**
	 * 获得正则表达式描述
	 */
	String getRegexDescription();
	
	/**
	 * 复制一个对象
	 */
	IRegexStringIterator copy();
	
	/**
	 * 获取扩展接口
	 */
	IRegexStringIteratorEx ex();
}