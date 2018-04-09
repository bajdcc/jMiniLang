package priv.bajdcc.OP.syntax.lexer.tokenizer;

import priv.bajdcc.util.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.regex.IRegexStringIterator;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

/**
 * 非终结符解析
 *
 * @author bajdcc
 */
public class NonTerminalTokenizer extends TokenAlgorithm {

	public NonTerminalTokenizer() throws RegexException {
		super(getRegexString(), null);
	}

	public static String getRegexString() {
		return "(\\a|_)\\w*";
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
		token.kToken = TokenType.ID;
		token.object = string;
		return token;
	}
}
