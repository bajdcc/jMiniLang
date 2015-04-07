package priv.bajdcc.semantic.token;

import priv.bajdcc.lexer.token.Token;

/**
 * 单词包
 *
 * @author bajdcc
 */
public class TokenBag {

	/**
	 * 保存的单词
	 */
	public Token m_Token = null;

	/**
	 * 保存的对象
	 */
	public Object m_Object = null;

	public TokenBag() {
		
	}
	
	public TokenBag(Token token) {
		m_Token = token;
	}
	
	public TokenBag(Object obj) {
		m_Object = obj;
	}
}
