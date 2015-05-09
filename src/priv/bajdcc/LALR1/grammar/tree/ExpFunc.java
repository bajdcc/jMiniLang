package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;

/**
 * 【语义分析】函数定义表达式
 *
 * @author bajdcc
 */
public class ExpFunc implements IExp {

	/**
	 * 调用函数
	 */
	private Function func = null;

	public Function getFunc() {
		return func;
	}

	public void setFunc(Function func) {
		this.func = func;
	}

	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public IExp simplify(ISemanticRecorder recorder) {
		return this;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {

	}

	@Override
	public void genCode(ICodegen codegen) {

	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		return func.print(prefix);
	}
}
