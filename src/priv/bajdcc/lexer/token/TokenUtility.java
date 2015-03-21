package priv.bajdcc.lexer.token;

/**
 * 单词特性
 * 
 * @author bajdcc
 */
public class TokenUtility {

	/**
	 * 是否为ASCII英文字母
	 * 
	 * @param ch
	 *            字符
	 * @return 是否为ASCII英文字母
	 */
	public static boolean isLetter(char ch) {
		return isUpperLetter(ch) || isLowerLetter(ch);
	}

	/**
	 * 是否为ASCII大写英文字母
	 * 
	 * @param ch
	 *            字符
	 * @return 是否为ASCII大写英文字母
	 */
	public static boolean isUpperLetter(char ch) {
		return ch >= 'A' && ch <= 'Z';
	}

	/**
	 * 是否为ASCII小写英文字母
	 * 
	 * @param ch
	 *            字符
	 * @return 是否为ASCII小写英文字母
	 */
	public static boolean isLowerLetter(char ch) {
		return ch >= 'a' && ch <= 'z';
	}
}
