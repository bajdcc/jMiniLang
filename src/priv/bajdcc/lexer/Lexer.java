package priv.bajdcc.lexer;

import java.util.HashSet;

import priv.bajdcc.lexer.algorithm.ITokenAlgorithm;
import priv.bajdcc.lexer.algorithm.TokenAlgorithmCollection;
import priv.bajdcc.lexer.algorithm.impl.CharacterTokenizer;
import priv.bajdcc.lexer.algorithm.impl.CommentTokenizer;
import priv.bajdcc.lexer.algorithm.impl.IdentifierTokenizer;
import priv.bajdcc.lexer.algorithm.impl.KeywordTokenizer;
import priv.bajdcc.lexer.algorithm.impl.MacroTokenizer;
import priv.bajdcc.lexer.algorithm.impl.NumberTokenizer;
import priv.bajdcc.lexer.algorithm.impl.OperatorTokenizer;
import priv.bajdcc.lexer.algorithm.impl.StringTokenizer;
import priv.bajdcc.lexer.algorithm.impl.WhitespaceTokenizer;
import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.regex.IRegexStringFilterHost;
import priv.bajdcc.lexer.regex.RegexStringIterator;
import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.lexer.token.TokenType;

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

	/**
	 * 丢弃的类型集合
	 */
	private HashSet<TokenType> m_setDiscardToken = new HashSet<TokenType>();

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
		Token token = m_algCollections.scan();
		if (m_setDiscardToken.contains(token.m_kToken)) {// 需要丢弃
			return null;
		}
		return token;
	}

	/**
	 * 设置丢弃符号
	 * 
	 * @param type
	 *            要丢弃的符号类型（不建议丢弃EOF，因为需要用来判断结束）
	 */
	public void discard(TokenType type) {
		m_setDiscardToken.add(type);
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
		//
		// ### 算法容器中装载解析组件是有一定顺序的 ###
		//
		// 组件调用原理：
		// 每个组件有自己的正则表达式匹配字符串
		// （可选）有自己的过滤方法，如字符串中的转义过滤
		//
		// 解析时，分别按序调用解析组件，若组件解析失败，则调用下一组件
		// 若某一组件解析成功，即返回匹配结果
		// 若全部解析失败，则调用出错处理（默认为前进一字符）
		//
		m_algCollections.attach(new WhitespaceTokenizer());// 空白字符解析组件
		m_algCollections.attach(new CommentTokenizer());// 注释解析组件
		m_algCollections.attach(new MacroTokenizer());// 宏解析组件
		m_algCollections.attach(new StringTokenizer());// 字符串解析组件
		m_algCollections.attach(new CharacterTokenizer());// 字符解析组件
		m_algCollections.attach(new IdentifierTokenizer());// 标识符解析组件
		m_algCollections.attach(new KeywordTokenizer());// 关键字解析组件
		m_algCollections.attach(new NumberTokenizer());// 数字解析组件
		m_algCollections.attach(new OperatorTokenizer());// 操作符解析组件
	}
}
