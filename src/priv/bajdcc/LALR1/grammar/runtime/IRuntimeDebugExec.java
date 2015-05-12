package priv.bajdcc.LALR1.grammar.runtime;

import java.util.List;

/**
 * 【扩展】外部化过程接口
 *
 * @author bajdcc
 */
public interface IRuntimeDebugExec {

	/**
	 * 调用外部化过程
	 * @return 过程返回值，若没有则返回空
	 */
	public RuntimeObject ExternalProcCall(List<RuntimeObject> args);
	
	/**
	 * 返回参数类型，用于运行时类型检查
	 * @return 若无参数则返回空
	 */
	public List<RuntimeObjectType> getArgsType();
	
	/**
	 * 返回过程文档
	 * @return 文档
	 */
	public String getDoc();
}
