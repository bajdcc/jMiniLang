package priv.bajdcc.LALR1.semantic.token;

import priv.bajdcc.util.lexer.token.Token;

/**
 * 单词包
 *
 * @author bajdcc
 */
public class TokenBag {

	/**
	 * 保存的单词
	 */
	public Token token = null;

	/**
	 * 保存的对象
	 */
	public Object object = null;

	public TokenBag() {
		
	}
	
	public TokenBag(Token token) {
		this.token = token;
	}
	
	public TokenBag(Object obj) {
		object = obj;
	}
}
