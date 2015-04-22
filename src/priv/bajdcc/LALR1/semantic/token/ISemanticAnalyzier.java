package priv.bajdcc.LALR1.semantic.token;

import priv.bajdcc.LALR1.grammar.symbol.IQuerySymbol;

/**
 * 语义分析接口
 *
 * @author bajdcc
 */
public interface ISemanticAnalyzier {

	/**
	 * 语义处理接口
	 * 
	 * @param indexed
	 *            索引包接口
	 * @param factory
	 *            单词工厂接口
	 * @return 处理后的数据
	 */
	public Object handle(IIndexedData indexed, IQuerySymbol factory);
}
