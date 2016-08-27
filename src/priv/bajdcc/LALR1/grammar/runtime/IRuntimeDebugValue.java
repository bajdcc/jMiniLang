package priv.bajdcc.LALR1.grammar.runtime;

/**
 * 【扩展】外部化值接口
 *
 * @author bajdcc
 */
public interface IRuntimeDebugValue {

	/**
	 * 获取外部化对象
	 *
	 * @return 外部化对象
	 */
	RuntimeObject getRuntimeObject();
}
