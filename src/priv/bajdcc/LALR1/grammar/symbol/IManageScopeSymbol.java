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
	void enterScope();

	/**
	 * 删除当前命名空间，返回上层
	 */
	void leaveScope();

	/**
	 * 删除所有预期参数
	 */
	void clearFutureArgs();

	/**
	 * 注册符号
	 * 
	 * @param name
	 *            符号名
	 */
	void registerSymbol(String name);

	/**
	 * 注册过程
	 * 
	 * @param name
	 *            过程名
	 * @param func
	 *            过程
	 */
	void registeFunc(String name, Function func);

	/**
	 * 注册下个块的参数表
	 * 
	 * @param name
	 *            参数名
	 * @return 无冲突则返回真
	 */
	boolean registerFutureSymbol(String name);
}
