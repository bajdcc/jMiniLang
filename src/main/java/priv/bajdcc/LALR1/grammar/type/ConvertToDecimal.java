package priv.bajdcc.LALR1.grammar.type;

import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

import java.math.BigDecimal;
import java.math.BigInteger;

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
	private static BigDecimal getDecimalValue(Token token) {
		switch (token.kToken) {
			case BOOL:
				boolean bool = (boolean) token.object;
				return bool ? BigDecimal.ONE : BigDecimal.ZERO;
			case STRING:
				String str = (String) token.object;
				try {
					return new BigDecimal(str);
				} catch (NumberFormatException e) {
					token.kToken = TokenType.ERROR;
				}
				break;
			case INTEGER:
				BigInteger integer = (BigInteger) token.object;
				return new BigDecimal(integer);
			case DECIMAL:
				return (BigDecimal) token.object;
			default:
				break;
		}
		return null;
	}
}
