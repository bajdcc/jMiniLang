package com.bajdcc.LALR1.grammar.tree;

import com.bajdcc.LALR1.grammar.codegen.ICodegen;
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import com.bajdcc.util.lexer.token.OperatorType;

/**
 * 【语义分析】表达式语句
 *
 * @author bajdcc
 */
public class StmtExp implements IStmt {

	private IExp exp = null;

	public IExp getExp() {
		return exp;
	}

	public void setExp(IExp exp) {
		this.exp = exp;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {
		if (exp != null) {
			exp.analysis(recorder);
		}
	}

	@Override
	public void genCode(ICodegen codegen) {
		if (exp != null) {
			exp.genCode(codegen);
			codegen.genCode(RuntimeInst.ipop);
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
		if (exp != null) {
			sb.append(exp.print(prefix));
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
