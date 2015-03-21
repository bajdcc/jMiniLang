package priv.bajdcc.lexer.algorithm.filter;

import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.error.RegexException.RegexError;
import priv.bajdcc.lexer.regex.IRegexStringFilter;
import priv.bajdcc.lexer.regex.IRegexStringFilterMeta;
import priv.bajdcc.lexer.regex.IRegexStringIterator;
import priv.bajdcc.lexer.regex.RegexStringIteratorData;
import priv.bajdcc.lexer.regex.RegexStringUtility;
import priv.bajdcc.lexer.token.MetaType;

/**
 * 字符类型过滤
 * 
 * @author bajdcc
 *
 */
public class CharacterFilter implements IRegexStringFilter,
		IRegexStringFilterMeta {

	@Override
	public RegexStringIteratorData filter(IRegexStringIterator iterator) {
		RegexStringUtility utility = iterator.utility();// 获取解析组件
		RegexStringIteratorData data = new RegexStringIteratorData();
		try {
			if (!iterator.available()) {
				data.m_kMeta = MetaType.END;
				data.m_chCurrent = MetaType.END.getChar();
			} else {
				data.m_kMeta = iterator.meta();
				data.m_chCurrent = iterator.current();
				iterator.next();
				if (data.m_kMeta == MetaType.SINGLE_QUOTE) {// 过滤单引号
					data.m_kMeta = MetaType.NULL;
				} else if (data.m_kMeta == MetaType.ESCAPE) {// 处理转义
					data.m_kMeta = MetaType.CHARACTER;
					data.m_chCurrent = iterator.current();
					iterator.next();
					data.m_chCurrent = utility.fromEscape(data.m_chCurrent,
							RegexError.ESCAPE);
				}
			}
		} catch (RegexException e) {
			System.err.println(e.getPosition() + " : "
					+ e.getMessage());
			data.m_kMeta = MetaType.ERROR;
			data.m_chCurrent = MetaType.ERROR.getChar();
		}
		return data;
	}

	@Override
	public IRegexStringFilterMeta getFilterMeta() {
		return this;
	}

	@Override
	public MetaType[] getMetaTypes() {
		return new MetaType[] { MetaType.SINGLE_QUOTE, MetaType.ESCAPE };
	}
}
