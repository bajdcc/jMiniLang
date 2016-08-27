package priv.bajdcc.LALR1.grammar.type;

import java.math.BigDecimal;
import java.math.BigInteger;

import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

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
	 * @param token
	 *            操作数
	 * @return 转换结果
	 */
	private static char getCharValue(Token token) {
		switch (token.kToken) {
		case STRING:
			String str = (String) token.object;
			return str.isEmpty() ? '\0' : str.charAt(0);
		case DECIMAL:
			BigDecimal decimal = (BigDecimal) token.object;
			return (char) decimal.intValue();
		case INTEGER:
			BigInteger integer = (BigInteger) token.object;
			return (char) integer.intValue();
		default:
			break;
		}
		return '\0';
	}
}
