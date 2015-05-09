package priv.bajdcc.LALR1.grammar.type;

import java.math.BigDecimal;
import java.math.BigInteger;

import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

/**
 * 【类型转换】转换为布尔类型
 *
 * @author bajdcc
 */
public class ConvertToBoolean implements ITokenConventer {

	@Override
	public Token convert(Token token) {
		switch (token.kToken) {
		case CHARACTER:
		case STRING:
		case DECIMAL:
		case INTEGER:
			token.object = getBooleanValue(token);
			token.kToken = TokenType.BOOL;
			break;
		default:
			break;
		}
		return token;
	}

	/**
	 * 强制布尔转换（值）
	 * 
	 * @param token
	 *            操作数
	 * @return 转换结果
	 */
	private static boolean getBooleanValue(Token token) {
		switch (token.kToken) {
		case BOOL:
			boolean bool = (boolean) token.object;
			return bool;
		case CHARACTER:
			char ch = (char) token.object;
			return ch != 0;
		case STRING:
			return true;
		case DECIMAL:
			BigDecimal decimal = (BigDecimal) token.object;
			return decimal.signum() != 0;
		case INTEGER:
			BigInteger integer = (BigInteger) token.object;
			return integer.signum() != 0;
		default:
			break;
		}
		return false;
	}
}
