package priv.bajdcc.semantic.tracker;

import priv.bajdcc.utility.Position;

/**
 * 错误
 *
 * @author bajdcc
 */
public class TrackerError {

	/**
	 * 错误信息
	 */
	public String m_strMessage = "";

	/**
	 * 位置
	 */
	public Position m_Position = null;

	public TrackerError(Position pos) {
		m_Position = pos;
	}

	@Override
	public String toString() {
		return String.format("位置：[%s] 信息：%s", m_Position.toString(),
				m_strMessage);
	}
}
