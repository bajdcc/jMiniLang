package priv.bajdcc.util.lexer.regex;

import java.util.Comparator;

/**
 * 字符范围
 *
 * @author bajdcc
 */
public class CharacterRange {

	/**
	 * 下限（包含）
	 */
	public char chLowerBound = 0;

	/**
	 * 上限（包含）
	 */
	public char chUpperBound = 0;

	public CharacterRange() {

	}

	public CharacterRange(char lower, char upper) {
		chLowerBound = lower;
		chUpperBound = upper;
	}

	/**
	 * 当前区间是否包含字符
	 *
	 * @param ch 字符
	 * @return 比较结果
	 */
	public boolean include(char ch) {
		return ch >= chLowerBound && ch <= chUpperBound;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (chLowerBound == chUpperBound) {
			sb.append(printChar(chLowerBound));
		} else {
			sb.append(printChar(chLowerBound)).append("-").append(printChar(chUpperBound));
		}
		return sb.toString();
	}

	private static String printChar(char ch) {
		if (Character.isISOControl(ch)) {
			return String.format("[\\u%04x]", (int) ch);
		} else {
			return String.format("[\\u%04x,'%c']", (int) ch, ch);
		}
	}

}

class CharacterRangeComparator implements Comparator<CharacterRange> {

	@Override
	public int compare(CharacterRange o1, CharacterRange o2) {
		if (o1.chLowerBound < o2.chLowerBound) {
			return -1;
		}
		if (o1.chLowerBound == o2.chLowerBound) {
			if (o1.chUpperBound < o2.chUpperBound) {
				return -1;
			}
			if (o1.chUpperBound == o2.chUpperBound) {
				return 0;
			}
		}
		return 1;
	}
}