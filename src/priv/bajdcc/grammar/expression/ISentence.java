package priv.bajdcc.grammar.expression;

import priv.bajdcc.grammar.codegen.ICodegen;
import priv.bajdcc.grammar.semantic.ISemanticRecorder;

/**
 *【语义分析】基本语句接口
 *
 * @author bajdcc
 */
public interface ISentence {

	/**
	 * 语义分析
	 * 
	 * @param recorder
	 *            错误记录器
	 */
	public void analysis(ISemanticRecorder recorder);

	/**
	 * 生成目标代码
	 * 
	 * @param codegen
	 *            代码生成接口
	 */
	public void genCode(ICodegen codegen);
}
