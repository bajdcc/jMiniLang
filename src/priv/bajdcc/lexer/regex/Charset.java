package priv.bajdcc.lexer.regex;

import java.util.ArrayList;

import priv.bajdcc.lexer.token.MetaType;

/**
 * 字符集
 * 
 * @author bajdcc
 */
public class Charset implements IRegexComponent {

	/**
	 * 包含的范围（正范围）
	 */
	public ArrayList<CharacterRange> m_arrPositiveBounds = new ArrayList<CharacterRange>();

	/**
	 * 是否取反
	 */
	public boolean m_bReverse = false;

	@Override
	public void visit(IRegexComponentVisitor visitor) {
		visitor.visitBegin(this);
		visitor.visitEnd(this);
	}

	/**
	 * 添加范围
	 * 
	 * @param begin
	 *            上限
	 * @param end
	 *            下限
	 */
	public boolean addRange(char begin, char end) {
		if (begin > end) {
			return false;
		}
		for (CharacterRange range : m_arrPositiveBounds) {
			if (begin <= range.m_chLowerBound && end >= range.m_chUpperBound)
				return false;
		}
		m_arrPositiveBounds.add(new CharacterRange(begin, end));
		return true;
	}

	/**
	 * 添加字符
	 * 
	 * @param ch
	 *            字符
	 */
	public boolean addChar(char ch) {
		return addRange(ch, ch);
	}
	
	/**
	 * 当前字符集是否包含指定字符
	 * @param ch 字符
	 * @return 查找结果
	 */
	public boolean include(char ch) {
		for (CharacterRange range : m_arrPositiveBounds) {
			if (range.include(ch)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean comma = false;
		for (CharacterRange range : m_arrPositiveBounds) {
			if (comma)
				sb.append(MetaType.COMMA.getChar());
			sb.append(range);
			if (!comma)
				comma = true;
		}
		return sb.toString();
	}
}
