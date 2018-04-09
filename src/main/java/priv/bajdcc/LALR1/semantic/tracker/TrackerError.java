package priv.bajdcc.LALR1.semantic.tracker;

import priv.bajdcc.util.Position;

/**
 * 错误
 *
 * @author bajdcc
 */
public class TrackerError {

	/**
	 * 错误信息
	 */
	public String message = "";

	/**
	 * 位置
	 */
	public Position position = null;

	public TrackerError(Position pos) {
		position = pos;
	}

	@Override
	public String toString() {
		return String.format("位置：[%s] 信息：%s", position.toString(),
				message);
	}
}
