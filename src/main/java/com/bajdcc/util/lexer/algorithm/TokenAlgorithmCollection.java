package com.bajdcc.util.lexer.algorithm;

import com.bajdcc.util.lexer.error.IErrorHandler;
import com.bajdcc.util.lexer.regex.IRegexStringFilterHost;
import com.bajdcc.util.lexer.regex.IRegexStringIterator;
import com.bajdcc.util.lexer.token.Token;
import com.bajdcc.util.lexer.token.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于抽取单词的算法集合（包含数字、字符串等）
 *
 * @author bajdcc
 */
public class TokenAlgorithmCollection implements Cloneable {
	/**
	 * 算法集合
	 */
	private List<ITokenAlgorithm> arrAlgorithms = new ArrayList<>();

	/**
	 * 字符串迭代器
	 */
	private IRegexStringIterator iterator;

	/**
	 * 字符转换主体
	 */
	private IRegexStringFilterHost filterHost;

	/**
	 * 错误处理
	 */
	private IErrorHandler handler;

	public TokenAlgorithmCollection(IRegexStringIterator iterator,
	                                IRegexStringFilterHost filterHost) {
		this.iterator = iterator;
		this.filterHost = filterHost;
		handler = new TokenErrorAdvanceHandler(iterator);
	}

	/**
	 * 添加解析组件
	 *
	 * @param alg 解析组件
	 */
	public void attach(ITokenAlgorithm alg) {
		arrAlgorithms.add(alg);
	}

	/**
	 * 删除解析组件
	 *
	 * @param alg 解析组件
	 */
	public void detach(ITokenAlgorithm alg) {
		arrAlgorithms.remove(alg);
	}

	/**
	 * 清空解析组件
	 */
	public void clear() {
		arrAlgorithms.clear();
	}

	public Token scan() {
		Token token = new Token();
		token.setType(TokenType.ERROR);
		if (!iterator.available()) {
			token.setType(TokenType.EOF);
		} else {
			for (ITokenAlgorithm alg : arrAlgorithms) {
				filterHost.setFilter(alg);
				iterator.translate();
				if (alg.accept(iterator, token))
					return token;
			}
			handler.handleError();
		}
		return token;
	}

	/**
	 * 拷贝构造
	 *
	 * @param iter   迭代器
	 * @param filter 过滤器
	 * @return 拷贝
	 * @throws CloneNotSupportedException 不支持拷贝
	 */
	public TokenAlgorithmCollection copy(IRegexStringIterator iter,
	                                     IRegexStringFilterHost filter) throws CloneNotSupportedException {
		TokenAlgorithmCollection o = (TokenAlgorithmCollection) super.clone();
		o.iterator = iter;
		o.filterHost = filter;
		o.handler = new TokenErrorAdvanceHandler(iter);
		return o;
	}
}
