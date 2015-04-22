package priv.bajdcc.LALR1.syntax.lexer.tokenizer;

import priv.bajdcc.util.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.util.lexer.algorithm.filter.StringFilter;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.token.MetaType;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

/**
 * 动作解析
 * 
 * @author bajdcc
 *
 */
public class ActionTokenizer extends TokenAlgorithm {

	public ActionTokenizer() throws RegexException {
		super(getRegexString(), new StringFilter(MetaType.SHARP));
	}

	public static String getRegexString() {
		return "#.*#";
	}

	/* （非 Javadoc）
	 * @see priv.bajdcc.lexer.algorithm.ITokenAlgorithm#getToken(java.lang.String, priv.bajdcc.lexer.token.Token)
	 */
	@Override
	public Token getToken(String string, Token token) {
		token.kToken = TokenType.RESERVE;
		token.object = string;
		return token;
	}
}
