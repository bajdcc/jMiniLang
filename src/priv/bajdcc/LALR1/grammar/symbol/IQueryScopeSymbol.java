package priv.bajdcc.LALR1.grammar.symbol;

import priv.bajdcc.LALR1.grammar.tree.Function;
import priv.bajdcc.util.lexer.token.Token;

/**
 * 命名空间查询接口
 *
 * @author bajdcc
 */
public interface IQueryScopeSymbol {

	/**
	 * 创建并进入新的命名空间
	 * 
	 * @param name
	 *            查询的符号名
	 * @return 符号名是否存在
	 */
	public boolean findDeclaredSymbol(String name);

	/**
	 * 在当前块下是否为唯一符号（检查重复定义）
	 * 
	 * @param name
	 *            符号名
	 * @return 是否唯一
	 */
	public boolean isUniqueSymbolOfBlock(String name);

	/**
	 * 获得入口名，一般为main
	 * 
	 * @return 入口名
	 */
	public String getEntryName();

	/**
	 * 获得入口单词，一般为main
	 * 
	 * @return 入口单词
	 */
	public Token getEntryToken();

	/**
	 * 根据过程名查找过程对象
	 * 
	 * @param name
	 *            查询的过程名
	 * 
	 * @return 过程对象
	 */
	public Function getFuncByName(String name);

	/**
	 * 根据真实过程名查找过程对象
	 * 
	 * @param name
	 *            查询的过程名
	 * 
	 * @return 过程对象
	 */
	public Function getFuncByRealName(String name);

	/**
	 * 过程名是否已被占用
	 * 
	 * @param name
	 *            查询的过程名
	 * 
	 * @return 是否占用
	 */
	public boolean isRegisteredFunc(String name);
}