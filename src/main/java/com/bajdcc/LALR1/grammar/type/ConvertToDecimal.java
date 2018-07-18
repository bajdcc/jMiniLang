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
		TokenType type = token.kToken;
		switch (token.kToken) {
			case BOOL:
			case STRING:
			case DECIMAL:
			case INTEGER:
				token.object = getDecimalValue(token);
				if (token.kToken == TokenType.ERROR) {
					token.kToken = type;
				} else {
					token.kToken = TokenType.DECIMAL;
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
		switch (token.kToken) {
			case BOOL:
				boolean bool = (boolean) token.object;
				return bool ? 1D : 0D;
			case STRING:
				String str = (String) token.object;
				try {
					return Double.parseDouble(str);
				} catch (NumberFormatException e) {
					token.kToken = TokenType.ERROR;
				}
				break;
			case INTEGER:
				long integer = (long) token.object;
				return (double) integer;
			case DECIMAL:
				return (double) token.object;
			default:
				break;
		}
		return 0D;
	}
}
