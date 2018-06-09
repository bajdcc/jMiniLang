package com.bajdcc.util.lexer.regex;

/**
 * 字符串过滤接口
 *
 * @author bajdcc
 */
public interface IRegexStringFilter {

	/**
	 * 过滤
	 *
	 * @param iterator 迭代器
	 * @return 过滤结果
	 */
	RegexStringIteratorData filter(IRegexStringIterator iterator);


	/**
	 * 返回类型过滤接口
	 *
	 * @return 类型过滤接口
	 */
	IRegexStringFilterMeta getFilterMeta();
}
