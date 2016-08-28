package priv.bajdcc.LALR1.grammar.type;

import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 【类型转换】转换为字符串类型
 *
 * @author bajdcc
 */
public class ConvertToString implements ITokenConventer {

	@Override
	public Token convert(Token token) {
		token.kToken = TokenType.STRING;
		token.object = String.valueOf(token.object);
		return token;
	}
}
