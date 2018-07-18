package com.bajdcc.LALR1.grammar.type;

import com.bajdcc.util.lexer.token.Token;
import com.bajdcc.util.lexer.token.TokenType;

/**
 * 【类型转换】转换为字符类型
 *
 * @author bajdcc
 */
public class ConvertToChar implements ITokenConventer {

	@Override
	public Token convert(Token token) {
		switch (token.kToken) {
			case STRING:
			case DECIMAL:
			case INTEGER:
				token.object = getCharValue(token);
				token.kToken = TokenType.CHARACTER;
				break;
			default:
				break;
		}
		return token;
	}

	/**
	 * 强制字符转换（值）
	 *
	 * @param token 操作数
	 * @return 转换结果
	 */
	private static char getCharValue(Token token) {
		switch (token.kToken) {
			case STRING:
				String str = (String) token.object;
				return str.isEmpty() ? '\0' : str.charAt(0);
			case DECIMAL:
				double decimal = (double) token.object;
				return (char) (double) decimal;
			case INTEGER:
				long integer = (long) token.object;
				return (char) (long) integer;
			default:
				break;
		}
		return '\0';
	}
}
