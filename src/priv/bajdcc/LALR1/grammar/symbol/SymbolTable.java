package priv.bajdcc.LALR1.grammar.symbol;

/**
 * 【语义分析】符号表
 *
 * @author bajdcc
 */
public class SymbolTable implements IQuerySymbol, IManageSymbol {

	/**
	 * 符号表管理
	 */
	private ManageScopeSymbol manageScopeSymbol = new ManageScopeSymbol();

	@Override
	public IManageScopeSymbol getManageScopeService() {
		return manageScopeSymbol;
	}

	@Override
	public IQueryScopeSymbol getQueryScopeService() {
		return manageScopeSymbol;
	}

	@Override
	public String toString() {
		return manageScopeSymbol.toString();
	}
}
