package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import priv.bajdcc.util.lexer.token.KeywordType;
import priv.bajdcc.util.lexer.token.OperatorType;

/**
 * 【语义分析】类属性赋值表达式
 *
 * @author bajdcc
 */
public class ExpAssignProperty implements IExp {

	/**
	 * 对象
	 */
	private IExp obj = null;

	/**
	 * 属性
	 */
	private IExp property = null;

	/**
	 * 表达式
	 */
	private IExp exp = null;

	public IExp getObj() {
		return obj;
	}

	public void setObj(IExp obj) {
		this.obj = obj;
	}

	public IExp getProperty() {
		return property;
	}

	public void setProperty(IExp property) {
		this.property = property;
	}

	public IExp getExp() {
		return exp;
	}

	public void setExp(IExp exp) {
		this.exp = exp;
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
		return this;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {
		obj.analysis(recorder);
		property.analysis(recorder);
		exp.analysis(recorder);
	}

	@Override
	public void genCode(ICodegen codegen) {
		codegen.genCode(RuntimeInst.iopena);
		obj.genCode(codegen);
		codegen.genCode(RuntimeInst.ipusha);
		property.genCode(codegen);
		codegen.genCode(RuntimeInst.ipusha);
		exp.genCode(codegen);
		codegen.genCode(RuntimeInst.ipusha);
		codegen.genCode(RuntimeInst.ipush, codegen.genDataRef("g_set_property"));
		codegen.genCode(RuntimeInst.icallx);
	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		return obj.print(prefix) + "." + property.print(prefix) +
				" " + OperatorType.ASSIGN.getName() + " " +
				exp.print(prefix);
	}

	@Override
	public void addClosure(IClosureScope scope) {
		exp.addClosure(scope);
	}

	@Override
	public void setYield() {

	}
}