package priv.bajdcc.LALR1.syntax.lexer.tokenizer;

import priv.bajdcc.util.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.util.lexer.algorithm.filter.StringPairFilter;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.regex.IRegexStringIterator;
import priv.bajdcc.util.lexer.token.MetaType;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

/**
 * 终结符解析
 *
 * @author bajdcc
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
	public Token getToken(String string, Token token, IRegexStringIterator iterator) {
		token.kToken = TokenType.MACRO;
		token.object = string;
		return token;
	}
}
