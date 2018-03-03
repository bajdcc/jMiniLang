package priv.bajdcc.LL1.syntax.lexer.tokenizer;

import java.util.HashMap;

import priv.bajdcc.LL1.syntax.token.OperatorType;
import priv.bajdcc.util.lexer.algorithm.TokenAlgorithm;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.regex.IRegexStringIterator;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

/**
 * 操作符解析
 * 
 * @author bajdcc
 *
 */
public class OperatorTokenizer extends TokenAlgorithm {

	/**
	 * 关键字哈希表
	 */
	private HashMap<String, OperatorType> hashOperator = new HashMap<>();

	public OperatorTokenizer() throws RegexException {
		super(getRegexString(), null);
		initializeHashMap();
	}

	public static String getRegexString() {
		return "->|\\||<|>";
	}

	@Override
	public boolean getGreedMode() {
		return true;
	}

	/**
	 * 初始化关键字哈希表
	 */
	private void initializeHashMap() {
		for (OperatorType operator : OperatorType.values()) {// 关键字
			hashOperator.put(operator.getName(), operator);
		}
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
		token.kToken = TokenType.OPERATOR;
		token.object = hashOperator.get(string);
		return token;
	}
}
