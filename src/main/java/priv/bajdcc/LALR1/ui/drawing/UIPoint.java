package priv.bajdcc.LALR1.ui.drawing;

/**
 * 【界面】点
 *
 * @author bajdcc
 */
public class UIPoint {
	public int x, y;

	public UIPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "UIPoint{" +
				"X=" + x +
				", Y=" + y +
				'}';
	}
}
