package com.bajdcc.util.lexer.algorithm.filter;

import com.bajdcc.util.lexer.error.RegexException;
import com.bajdcc.util.lexer.error.RegexException.RegexError;
import com.bajdcc.util.lexer.regex.*;
import com.bajdcc.util.lexer.token.MetaType;

/**
 * 字符串类型过滤（首尾字符不同）
 *
 * @author bajdcc
 */
public class StringPairFilter implements IRegexStringFilter,
		IRegexStringFilterMeta {

	/**
	 * 字符串首的终结符
	 */
	private MetaType kMetaBegin;

	/**
	 * 字符串尾的终结符
	 */
	private MetaType kMetaEnd;

	public StringPairFilter(MetaType begin, MetaType end) {
		kMetaBegin = begin;
		kMetaEnd = end;
	}

	@Override
	public RegexStringIteratorData filter(IRegexStringIterator iterator) {
		RegexStringUtility utility = iterator.utility();// 获取解析组件
		RegexStringIteratorData data = new RegexStringIteratorData();
		try {
			if (!iterator.available()) {
				data.setMeta(MetaType.END);
				data.setCurrent(MetaType.END.getChar());
			} else {
				data.setMeta(iterator.meta());
				data.setCurrent(iterator.current());
				iterator.next();
				if (data.getMeta() == kMetaBegin || data.getMeta() == kMetaEnd) {// 过滤终结符
					data.setMeta(MetaType.NULL);
				} else if (data.getMeta() == MetaType.ESCAPE) {// 处理转义
					data.setCurrent(iterator.current());
					iterator.next();
					data.setMeta(MetaType.MUST_SAVE);
					data.setCurrent(utility.fromEscape(data.getCurrent(),
							RegexError.ESCAPE));
				}
			}
		} catch (RegexException e) {
			System.err.println(e.getPosition() + " : " + e.getMessage());
			data.setMeta(MetaType.ERROR);
			data.setCurrent(MetaType.ERROR.getChar());
		}
		return data;
	}

	@Override
	public IRegexStringFilterMeta getFilterMeta() {
		return this;
	}

	@Override
	public MetaType[] getMetaTypes() {
		return new MetaType[]{kMetaBegin, kMetaEnd, MetaType.ESCAPE};
	}
}
