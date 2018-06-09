package com.bajdcc.LALR1.semantic.token;

import com.bajdcc.util.lexer.token.Token;

/**
 * 【语义分析】随机访问单词流
 *
 * @author bajdcc
 */
public interface IRandomAccessOfTokens {

	/**
	 * 获取相对位置的单词
	 *
	 * @param index 索引
	 * @return 单词
	 */
	Token relativeGet(int index);

	/**
	 * 设置相对位置的单词
	 *
	 * @param index 索引
	 * @param token 单词
	 */
	void relativeSet(int index, Token token);

	/**
	 * 获取绝对位置的单词
	 *
	 * @param index 索引
	 * @return 单词
	 */
	Token positiveGet(int index);

	/**
	 * 设置绝对位置的单词
	 *
	 * @param index 索引
	 * @param token 单词
	 */
	void positiveSet(int index, Token token);
}
