package com.bajdcc.util.lexer.regex;

import com.bajdcc.util.lexer.token.MetaType;

/**
 * 分析时使用的数据
 */
public class RegexStringIteratorData implements Cloneable {
	/**
	 * 当前处理的位置
	 */
	public int iIndex = 0;

	/**
	 * 字符
	 */
	public char chCurrent = 0;

	/**
	 * 字符类型
	 */
	public MetaType kMeta = MetaType.END;

	public RegexStringIteratorData() {

	}

	public RegexStringIteratorData(int index, char current, MetaType meta) {
		iIndex = index;
		chCurrent = current;
		kMeta = meta;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}