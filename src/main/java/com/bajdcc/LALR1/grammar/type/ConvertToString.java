package com.bajdcc.LALR1.grammar.type;

import com.bajdcc.util.lexer.token.Token;
import com.bajdcc.util.lexer.token.TokenType;

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
