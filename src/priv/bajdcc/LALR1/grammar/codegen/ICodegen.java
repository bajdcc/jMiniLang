package priv.bajdcc.LALR1.grammar.codegen;

import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.tree.Function;

/**
 * 【目标代码生成】接口
 *
 * @author bajdcc
 */
public interface ICodegen {

	public void genFuncEntry(String funcName);
	public void genCode(RuntimeInst inst);
	public void genCode(RuntimeInst inst, int op1);
	public void genCode(RuntimeInst inst, int op1, int op2);
	public int genDataRef(Object object);
	public int getFuncIndex(Function func);
}
