package priv.bajdcc.lexer.regex;

import priv.bajdcc.lexer.token.MetaType;

/**
 * 单词类型解析接口
 * 
 * @author bajdcc
 *
 */
public interface IRegexStringFilterMeta {
	
	/**
	 * 返回单词类型数组
	 */
	public MetaType[] getMetaTypes();
}
