package priv.bajdcc.lexer.regex;

/**
 * 字符串过滤接口
 * 
 * @author bajdcc
 *
 */
public interface IRegexStringFilter {
	
	/**
	 * 过滤
	 */
	public RegexStringIteratorData filter(IRegexStringIterator iterator);
	
	
	/**
	 * 返回类型过滤接口
	 */
	public IRegexStringFilterMeta getFilterMeta();
}
