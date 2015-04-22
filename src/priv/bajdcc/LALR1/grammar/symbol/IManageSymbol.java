package priv.bajdcc.LALR1.grammar.symbol;

/**
 * 符号表管理接口
 *
 * @author bajdcc
 */
public interface IManageSymbol {

	/**
	 * 创建并进入新的命名空间
	 */
	public void enterNamespace();
	
	/**
	 * 创建并进入新的命名空间
	 * @param name 命名空间名称
	 */
	public void enterNamespace(String name);
	
	/**
	 * 删除当前命名空间，返回上层
	 */
	public void leaveNamespace();
}
