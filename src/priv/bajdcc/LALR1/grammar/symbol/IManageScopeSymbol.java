package priv.bajdcc.LALR1.grammar.symbol;

import priv.bajdcc.LALR1.grammar.tree.Function;

/**
 * 命名空间管理接口
 *
 * @author bajdcc
 */
public interface IManageScopeSymbol {

	/**
	 * 创建并进入新的命名空间
	 */
	public void enterScope();

	/**
	 * 删除当前命名空间，返回上层
	 */
	public void leaveScope();
	
	/**
	 * 删除所有预期参数
	 */
	public void clearFutureArgs();

	/**
	 * 注册符号
	 * 
	 * @param name
	 *            符号名
	 */
	public void registerSymbol(String name);

	/**
	 * 注册过程
	 * 
	 * @param name
	 *            过程名
	 * @param func
	 *            过程
	 */
	public void registeFunc(String name, Function func);
	
	/**
	 * 注册下个块的参数表
	 * @return 无冲突则返回真
	 */
	public boolean registerFutureSymbol(String name);
}
