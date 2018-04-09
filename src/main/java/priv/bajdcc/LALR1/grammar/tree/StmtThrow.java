package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import priv.bajdcc.util.lexer.token.KeywordType;
import priv.bajdcc.util.lexer.token.OperatorType;

/**
 * 【语义分析】异常处理语句
 *
 * @author bajdcc
 */
public class StmtThrow implements IStmt {

	/**
	 * 异常表达式
	 */
	private IExp exp = null;

	public IExp getExp() {
		return exp;
	}

	public void setExp(IExp exp) {
		this.exp = exp;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {
		exp.analysis(recorder);
	}

	@Override
	public void genCode(ICodegen codegen) {
		exp.genCode(codegen);
		codegen.genCode(RuntimeInst.ithrow);
	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		StringBuilder sb = new StringBuilder();
		sb.append(prefix.toString());
		sb.append(KeywordType.THROW.getName());
		sb.append(" ");
		sb.append(exp.print(prefix));
		sb.append(OperatorType.SEMI.getName());
		return sb.toString();
	}

	@Override
	public void addClosure(IClosureScope scope) {
		exp.addClosure(scope);
	}
}