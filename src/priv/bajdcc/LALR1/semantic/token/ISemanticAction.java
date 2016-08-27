package priv.bajdcc.LALR1.semantic.token;

import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.symbol.IManageSymbol;

/**
 * 【语义分析】语义动作接口
 *
 * @author bajdcc
 */
public interface ISemanticAction {

	/**
	 * 处理语义动作
	 * @param indexed 参数
	 * @param manage 符号表管理接口
	 * @param access 单词流
	 * @param recorder 错误记录器
     */
	void handle(IIndexedData indexed, IManageSymbol manage,
				IRandomAccessOfTokens access, ISemanticRecorder recorder);
}
