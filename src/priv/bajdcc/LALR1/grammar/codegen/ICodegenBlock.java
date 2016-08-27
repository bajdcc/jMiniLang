package priv.bajdcc.LALR1.grammar.codegen;

import priv.bajdcc.LALR1.grammar.runtime.RuntimeInstUnary;

/**
 * 【目标代码生成】块接口
 *
 * @author bajdcc
 */
public interface ICodegenBlock {

	RuntimeInstUnary genBreak();
	RuntimeInstUnary genContinue();
	void enterBlockEntry(CodegenBlock block);
	void leaveBlockEntry();
	boolean isInBlock();
}
