package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import priv.bajdcc.util.lexer.token.OperatorType;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

/**
 * 【语义分析】间接寻址
 *
 * @author bajdcc
 */
public class ExpIndex implements IExp {

	/**
	 * 对象
	 */
	private IExp exp = null;

	/**
	 * 索引
	 */
	private IExp index = null;

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
		return this;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {
		exp.analysis(recorder);
		index.analysis(recorder);
	}

	@Override
	public void genCode(ICodegen codegen) {
		exp.genCode(codegen);
		index.genCode(codegen);
		codegen.genCode(RuntimeInst.iidx);
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
		return sb.toString();
	}

	@Override
	public void addClosure(IClosureScope scope) {
		exp.addClosure(scope);
		index.addClosure(scope);
	}

	@Override
	public void setYield() {

	}
}
