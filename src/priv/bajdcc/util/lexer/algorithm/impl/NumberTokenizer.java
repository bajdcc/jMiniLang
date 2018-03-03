package priv.bajdcc.util.lexer.algorithm.impl;

import java.math.BigDecimal;
import priv.bajdcc.util.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.regex.IRegexStringIterator;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

/**
 * 数字解析
 * 
 * @author bajdcc
 *
 */
public class NumberTokenizer extends TokenAlgorithm {

	public NumberTokenizer() throws RegexException {
		super(getRegexString(), null);
	}

	public static String getRegexString() {
		return "[+-]?(\\d*\\.?\\d+|\\d+\\.?\\d*)([eE][+-]?\\d+)?";
	}

	@Override
	public boolean getGreedMode() {
		return true;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * priv.bajdcc.lexer.algorithm.ITokenAlgorithm#getToken(java.lang.String,
	 * priv.bajdcc.lexer.token.Token)
	 */
	@Override
	public Token getToken(String string, Token token, IRegexStringIterator iterator) {
		try {
			BigDecimal decimal = new BigDecimal(string);
			token.object = decimal;
			if (string.indexOf('.') == -1) {
				token.object = decimal.toBigIntegerExact();
				token.kToken = TokenType.INTEGER;
			} else {
				token.kToken = TokenType.DECIMAL;
			}
		} catch (ArithmeticException e) {
			token.kToken = TokenType.DECIMAL;
		}
		return token;
	}
}
