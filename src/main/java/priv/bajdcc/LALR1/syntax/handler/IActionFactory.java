package priv.bajdcc.LALR1.syntax.handler;

/**
 * 【语义分析】语义动作工厂接口
 *
 * @author bajdcc
 */
public interface IActionFactory {

	/**
	 * 调用指定过程
	 *
	 * @param name 过程名称
	 */
	void invoke(String name);
}
