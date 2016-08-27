package priv.bajdcc.util.lexer.regex;

/**
 * 匹配信息
 * 
 * @author bajdcc
 *
 */
public interface IRegexStringAttribute {

	/**
	 * 设置匹配结果
	 * 
	 * @param result
	 *            匹配结果
	 */
	void setResult(String result);

	/**
	 * 返回匹配结果
	 * 
	 * @return 匹配结果
	 */
	String getResult();

	/**
	 * 返回贪婪模式
	 * 
	 * @return 是否为贪婪模式
	 */
	boolean getGreedMode();
}
