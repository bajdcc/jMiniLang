package priv.bajdcc.lexer.algorithm;

import java.util.ArrayList;

import priv.bajdcc.lexer.error.IErrorHandler;
import priv.bajdcc.lexer.regex.IRegexStringFilterHost;
import priv.bajdcc.lexer.regex.IRegexStringIterator;
import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.lexer.token.TokenType;

/**
 * 用于抽取单词的算法集合（包含数字、字符串等）
 * 
 * @author bajdcc
 */
public class TokenAlgorithmCollection {
	/**
	 * 算法集合
	 */
	private ArrayList<ITokenAlgorithm> m_arrAlgorithms = new ArrayList<ITokenAlgorithm>();

	/**
	 * 字符串迭代器
	 */
	private IRegexStringIterator m_Iterator = null;

	/**
	 * 字符转换主体
	 */
	private IRegexStringFilterHost m_FilterHost = null;
	
	/**
	 * 错误处理
	 */
	private IErrorHandler m_Handler = null;

	public TokenAlgorithmCollection(IRegexStringIterator iterator,
			IRegexStringFilterHost host) {
		m_Iterator = iterator;
		m_FilterHost = host;
		m_Handler = new TokenErrorAdvanceHandler(iterator);
	}

	public void attach(ITokenAlgorithm alg) {
		m_arrAlgorithms.add(alg);
	}

	public void detach(ITokenAlgorithm alg) {
		m_arrAlgorithms.remove(alg);
	}

	public Token scan() {
		Token token = new Token();
		token.m_kToken = TokenType.ERROR;
		if (!m_Iterator.available()) {
			token.m_kToken = TokenType.EOF;
		} else {
			for (ITokenAlgorithm alg : m_arrAlgorithms) {
				m_FilterHost.setFilter(alg);
				m_Iterator.translate();
				if (alg.accept(m_Iterator, token))
					return token;
			}
			m_Handler.handleError();
		}
		return token;
	}
}
