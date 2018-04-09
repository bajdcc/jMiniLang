package priv.bajdcc.LALR1.grammar.symbol;

/**
 * 符号表查询接口
 *
 * @author bajdcc
 */
public interface IQuerySymbol {

	/**
	 * 得到命名空间查询接口
	 *
	 * @return 命名空间查询接口
	 */
	IQueryScopeSymbol getQueryScopeService();

	/**
	 * 得到块查询接口
	 *
	 * @return 块查询接口
	 */
	IQueryBlockSymbol getQueryBlockService();
}
