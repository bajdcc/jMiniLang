package priv.bajdcc.lexer.algorithm;

import java.util.HashMap;

import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.regex.IRegexStringFilter;
import priv.bajdcc.lexer.regex.IRegexStringIterator;
import priv.bajdcc.lexer.regex.IRegexStringAttribute;
import priv.bajdcc.lexer.regex.Regex;
import priv.bajdcc.lexer.token.MetaType;
import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.lexer.token.TokenType;

/**
 * 单词识别算法
 * 
 * @author bajdcc
 *
 */
/**
 * @author bajdcc
 *
 */
public abstract class TokenAlgorithm implements ITokenAlgorithm,
		IRegexStringAttribute {

	/**
	 * 用来匹配的正则表达式
	 */
	protected Regex m_Regex = null;

	/**
	 * 匹配结果
	 */
	protected String m_strMatch = "";

	/**
	 * 字符过滤接口
	 */
	protected IRegexStringFilter m_Filter = null;

	/**
	 * 字符类型哈段表
	 */
	protected HashMap<Character, MetaType> m_MetaMap = new HashMap<Character, MetaType>();

	public TokenAlgorithm(String regex, IRegexStringFilter filter)
			throws RegexException {
		m_Regex = new Regex(regex);
		if (filter != null) {
			m_Filter = filter;
			m_Regex.setFilter(m_Filter);
			MetaType[] metaTypes = m_Filter.getFilterMeta().getMetaTypes();
			for (int i = 0; i < metaTypes.length; i++) {
				m_MetaMap.put(metaTypes[i].getChar(), metaTypes[i]);
			}
		}
	}

	@Override
	public boolean accept(IRegexStringIterator iterator, Token token) {
		if (!iterator.available()) {
			token.m_kToken = TokenType.EOF;
			return true;
		}
		token.m_Position = iterator.position();
		if (m_Regex.match(iterator, this)) {// 匹配成功
			token = getToken(m_strMatch, token);// 自动转换单词
			return true;
		}
		return false;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see priv.bajdcc.lexer.regex.IRegexStringAttribute#getGreedMode()
	 */
	@Override
	public boolean getGreedMode() {
		return false;// 默认为非贪婪模式
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see priv.bajdcc.lexer.algorithm.ITokenAlgorithm#getRegexStringFilter()
	 */
	@Override
	public IRegexStringFilter getRegexStringFilter() {
		return m_Filter;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see priv.bajdcc.lexer.algorithm.ITokenAlgorithm#getMetaHash()
	 */
	@Override
	public HashMap<Character, MetaType> getMetaHash() {
		return m_MetaMap;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * priv.bajdcc.lexer.regex.IRegexStringResult#setResult(java.lang.String)
	 */
	@Override
	public void setResult(String result) {
		m_strMatch = result;
	}
}
