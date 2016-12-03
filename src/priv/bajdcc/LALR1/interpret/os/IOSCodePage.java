package priv.bajdcc.LALR1.interpret.os;

/**
 * 【操作系统】代码页接口
 *
 * @author bajdcc
 */
public interface IOSCodePage {

	/**
	 * 返回页名
	 * @return 页名
	 */
	String getName();

	/**
	 * 返回代码
	 * @return 代码
	 */
	String getCode();
}
