package priv.bajdcc.LALR1.interpret.module;

import priv.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;

/**
 * 【解释器】解释器扩展接口
 *
 * @author bajdcc
 */
public interface IInterpreterModule {

	/**
	 * 返回模块名
	 *
	 * @return 模块名
	 */
	String getModuleName();

	/**
	 * 返回模块代码
	 *
	 * @return 模块代码
	 */
	String getModuleCode();

	/**
	 * 返回代码页
	 *
	 * @return 代码页
	 * @throws Exception 异常
	 */
	RuntimeCodePage getCodePage() throws Exception;
}
