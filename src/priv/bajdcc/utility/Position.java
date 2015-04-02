package priv.bajdcc.utility;

/**
 * 位置
 * 
 * @author bajdcc
 *
 */
public class Position implements Cloneable {
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
	
	public Position(Position obj) {
		m_iColumn = obj.m_iColumn;
		m_iLine = obj.m_iLine;
	}

	public Position(int col, int line) {
		m_iColumn = col;
		m_iLine = line;
	}

	@Override
	public String toString() {
		return m_iLine + "," + m_iColumn;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
