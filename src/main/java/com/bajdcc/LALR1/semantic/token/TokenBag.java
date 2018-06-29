package com.bajdcc.LALR1.semantic.token;

import com.bajdcc.util.lexer.token.Token;

/**
 * 单词包
 *
 * @author bajdcc
 */
public class TokenBag {

	/**
	 * 保存的单词
	 */
	private Token token = null;

	/**
	 * 保存的对象
	 */
	private Object object = null;

	public TokenBag() {

	}

	public TokenBag(Token token) {
		this.token = token;
	}

	public TokenBag(Object obj) {
		this.object = obj;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
