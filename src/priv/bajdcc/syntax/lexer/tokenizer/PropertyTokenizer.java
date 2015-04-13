package priv.bajdcc.syntax.lexer.tokenizer;

import priv.bajdcc.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.lexer.algorithm.filter.StringPairFilter;
import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.token.MetaType;
import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.lexer.token.TokenType;

/**
 * 终结符解析
 * 
 * @author bajdcc
 *
 */
public class PropertyTokenizer extends TokenAlgorithm {

	public PropertyTokenizer() throws RegexException {
		super(getRegexString(), new StringPairFilter(MetaType.LBRACE, MetaType.RBRACE));
	}

	public static String getRegexString() {
		return "{.*}";
	}

	/* （非 Javadoc）
	 * @see priv.bajdcc.lexer.algorithm.ITokenAlgorithm#getToken(java.lang.String, priv.bajdcc.lexer.token.Token)
	 */
	@Override
	public Token getToken(String string, Token token) {
		token.kToken = TokenType.MACRO;
		token.object = string;
		return token;
	}
}
