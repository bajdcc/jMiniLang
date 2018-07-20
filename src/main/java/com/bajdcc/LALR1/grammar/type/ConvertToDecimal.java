package com.bajdcc.LALR1.grammar.type;

import com.bajdcc.util.lexer.token.Token;
import com.bajdcc.util.lexer.token.TokenType;

/**
 * 【类型转换】转换为定点数类型
 *
 * @author bajdcc
 */
public class ConvertToDecimal implements ITokenConventer {

	@Override
	public Token convert(Token token) {
		TokenType type = token.getType();
		switch (token.getType()) {
			case BOOL:
			case STRING:
			case DECIMAL:
			case INTEGER:
				token.setObj(getDecimalValue(token));
				if (token.getType() == TokenType.ERROR) {
					token.setType(type);
				} else {
					token.setType(TokenType.DECIMAL);
				}
				break;
			default:
				break;
		}
		return token;
	}

	/**
	 * 强制定点数转换（值）
	 *
	 * @param token 操作数
	 * @return 转换结果
	 */
	private static double getDecimalValue(Token token) {
		switch (token.getType()) {
			case BOOL:
				boolean bool = (boolean) token.getObj();
				return bool ? 1D : 0D;
			case STRING:
				String str = (String) token.getObj();
				try {
					return Double.parseDouble(str);
				} catch (NumberFormatException e) {
					token.setType(TokenType.ERROR);
				}
				break;
			case INTEGER:
				long integer = (long) token.getObj();
				return (double) integer;
			case DECIMAL:
				return (double) token.getObj();
			default:
				break;
		}
		return 0D;
	}
}
