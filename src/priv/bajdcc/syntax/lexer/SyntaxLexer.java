package priv.bajdcc.syntax.lexer;

import java.util.HashSet;

import priv.bajdcc.lexer.algorithm.ITokenAlgorithm;
import priv.bajdcc.lexer.algorithm.TokenAlgorithmCollection;
import priv.bajdcc.lexer.algorithm.impl.WhitespaceTokenizer;
import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.regex.IRegexStringFilterHost;
import priv.bajdcc.lexer.regex.RegexStringIterator;
import priv.bajdcc.lexer.token.MetaType;
import priv.bajdcc.syntax.lexer.tokenizer.CommentTokenizer;
import priv.bajdcc.syntax.lexer.tokenizer.NonTerminalTokenizer;
import priv.bajdcc.syntax.lexer.tokenizer.NumberTokenizer;
import priv.bajdcc.syntax.lexer.tokenizer.OperatorTokenizer;
import priv.bajdcc.syntax.lexer.tokenizer.TerminalTokenizer;
import priv.bajdcc.syntax.token.Token;
import priv.bajdcc.syntax.token.TokenType;

/**
 * 解析文法的词法分析器
 * 
 * @author bajdcc
 */
public class SyntaxLexer extends RegexStringIterator implements
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

	public SyntaxLexer() throws RegexException {
		initialize();
	}

	/**
	 * 设置要分析的内容
	 * 
	 * @param context
	 *            文法推导式
	 */
	public void setContext(String context) {
		/* 初始化 */
		m_strContext = context;
		m_Position.m_iColumn = 0;
		m_Position.m_iLine = 0;
		m_Data.m_chCurrent = 0;
		m_Data.m_iIndex = 0;
		m_Data.m_kMeta = MetaType.END;
		m_stkIndex.clear();
		m_stkPosition.clear();
	}

	/**
	 * 获取一个单词
	 * 
	 * @return 单词
	 */
	public Token scan() {
		Token token = Token.transfer(m_algCollections.scan());
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
		m_algCollections.attach(new TerminalTokenizer());// 终结符解析组件
		m_algCollections.attach(new NonTerminalTokenizer());// 非终结符解析组件
		m_algCollections.attach(new NumberTokenizer());// 存储序号解析组件
		m_algCollections.attach(new OperatorTokenizer());// 操作符解析组件
	}
}
