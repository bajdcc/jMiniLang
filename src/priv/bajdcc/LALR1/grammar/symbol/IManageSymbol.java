package priv.bajdcc.LALR1.grammar.symbol;

/**
 * 符号表管理接口
 *
 * @author bajdcc
 */
public interface IManageSymbol extends IQuerySymbol {

	/**
	 * 得到命名空间管理接口
	 */
	public IManageScopeSymbol getManageScopeService();
	
	public IManageDataSymbol getManageDataService();
}
