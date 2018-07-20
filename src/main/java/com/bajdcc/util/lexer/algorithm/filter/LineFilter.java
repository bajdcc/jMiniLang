package com.bajdcc.util.lexer.algorithm.filter;

import com.bajdcc.util.lexer.regex.IRegexStringFilter;
import com.bajdcc.util.lexer.regex.IRegexStringFilterMeta;
import com.bajdcc.util.lexer.regex.IRegexStringIterator;
import com.bajdcc.util.lexer.regex.RegexStringIteratorData;
import com.bajdcc.util.lexer.token.MetaType;

/**
 * 转义换行过滤
 *
 * @author bajdcc
 */
public class LineFilter implements IRegexStringFilter, IRegexStringFilterMeta {

	@Override
	public RegexStringIteratorData filter(IRegexStringIterator iterator) {
		RegexStringIteratorData data = new RegexStringIteratorData();
		if (!iterator.available()) {
			data.setMeta(MetaType.END);
			data.setCurrent(MetaType.END.getChar());
		} else {
			data.setMeta(iterator.meta());
			data.setCurrent(iterator.current());
			iterator.next();
			if (data.getMeta() == MetaType.ESCAPE) {// 过滤转义换行
				iterator.next();
				iterator.snapshot();
				data.setMeta(iterator.meta());
				if (data.getMeta() == MetaType.NEW_LINE
						|| data.getMeta() == MetaType.CARRIAGE_RETURN) {// 确认换行
					iterator.next();
					iterator.cover();
					data.setMeta(iterator.meta());
					if (data.getMeta() == MetaType.NEW_LINE
							|| data.getMeta() == MetaType.CARRIAGE_RETURN) {// 确认换行
						iterator.discard();
						iterator.next();
					} else {
						iterator.restore();
					}
					data.setMeta(MetaType.MUST_SAVE);
					data.setCurrent(iterator.current());
					iterator.next();
				} else {
					iterator.restore();
				}
			}
		}
		return data;
	}

	@Override
	public IRegexStringFilterMeta getFilterMeta() {
		return this;
	}

	@Override
	public MetaType[] getMetaTypes() {
		return new MetaType[]{MetaType.ESCAPE, MetaType.NEW_LINE,
				MetaType.CARRIAGE_RETURN};
	}
}
