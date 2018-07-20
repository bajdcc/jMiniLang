package com.bajdcc.util.lexer.algorithm.filter;

import com.bajdcc.util.lexer.error.RegexException;
import com.bajdcc.util.lexer.error.RegexException.RegexError;
import com.bajdcc.util.lexer.regex.*;
import com.bajdcc.util.lexer.token.MetaType;

/**
 * 字符类型过滤
 *
 * @author bajdcc
 */
public class CharacterFilter implements IRegexStringFilter,
		IRegexStringFilterMeta {

	@Override
	public RegexStringIteratorData filter(IRegexStringIterator iterator) {
		RegexStringUtility utility = iterator.utility();// 获取解析组件
		RegexStringIteratorData data = new RegexStringIteratorData();
		try {
			if (!iterator.available()) {
				data.kMeta = MetaType.END;
				data.chCurrent = MetaType.END.getChar();
			} else {
				data.kMeta = iterator.meta();
				data.chCurrent = iterator.current();
				iterator.next();
				if (data.kMeta == MetaType.SINGLE_QUOTE) {// 过滤单引号
					data.kMeta = MetaType.NULL;
				} else if (data.kMeta == MetaType.ESCAPE) {// 处理转义
					data.chCurrent = iterator.current();
					iterator.next();
					data.kMeta = MetaType.MUST_SAVE;
					if (data.chCurrent == '0')
						data.zero = true;
					data.chCurrent = utility.fromEscape(data.chCurrent,
							RegexError.ESCAPE);
				}
			}
		} catch (RegexException e) {
			System.err.println(e.getPosition() + " : "
					+ e.getMessage());
			data.kMeta = MetaType.ERROR;
			data.chCurrent = MetaType.ERROR.getChar();
		}
		return data;
	}

	@Override
	public IRegexStringFilterMeta getFilterMeta() {
		return this;
	}

	@Override
	public MetaType[] getMetaTypes() {
		return new MetaType[]{MetaType.SINGLE_QUOTE, MetaType.ESCAPE};
	}
}
