package priv.bajdcc.LALR1.grammar.symbol;

/**
 * 【语义分析】符号表
 *
 * @author bajdcc
 */
public class SymbolTable implements IQuerySymbol, IManageSymbol {

	/**
	 * 根命名空间，即全局命名空间
	 */
	private Namespace root = null;

	/**
	 * 当前命名空间
	 */
	private Namespace current = null;

	@Override
	public void enterNamespace() {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void enterNamespace(String name) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void leaveNamespace() {
		// TODO 自动生成的方法存根
		
	}
}
