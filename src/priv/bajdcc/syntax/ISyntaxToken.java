package priv.bajdcc.syntax;

/**
 * 记号控制器
 *
 * @author bajdcc
 */
public interface ISyntaxToken {

	/**
	 * 返回记号类型
	 */
	public int getTokenID();

	/**
	 * 控制器是否有效
	 */
	public boolean available();

	/**
	 * 向前
	 */
	public void previous();

	/**
	 * 向后
	 */
	public void next();

	/**
	 * 返回记号所处位置
	 */
	public int getPosition();
}
