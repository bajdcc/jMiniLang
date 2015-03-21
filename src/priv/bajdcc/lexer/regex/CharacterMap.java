package priv.bajdcc.lexer.regex;

import java.util.ArrayList;

/**
 * 字符集合，将字符范围按状态分组（Sigma集合）
 * 
 * @see CharacterRange
 * @author bajdcc
 *
 */
public class CharacterMap implements IRegexComponentVisitor {

	/**
	 * Unicode字符的范围0-65535
	 */
	private static final int g_iUnicodeMapSize = 0x10000;

	/**
	 * 遍历的结点的深度
	 */
	private int m_iLevel = 0;

	/**
	 * 重新分组后的范围集合
	 */
	private ArrayList<CharacterRange> m_arrRanges = new ArrayList<CharacterRange>();

	/**
	 * 返回重构（规范化）的字符区间集合
	 * 
	 * @return 重构的字符区间集合
	 */
	public ArrayList<CharacterRange> getRanges() {
		return m_arrRanges;
	}

	/**
	 * 面向字符（Unicode）的状态映射表，大小65536
	 */
	private int[] m_arrStatus = new int[g_iUnicodeMapSize];

	/**
	 * 返回Unicode字符映射表
	 * 
	 * @return 字符映射表
	 */
	public int[] getStatus() {
		return m_arrStatus;
	}

	/**
	 * 排序方法
	 */
	private CharacterRangeComparator m_Comparator = new CharacterRangeComparator();

	@Override
	public void visitBegin(Charset node) {
		increaseLevel();
		if (node.m_bReverse) {
			preceedReverse(node);// 处理取反集合
		}
		addRanges(node);// 将状态集合分解重构
	}

	@Override
	public void visitBegin(Constructure node) {
		increaseLevel();
	}

	@Override
	public void visitBegin(Repetition node) {
		increaseLevel();
	}

	@Override
	public void visitEnd(Charset node) {
		decreaseLevel();
	}

	@Override
	public void visitEnd(Constructure node) {
		decreaseLevel();
	}

	@Override
	public void visitEnd(Repetition node) {
		decreaseLevel();
	}

	/**
	 * 查找指定字符所在的区间范围序号
	 * 
	 * @param ch
	 *            字符
	 * @return 序号，-1代表不存在
	 */
	public int find(char ch) {
		for (int i = 0; i < m_arrRanges.size(); i++) {
			CharacterRange range = m_arrRanges.get(i);
			if (range.include(ch)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 化简、消去字符集中的取反属性
	 * 
	 * @param charset
	 *            字符集
	 */
	private void preceedReverse(Charset charset) {
		charset.m_bReverse = false;
		charset.m_arrPositiveBounds.sort(m_Comparator);// 对集合排序
		ArrayList<CharacterRange> ranges = new ArrayList<CharacterRange>();
		CharacterRange oldRange = new CharacterRange();
		for (CharacterRange range : charset.m_arrPositiveBounds) {
			if (range.m_chLowerBound > oldRange.m_chUpperBound + 1) {// 当前下界大于之前上界，故添加
				CharacterRange midRange = new CharacterRange(
						(char) (oldRange.m_chUpperBound + 1),
						(char) (range.m_chLowerBound - 1));// 添加范围，从之前上界到当前下界
				ranges.add(midRange);
				oldRange = range;
			}
		}
		if (oldRange.m_chUpperBound < g_iUnicodeMapSize - 1) {
			CharacterRange midRange = new CharacterRange(
					(char) (oldRange.m_chLowerBound + 1),
					(char) (g_iUnicodeMapSize - 1));
			ranges.add(midRange);// 添加最后的范围
		}
		charset.m_arrPositiveBounds = ranges;
		charset.m_arrPositiveBounds.sort(m_Comparator);// 对集合排序
	}

	/**
	 * 深度加一
	 */
	private void increaseLevel() {
		m_iLevel++;
	}

	/**
	 * 深度减一
	 */
	private void decreaseLevel() {
		m_iLevel--;
		if (m_iLevel == 0) {// 遍历到根结点
			putStatus();
		}
	}

	/**
	 * 添加所有状态
	 */
	private void putStatus() {
		for (int i = 0; i < g_iUnicodeMapSize; i++) {
			m_arrStatus[i] = -1;// 所有元素置为无效状态-1
		}
		for (int i = 0; i < m_arrRanges.size(); i++) {
			int lower = m_arrRanges.get(i).m_chLowerBound;
			int upper = m_arrRanges.get(i).m_chUpperBound;
			for (int j = lower; j <= upper; j++) {
				m_arrStatus[j] = i;// 将范围i中包括的所有元素置为i
			}
		}
	}

	/**
	 * 处理新添加的字符范围，必要时将其分解，使得元素间相互独立
	 * 
	 * @param CharacterRange
	 *            字符区间
	 */
	private void addRange(CharacterRange newRange) {

		for (int i = 0; i < m_arrRanges.size(); i++) {
			m_arrRanges.sort(m_Comparator);
			CharacterRange oldRange = m_arrRanges.get(i);
			/*
			 * 防止新增区间[New]与之前区间[Old]产生交集，若有，则将集合分裂
			 */
			if (oldRange.m_chLowerBound < newRange.m_chLowerBound) {
				if (oldRange.m_chUpperBound < newRange.m_chLowerBound) {

					// [####Old####]_______________
					// ______________[#####New####]
					// [Old]比[New]小，没有交集

				} else if (oldRange.m_chUpperBound < newRange.m_chUpperBound) {

					// [######Old######]__________
					// ______________[#####New####]
					// [Old]与[New]有交集[New.Lower,Old.Upper]
					// [Old]=[Old.Lower,New.Lower-1]
					// [Mid]=[New.Lower,Old.Upper]
					// [New]=[Old.Upper+1,New.Upper]

					m_arrRanges.remove(i);
					newRange.m_chLowerBound = (char) (oldRange.m_chUpperBound + 1);
					oldRange.m_chUpperBound = (char) (newRange.m_chLowerBound - 1);
					m_arrRanges.add(oldRange);
					m_arrRanges.add(new CharacterRange(
							(char) (oldRange.m_chUpperBound + 1),
							(char) (newRange.m_chLowerBound - 1)));
					i++;
				} else if (oldRange.m_chUpperBound == newRange.m_chUpperBound) {

					// [###########Old############]
					// ______________[#####New####]
					// [Old]与[New]有交集[New]
					// [Old]=[Old.Lower,New.Lower-1]
					// [New]=[New]

					m_arrRanges.remove(i);
					oldRange.m_chUpperBound = (char) (newRange.m_chLowerBound - 1);
					m_arrRanges.add(newRange);
					m_arrRanges.add(oldRange);
					return;
				} else {

					// [#############Old##############]
					// ______________[#####New####]____
					// [Old]与[New]有交集[New]
					// [Left]=[Old.Lower,New.Lower-1]
					// [Mid]=[New]
					// [Right]=[New.Upper+1]

					m_arrRanges.remove(i);
					m_arrRanges.add(new CharacterRange(oldRange.m_chLowerBound,
							(char) (newRange.m_chUpperBound - 1)));
					m_arrRanges.add(newRange);
					m_arrRanges.add(new CharacterRange(
							(char) (newRange.m_chLowerBound + 1),
							oldRange.m_chUpperBound));
					return;
				}
			} else if (oldRange.m_chLowerBound == newRange.m_chLowerBound) {
				if (oldRange.m_chUpperBound < newRange.m_chUpperBound) {

					// [#######Old#######]
					// [##########New##########]
					// [Old]与[New]有交集[Old]

					newRange.m_chLowerBound = (char) (oldRange.m_chUpperBound + 1);
				} else if (oldRange.m_chUpperBound == newRange.m_chUpperBound) {

					// [#######Old#######]
					// [#######New#######]
					// [Old]=[New]

					return;
				} else {

					// [##########Old##########]
					// [#######New#######]
					// [Old]与[New]有交集[New]
					// [Old]=[New.Upper+1,Old.Upper]
					// [New]=[New]

					m_arrRanges.remove(i);
					oldRange.m_chLowerBound = (char) (newRange.m_chUpperBound + 1);
					m_arrRanges.add(newRange);
					m_arrRanges.add(oldRange);
					return;
				}
			} else if (oldRange.m_chLowerBound <= newRange.m_chUpperBound) {
				if (oldRange.m_chUpperBound < newRange.m_chUpperBound) {

					// ___[#######Old#######]___
					// [##########New##########]
					// [Old]与[New]有交集[Old]
					// [Left]=[New.Lower,Old.Lower-1]
					// [New]=[Old.Upper+1,New.Upper]

					m_arrRanges.add(new CharacterRange(newRange.m_chLowerBound,
							(char) (oldRange.m_chLowerBound - 1)));
					newRange.m_chLowerBound = (char) (oldRange.m_chUpperBound + 1);
					i++;
				} else if (oldRange.m_chUpperBound == newRange.m_chUpperBound) {

					// ______[#######Old#######]
					// [##########New##########]
					// [Old]与[New]有交集[Old]
					// [Old]=[Old]
					// [New]=[New.Lower,Old.Lower-1]

					newRange.m_chUpperBound = (char) (oldRange.m_chLowerBound - 1);
					m_arrRanges.add(newRange);
					return;
				} else {

					// ______[##########Old##########]
					// [##########New##########]______
					// [Old]与[New]有交集[Old.Lower,New.Upper]
					// [Old]=[New.Upper+1,Old.Upper]
					// [Mid]=[Old.Lower,New.Upper]
					// [New]=[New.Lower,Old.Lower-1]

					m_arrRanges.remove(i);
					newRange.m_chUpperBound = (char) (oldRange.m_chLowerBound - 1);
					oldRange.m_chLowerBound = (char) (newRange.m_chUpperBound + 1);
					m_arrRanges.add(oldRange);
					m_arrRanges.add(new CharacterRange(oldRange.m_chLowerBound,
							newRange.m_chUpperBound));
					m_arrRanges.add(newRange);
					return;
				}
			}
		}
		m_arrRanges.add(newRange);
	}

	/**
	 * 处理新添加的字符范围，必要时将其分解，使得元素间相互独立
	 * 
	 * @param charset
	 *            字符集
	 */
	private void addRanges(Charset charset) {
		for (CharacterRange range : charset.m_arrPositiveBounds) {
			addRange(new CharacterRange(range.m_chLowerBound, range.m_chUpperBound));
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (CharacterRange range : m_arrRanges) {
			sb.append(range + System.getProperty("line.separator"));
		}
		return sb.toString();
	}
}
