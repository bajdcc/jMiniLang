package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import priv.bajdcc.util.lexer.token.OperatorType;

/**
 * 【语义分析】间接寻址赋值
 *
 * @author bajdcc
 */
public class ExpIndexAssign implements IExp {

	/**
	 * 对象
	 */
	private IExp exp = null;

	/**
	 * 索引
	 */
	private IExp index = null;

	/**
	 * 对象
	 */
	private IExp obj = null;

	public IExp getExp() {
		return exp;
	}

	public void setExp(IExp exp) {
		this.exp = exp;
	}

	public IExp getIndex() {
		return index;
	}

	public void setIndex(IExp index) {
		this.index = index;
	}

	public IExp getObj() {
		return obj;
	}

	public void setObj(IExp obj) {
		this.obj = obj;
	}

	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public boolean isEnumerable() {
		return false;
	}

	@Override
	public IExp simplify(ISemanticRecorder recorder) {
		exp = exp.simplify(recorder);
		index = index.simplify(recorder);
		obj = obj.simplify(recorder);
		return this;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {
		exp.analysis(recorder);
		index.analysis(recorder);
		obj.analysis(recorder);
	}

	@Override
	public void genCode(ICodegen codegen) {
		obj.genCode(codegen);
		exp.genCode(codegen);
		index.genCode(codegen);
		codegen.genCode(RuntimeInst.iidxa);
	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		StringBuilder sb = new StringBuilder();
		sb.append(exp.print(prefix));
		sb.append(OperatorType.LSQUARE.getName());
		sb.append(index.print(prefix));
		sb.append(OperatorType.RSQUARE.getName());
		sb.append(" ");
		sb.append(OperatorType.ASSIGN.getName());
		sb.append(" ");
		sb.append(obj.print(prefix));
		return sb.toString();
	}

	@Override
	public void addClosure(IClosureScope scope) {
		exp.addClosure(scope);
		index.addClosure(scope);
		obj.addClosure(scope);
	}

	@Override
	public void setYield() {

	}
}
