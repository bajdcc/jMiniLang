package priv.bajdcc.LALR1.grammar.tree.closure;

/**
 * 闭包
 *
 * @author bajdcc
 */
public interface IClosureScope {

	public void addRef(Object obj);
	public void addDecl(Object obj);
}
