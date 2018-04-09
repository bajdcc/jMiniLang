package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import priv.bajdcc.util.lexer.token.KeywordType;
import priv.bajdcc.util.lexer.token.OperatorType;

/**
 * 【语义分析】返回语句
 *
 * @author bajdcc
 */
public class StmtReturn implements IStmt {

	private IExp exp = null;

	private boolean yield = false;

	public IExp getExp() {
		return exp;
	}

	public void setExp(IExp exp) {
		this.exp = exp;
	}

	public boolean isYield() {
		return yield;
	}

	public void setYield(boolean yield) {
		this.yield = yield;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {
		if (exp != null) {
			exp.analysis(recorder);
		}
	}

	@Override
	public void genCode(ICodegen codegen) {
		if (!yield) {
			if (exp != null) {
				exp.genCode(codegen);
			} else {
				codegen.genCode(RuntimeInst.ipushx);
			}
			codegen.genCode(RuntimeInst.iret);
		} else {
			if (exp != null) {
				exp.genCode(codegen);
				codegen.genCode(RuntimeInst.iyldi);
				codegen.genCode(RuntimeInst.iyldl);
			} else {
				codegen.genCode(RuntimeInst.ipushn);
				codegen.genCode(RuntimeInst.iyldi);
				codegen.genCode(RuntimeInst.iyldl);
			}
		}
	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		StringBuilder sb = new StringBuilder();
		sb.append(prefix.toString());
		if (yield) {
			sb.append(KeywordType.YIELD.getName());
			sb.append(" ");
		}
		if (exp != null) {
			sb.append(KeywordType.RETURN.getName());
			sb.append(" ");
			sb.append(exp.print(prefix));
		} else if (yield) {
			sb.append(KeywordType.BREAK.getName());
		} else {
			sb.append(KeywordType.RETURN.getName());
		}
		sb.append(OperatorType.SEMI.getName());
		return sb.toString();
	}

	@Override
	public void addClosure(IClosureScope scope) {
		if (exp != null) {
			exp.addClosure(scope);
		}
	}
}
