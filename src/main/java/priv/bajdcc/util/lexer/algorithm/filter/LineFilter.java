package priv.bajdcc.util.lexer.algorithm.filter;

import priv.bajdcc.util.lexer.regex.IRegexStringFilter;
import priv.bajdcc.util.lexer.regex.IRegexStringFilterMeta;
import priv.bajdcc.util.lexer.regex.IRegexStringIterator;
import priv.bajdcc.util.lexer.regex.RegexStringIteratorData;
import priv.bajdcc.util.lexer.token.MetaType;

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
			data.kMeta = MetaType.END;
			data.chCurrent = MetaType.END.getChar();
		} else {
			data.kMeta = iterator.meta();
			data.chCurrent = iterator.current();
			iterator.next();
			if (data.kMeta == MetaType.ESCAPE) {// 过滤转义换行
				iterator.next();
				iterator.snapshot();
				data.kMeta = iterator.meta();
				if (data.kMeta == MetaType.NEW_LINE
						|| data.kMeta == MetaType.CARRIAGE_RETURN) {// 确认换行
					iterator.next();
					iterator.cover();
					data.kMeta = iterator.meta();
					if (data.kMeta == MetaType.NEW_LINE
							|| data.kMeta == MetaType.CARRIAGE_RETURN) {// 确认换行
						iterator.discard();
						iterator.next();
					} else {
						iterator.restore();
					}
					data.kMeta = MetaType.MUST_SAVE;
					data.chCurrent = iterator.current();
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
