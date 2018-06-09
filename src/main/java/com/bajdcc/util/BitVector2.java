package com.bajdcc.util;

import java.util.BitSet;

/**
 * 二重布尔矩阵
 *
 * @author bajdcc
 */
public class BitVector2 implements Cloneable {
	/**
	 * 内部一重布尔数组
	 */
	BitSet bs = null;

	/**
	 * 行数
	 */
	int nX = 0;

	/**
	 * 列数
	 */
	int nY = 0;

	public BitVector2(int nx, int ny) {
		if (nx <= 0 || ny <= 0) {
			throw new NegativeArraySizeException();
		}
		nX = nx;
		nY = ny;
		bs = new BitSet(nx * ny);
	}

	private BitVector2() {

	}

	/**
	 * 全部置位
	 */
	public void set() {
		bs.set(0, nX * nY - 1);
	}

	/**
	 * 置位
	 *
	 * @param x 行
	 * @param y 列
	 */
	public void set(int x, int y) {
		bs.set(x * nX + y);
	}

	/**
	 * 置位
	 *
	 * @param x     行
	 * @param y     列
	 * @param value 设置的值
	 */
	public void set(int x, int y, boolean value) {
		bs.set(x * nX + y, value);
	}

	/**
	 * 位置测试
	 *
	 * @param x 行
	 * @param y 列
	 * @return 位
	 */
	public boolean test(int x, int y) {
		return bs.get(x * nX + y);
	}

	/**
	 * 全部清零
	 */
	public void clear() {
		bs.clear();
	}

	/**
	 * 清零
	 *
	 * @param x 行
	 * @param y 列
	 */
	public void clear(int x, int y) {
		bs.clear(x * nX + y);
	}

	@Override
	public Object clone() {
		BitVector2 bv2 = null;
		try {
			bv2 = (BitVector2) super.clone();
			bv2.nX = nX;
			bv2.nY = nY;
			bv2.bs = (BitSet) bs.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return bv2;
	}
}
