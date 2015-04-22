package priv.bajdcc.util.lexer.algorithm.filter;

import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.error.RegexException.RegexError;
import priv.bajdcc.util.lexer.regex.IRegexStringFilter;
import priv.bajdcc.util.lexer.regex.IRegexStringFilterMeta;
import priv.bajdcc.util.lexer.regex.IRegexStringIterator;
import priv.bajdcc.util.lexer.regex.RegexStringIteratorData;
import priv.bajdcc.util.lexer.regex.RegexStringUtility;
import priv.bajdcc.util.lexer.token.MetaType;

/**
 * 字符串类型过滤（首尾字符不同）
 * 
 * @author bajdcc
 *
 */
public class StringPairFilter implements IRegexStringFilter,
		IRegexStringFilterMeta {

	/**
	 * 字符串首的终结符
	 */
	private MetaType kMetaBegin = MetaType.NULL;

	/**
	 * 字符串尾的终结符
	 */
	private MetaType kMetaEnd = MetaType.NULL;

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
				data.kMeta = MetaType.END;
				data.chCurrent = MetaType.END.getChar();
			} else {
				data.kMeta = iterator.meta();
				data.chCurrent = iterator.current();
				iterator.next();
				if (data.kMeta == kMetaBegin || data.kMeta == kMetaEnd) {// 过滤终结符
					data.kMeta = MetaType.NULL;
				} else if (data.kMeta == MetaType.ESCAPE) {// 处理转义
					data.chCurrent = iterator.current();
					iterator.next();
					data.kMeta = MetaType.MUST_SAVE;
					data.chCurrent = utility.fromEscape(data.chCurrent,
							RegexError.ESCAPE);
				}
			}
		} catch (RegexException e) {
			System.err.println(e.getPosition() + " : " + e.getMessage());
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
		return new MetaType[] { kMetaBegin, kMetaEnd, MetaType.ESCAPE };
	}
}
