package priv.bajdcc.util.lexer.regex;

import priv.bajdcc.util.lexer.token.MetaType;

/**
 * 单词类型解析接口
 * 
 * @author bajdcc
 *
 */
public interface IRegexStringFilterMeta {
	
	/**
	 * 返回单词类型数组
	 * @return 单词类型数组
	 */
	MetaType[] getMetaTypes();
}
