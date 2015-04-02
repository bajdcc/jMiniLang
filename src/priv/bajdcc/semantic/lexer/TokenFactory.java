package priv.bajdcc.semantic.lexer;

import priv.bajdcc.lexer.Lexer;
import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.regex.IRegexStringIterator;

/**
 * 词法分析器
 * 
 * @author bajdcc
 */
public class TokenFactory extends Lexer {

	public TokenFactory(String context) throws RegexException {
		super(context);
	}
	
	@Override
	public IRegexStringIterator copy() {
		TokenFactory o = null;
		try {
			o = (TokenFactory) super.clone();			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}
}
