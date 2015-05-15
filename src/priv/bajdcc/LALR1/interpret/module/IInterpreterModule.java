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
	 * @return
	 */
	public String getModuleName();
	
	/**
	 * 返回代码页
	 * @return
	 */
	public RuntimeCodePage getCodePage() throws Exception;
}
