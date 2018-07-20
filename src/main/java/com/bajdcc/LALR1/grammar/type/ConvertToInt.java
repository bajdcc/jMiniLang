package com.bajdcc.LALR1.grammar.type;

import com.bajdcc.util.lexer.token.Token;
import com.bajdcc.util.lexer.token.TokenType;

/**
 * 【类型转换】转换为整数类型
 *
 * @author bajdcc
 */
public class ConvertToInt implements ITokenConventer {

	@Override
	public Token convert(Token token) {
		TokenType type = token.getType();
		switch (token.getType()) {
			case BOOL:
			case CHARACTER:
			case STRING:
			case DECIMAL:
			case INTEGER:
				token.setObj(getIntValue(token));
				if (token.getType() == TokenType.ERROR) {
					token.setType(type);
				} else {
					token.setType(TokenType.INTEGER);
				}
				break;
			default:
				break;
		}
		return token;
	}

	/**
	 * 强制整数转换（值）
	 *
	 * @param token 操作数
	 * @return 转换结果
	 */
	private static long getIntValue(Token token) {
		switch (token.getType()) {
			case BOOL:
				boolean bool = (boolean) token.getObj();
				return bool ? 1L : 0L;
			case CHARACTER:
				char ch = (char) token.getObj();
				return (long) ch;
			case STRING:
				String str = (String) token.getObj();
				try {
					return Long.parseLong(str);
				} catch (NumberFormatException e) {
					token.setType(TokenType.ERROR);
				}
				break;
			case INTEGER:
				return (long) token.getObj();
			case DECIMAL:
				double decimal = (double) token.getObj();
				return (long) decimal;
			default:
				break;
		}
		return 0L;
	}
}
