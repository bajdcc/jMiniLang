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
	 * 查找变量名
	 * 
	 * @param name
	 *            查询的符号名
	 * @return 符号名是否存在
	 */
	boolean findDeclaredSymbol(String name);
	
	/**
	 * 查找变量名（当前命名空间）
	 * 
	 * @param name
	 *            查询的符号名
	 * @return 符号名是否存在
	 */
	boolean findDeclaredSymbolDirect(String name);

	/**
	 * 在当前块下是否为唯一符号（检查重复定义）
	 * 
	 * @param name
	 *            符号名
	 * @return 是否唯一
	 */
	boolean isUniqueSymbolOfBlock(String name);

	/**
	 * 获得入口名，一般为main
	 * 
	 * @return 入口名
	 */
	String getEntryName();

	/**
	 * 获得入口单词，一般为main
	 * 
	 * @return 入口单词
	 */
	Token getEntryToken();

	/**
	 * 根据过程名查找过程对象
	 * 
	 * @param name
	 *            查询的过程名
	 * 
	 * @return 过程对象
	 */
	Function getFuncByName(String name);

	/**
	 * 得到当前的匿名函数
	 *
	 * @return 过程对象
	 */
	Function getLambda();

	/**
	 * 过程名是否已被占用
	 * 
	 * @param name
	 *            查询的过程名
	 * 
	 * @return 是否占用
	 */
	boolean isRegisteredFunc(String name);
}