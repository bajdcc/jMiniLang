package com.bajdcc.LALR1.grammar.tree;

import com.bajdcc.LALR1.grammar.codegen.ICodegen;
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import com.bajdcc.util.lexer.token.KeywordType;
import com.bajdcc.util.lexer.token.OperatorType;

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
		return prefix.toString() +
				KeywordType.THROW.getDesc() +
				" " +
				exp.print(prefix) +
				OperatorType.SEMI.getDesc();
	}

	@Override
	public void addClosure(IClosureScope scope) {
		exp.addClosure(scope);
	}
}