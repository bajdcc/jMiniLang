package priv.bajdcc.lexer.regex;

import priv.bajdcc.lexer.token.MetaType;

/**
 * 分析时使用的数据
 */
public class RegexStringIteratorData {
	/**
	 * 当前处理的位置
	 */
	public int m_iIndex = 0;

	/**
	 * 字符
	 */
	public char m_chCurrent = 0;

	/**
	 * 字符类型
	 */
	public MetaType m_kMeta = MetaType.END;

	public RegexStringIteratorData() {

	}

	public RegexStringIteratorData(int index, char current, MetaType meta) {
		m_iIndex = index;
		m_chCurrent = current;
		m_kMeta = meta;
	}
}