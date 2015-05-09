package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.util.lexer.token.OperatorType;

/**
 * 【语义分析】赋值语句
 *
 * @author bajdcc
 */
public class StmtInvoke implements IStmt {

	private ExpInvoke exp = null;

	public ExpInvoke getExp() {
		return exp;
	}

	public void setExp(ExpInvoke exp) {
		this.exp = exp;
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
		StringBuilder sb = new StringBuilder();
		sb.append(prefix.toString());
		sb.append(exp.print(prefix));
		sb.append(OperatorType.SEMI.getName());
		return sb.toString();
	}
}
