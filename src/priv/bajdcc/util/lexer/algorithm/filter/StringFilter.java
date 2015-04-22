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
 * 字符串类型过滤
 * 
 * @author bajdcc
 *
 */
public class StringFilter implements IRegexStringFilter, IRegexStringFilterMeta {

	/**
	 * 字符串首尾的终结符
	 */
	private MetaType kMeta = MetaType.NULL;
	
	public StringFilter(MetaType meta) {
		kMeta = meta;
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
				if (data.kMeta == kMeta) {// 过滤终结符
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
		return new MetaType[] { kMeta, MetaType.ESCAPE };
	}
}
