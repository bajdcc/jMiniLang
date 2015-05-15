package priv.bajdcc.LALR1.grammar.runtime;

/**
 * 【运行时】运行时状态查询
 *
 * @author bajdcc
 */
public interface IRuntimeStatus {

	/**
	 * 得到过程的文档
	 * @param name 过程名
	 * @return
	 */
	public String getHelpString(String name);

	/**
	 * 得到过程的地址
	 * @param name 过程名
	 * @return
	 * @throws RuntimeException 
	 */
	public int getFuncAddr(String name) throws RuntimeException;
}
