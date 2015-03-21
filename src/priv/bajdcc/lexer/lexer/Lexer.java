package priv.bajdcc.lexer.lexer;

import priv.bajdcc.lexer.algorithm.ITokenAlgorithm;
import priv.bajdcc.lexer.algorithm.TokenAlgorithmCollection;
import priv.bajdcc.lexer.algorithm.impl.CharacterTokenizer;
import priv.bajdcc.lexer.algorithm.impl.CommentTokenizer;
import priv.bajdcc.lexer.algorithm.impl.IdentifierTokenizer;
import priv.bajdcc.lexer.algorithm.impl.KeywordTokenizer;
import priv.bajdcc.lexer.algorithm.impl.NumberTokenizer;
import priv.bajdcc.lexer.algorithm.impl.OperatorTokenizer;
import priv.bajdcc.lexer.algorithm.impl.StringTokenizer;
import priv.bajdcc.lexer.algorithm.impl.WhitespaceTokenizer;
import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.regex.IRegexStringFilterHost;
import priv.bajdcc.lexer.regex.RegexStringIterator;
import priv.bajdcc.lexer.token.Token;

/**
 * 词法分析器
 * 
 * @author bajdcc
 */
public class Lexer extends RegexStringIterator implements
		IRegexStringFilterHost {

	/**
	 * 算法集合（正则表达式匹配）
	 */
	private TokenAlgorithmCollection m_algCollections = new TokenAlgorithmCollection(
			this, this);

	/**
	 * 字符转换算法
	 */
	private ITokenAlgorithm m_TokenAlg = null;

	public Lexer(String context) throws RegexException {
		super(context);
		initialize();
	}

	/**
	 * 获取一个单词
	 * 
	 * @return 单词
	 */
	public Token scan() {
		return m_algCollections.scan();
	}

	@Override
	public void setFilter(ITokenAlgorithm alg) {
		m_TokenAlg = alg;
	}

	@Override
	protected void transform() {
		super.transform();
		if (m_TokenAlg != null) {
			m_Data.m_kMeta = m_TokenAlg.getMetaHash().get(m_Data.m_chCurrent);
		}
	}

	/**
	 * 初始化（添加组件）
	 * 
	 * @throws RegexException
	 */
	private void initialize() throws RegexException {
		m_algCollections.attach(new WhitespaceTokenizer());// 空白字符解析组件
		m_algCollections.attach(new CommentTokenizer());// 注释解析组件
		m_algCollections.attach(new NumberTokenizer());// 数字解析组件
		m_algCollections.attach(new OperatorTokenizer());// 操作符解析组件
		m_algCollections.attach(new KeywordTokenizer());// 关键字解析组件
		m_algCollections.attach(new StringTokenizer());// 字符串解析组件
		m_algCollections.attach(new CharacterTokenizer());// 字符解析组件
		m_algCollections.attach(new IdentifierTokenizer());// 标识符解析组件
	}
}
