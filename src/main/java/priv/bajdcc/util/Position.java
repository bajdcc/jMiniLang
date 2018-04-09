package priv.bajdcc.util;

/**
 * 位置
 *
 * @author bajdcc
 */
public class Position implements Cloneable {
	/**
	 * 列号
	 */
	public int iColumn = 0;

	/**
	 * 行号
	 */
	public int iLine = 0;

	public Position() {
	}

	public Position(Position obj) {
		iColumn = obj.iColumn;
		iLine = obj.iLine;
	}

	public Position(int col, int line) {
		iColumn = col;
		iLine = line;
	}

	@Override
	public String toString() {
		return iLine + "," + iColumn;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
