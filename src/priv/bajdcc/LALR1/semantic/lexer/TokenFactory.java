package priv.bajdcc.LALR1.semantic.lexer;

import java.util.ArrayList;

import priv.bajdcc.util.lexer.Lexer;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.regex.IRegexStringIterator;
import priv.bajdcc.util.lexer.token.Token;

/**
 * 词法分析器
 * 
 * @author bajdcc
 */
public class TokenFactory extends Lexer {

	/**
	 * 保存当前分析的单词流
	 */
	private ArrayList<Token> arrTokens = new ArrayList<Token>();

	public TokenFactory(String context) throws RegexException {
		super(context);
	}

	@Override
	public IRegexStringIterator copy() {
		TokenFactory o = null;
		try {
			o = (TokenFactory) super.clone();
			o.arrTokens = new ArrayList<Token>(arrTokens);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	@Override
	public ArrayList<Token> tokenList() {
		return arrTokens;
	}

	@Override
	public void saveToken() {
		arrTokens.add(token);
	}
}
