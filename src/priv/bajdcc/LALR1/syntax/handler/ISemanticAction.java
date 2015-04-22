package priv.bajdcc.LALR1.syntax.handler;

import priv.bajdcc.LALR1.grammar.symbol.IManageSymbol;

/**
 * 【语义分析】语义动作接口
 *
 * @author bajdcc
 */
public interface ISemanticAction {
	/**
	 * 处理语义动作
	 * 
	 * @param manage
	 *            符号表管理接口
	 */
	public void handle(IManageSymbol manage);
}
