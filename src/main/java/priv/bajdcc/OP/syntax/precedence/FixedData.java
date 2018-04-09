package priv.bajdcc.OP.syntax.precedence;

import priv.bajdcc.util.lexer.token.Token;

/**
 * 混合数据结构
 *
 * @author bajdcc
 */
public class FixedData {

	/**
	 * 单词
	 */
	public Token token = null;

	/**
	 * 数据
	 */
	public Object obj = null;

	public FixedData() {

	}

	public FixedData(Token token) {
		this.token = token;
	}

	public FixedData(Object obj) {
		this.obj = obj;
	}

	@Override
	public String toString() {
		if (token != null) {
			return token.toString();
		} else if (obj != null) {
			return obj.toString();
		} else {
			return "(null)";
		}
	}
}
