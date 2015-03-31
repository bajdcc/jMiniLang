package priv.bajdcc.utility;

import java.util.BitSet;

/**
 * 二重布尔矩阵
 *
 * @author bajdcc
 */
public class BitVector2 {
	/**
	 * 内部一重布尔数组
	 */
	BitSet m_BV = null;

	/**
	 * 行数
	 */
	int m_nX = 0;

	/**
	 * 列数
	 */
	int m_nY = 0;

	public BitVector2(int nx, int ny) {
		if (nx <= 0 || ny <= 0) {
			throw new NegativeArraySizeException();
		}
		m_nX = nx;
		m_nY = ny;
		m_BV = new BitSet(nx * ny);
	}

	private BitVector2() {

	}

	/**
	 * 全部置位
	 */
	public void set() {
		m_BV.set(0, m_nX * m_nY - 1);
	}

	/**
	 * 置位
	 * 
	 * @param x
	 *            行
	 * @param y
	 *            列
	 */
	public void set(int x, int y) {
		m_BV.set(x * m_nX + y);
	}

	/**
	 * 置位
	 * 
	 * @param x
	 *            行
	 * @param y
	 *            列
	 * @param value
	 *            设置的值
	 */
	public void set(int x, int y, boolean value) {
		m_BV.set(x * m_nX + y, value);
	}

	/**
	 * 位置测试
	 * 
	 * @param x
	 *            行
	 * @param y
	 *            列
	 * @return 位
	 */
	public boolean test(int x, int y) {
		return m_BV.get(x * m_nX + y);
	}

	/**
	 * 全部清零
	 */
	public void clear() {
		m_BV.clear();
	}

	/**
	 * 清零
	 * 
	 * @param x
	 *            行
	 * @param y
	 *            列
	 */
	public void clear(int x, int y) {
		m_BV.clear(x * m_nX + y);
	}

	@Override
	public Object clone() {
		BitVector2 bv2 = new BitVector2();
		bv2.m_nX = m_nX;
		bv2.m_nY = m_nY;
		bv2.m_BV = (BitSet) m_BV.clone();
		return bv2;
	}
}
