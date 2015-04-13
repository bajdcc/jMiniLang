package priv.bajdcc.syntax.lexer.tokenizer;

import priv.bajdcc.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.lexer.algorithm.filter.StringPairFilter;
import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.token.MetaType;
import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.lexer.token.TokenType;

/**
 * 数字解析
 * 
 * @author bajdcc
 *
 */
public class NumberTokenizer extends TokenAlgorithm {

	public NumberTokenizer() throws RegexException {
		super(getRegexString(), new StringPairFilter(MetaType.LSQUARE, MetaType.RSQUARE));
	}

	public static String getRegexString() {
		return "-1|\\[\\d+\\]";
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
	public Token getToken(String string, Token token) {
		token.kToken = TokenType.INTEGER;
		token.object = Integer.parseInt(string);
		return token;
	}
}
