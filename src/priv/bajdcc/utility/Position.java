package priv.bajdcc.utility;

/**
 * 位置
 * 
 * @author bajdcc
 *
 */
public class Position {
	/**
	 * 列号
	 */
	public int m_iColumn = 0;

	/**
	 * 行号
	 */
	public int m_iLine = 0;

	public Position() {
	}

	public Position(int col, int line) {
		m_iColumn = col;
		m_iLine = line;
	}

	@Override
	public String toString() {
		return m_iLine + "," + m_iColumn;
	}
}
