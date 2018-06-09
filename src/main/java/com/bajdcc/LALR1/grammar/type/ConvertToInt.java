package com.bajdcc.LALR1.grammar.type;

import com.bajdcc.util.lexer.token.Token;
import com.bajdcc.util.lexer.token.TokenType;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 【类型转换】转换为整数类型
 *
 * @author bajdcc
 */
public class ConvertToInt implements ITokenConventer {

	@Override
	public Token convert(Token token) {
		TokenType type = token.kToken;
		switch (token.kToken) {
			case BOOL:
			case CHARACTER:
			case STRING:
			case DECIMAL:
			case INTEGER:
				token.object = getIntValue(token);
				if (token.kToken == TokenType.ERROR) {
					token.kToken = type;
				} else {
					token.kToken = TokenType.INTEGER;
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
	private static BigInteger getIntValue(Token token) {
		switch (token.kToken) {
			case BOOL:
				boolean bool = (boolean) token.object;
				return bool ? BigInteger.ONE : BigInteger.ZERO;
			case CHARACTER:
				char ch = (char) token.object;
				return BigInteger.valueOf(ch);
			case STRING:
				String str = (String) token.object;
				try {
					return new BigInteger(str);
				} catch (NumberFormatException e) {
					token.kToken = TokenType.ERROR;
				}
				break;
			case INTEGER:
				return (BigInteger) token.object;
			case DECIMAL:
				BigDecimal decimal = (BigDecimal) token.object;
				return decimal.toBigInteger();
			default:
				break;
		}
		return null;
	}
}
