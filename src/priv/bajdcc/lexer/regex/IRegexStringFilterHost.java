package priv.bajdcc.lexer.regex;

import priv.bajdcc.lexer.algorithm.ITokenAlgorithm;

/**
 * 字符串过滤主体
 * 
 * @author bajdcc
 *
 */
public interface IRegexStringFilterHost {
	/**
	 * 设置字符转换算法
	 * @param alg 字符转换算法
	 */
	public void setFilter(ITokenAlgorithm alg);
}
