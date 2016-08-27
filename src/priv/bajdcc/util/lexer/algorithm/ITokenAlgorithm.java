package priv.bajdcc.util.lexer.algorithm;

import java.util.HashMap;

import priv.bajdcc.util.lexer.regex.IRegexStringFilter;
import priv.bajdcc.util.lexer.regex.IRegexStringIterator;
import priv.bajdcc.util.lexer.token.MetaType;
import priv.bajdcc.util.lexer.token.Token;

/**
 * 检测/读取单词的算法接口
 * 
 * @author bajdcc
 */
public interface ITokenAlgorithm {

	/**
	 * 当前算法是否接受相应Visitor对象（即是否匹配）
	 * 
	 * @param iterator
	 *            字符串迭代对象
	 * @param token
	 *            返回的单词（可能出错，或者为EOL、EOF）
	 * @return 算法匹配结果
	 */
	boolean accept(IRegexStringIterator iterator, Token token);

	/**
	 * 返回字符串过滤组件
	 */
	IRegexStringFilter getRegexStringFilter();

	/**
	 * 返回字符类型哈希映射表
	 */
	HashMap<Character, MetaType> getMetaHash();

	/**
	 * 返回正则表达式字符串
	 * 
	 * @param string
	 *            匹配的字符串
	 * @param token
	 *            输入的单词
	 * @return 输出的单词
	 */
	Token getToken(String string, Token token);
	
	/**
	 * 返回正则表达式描述
	 * @return 正则表达式描述
	 */
	String getRegexDescription();
}
