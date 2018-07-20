package com.bajdcc.LALR1.grammar.tree;

import com.bajdcc.LALR1.grammar.codegen.ICodegen;
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import com.bajdcc.util.lexer.token.OperatorType;

import java.util.ArrayList;
import java.util.List;

/**
 * 【语义分析】数组
 *
 * @author bajdcc
 */
public class ExpArray implements IExp {

	/**
	 * 参数
	 */
	private List<IExp> params = new ArrayList<>();

	public List<IExp> getParams() {
		return params;
	}

	public void setParams(List<IExp> params) {
		this.params = params;
	}

	@Override
	public boolean isConstant() {
		for (IExp exp : params) {
			if (!exp.isConstant())
				return false;
		}
		return true;
	}

	@Override
	public boolean isEnumerable() {
		return false;
	}

	@Override
	public IExp simplify(ISemanticRecorder recorder) {
		for (int i = 0; i < params.size(); i++) {
			params.set(i, params.get(i).simplify(recorder));
		}
		return this;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {
		for (IExp exp : params) {
			exp.analysis(recorder);
		}
	}

	@Override
	public void genCode(ICodegen codegen) {
		for (int i = params.size() - 1; i >= 0; i--) {
			params.get(i).genCode(codegen);
		}
		codegen.genCode(RuntimeInst.iarr, params.size());
	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		StringBuilder sb = new StringBuilder();
		sb.append(OperatorType.LSQUARE.getDesc());
		if (!params.isEmpty())
			sb.append(" ");
		for (IExp exp : params) {
			sb.append(exp.print(prefix));
			sb.append(", ");
		}
		if (!params.isEmpty()) {
			sb.deleteCharAt(sb.length() - 1);
			sb.deleteCharAt(sb.length() - 1);
		}
		if (!params.isEmpty())
			sb.append(" ");
		sb.append(OperatorType.RSQUARE.getDesc());
		return sb.toString();
	}

	@Override
	public void addClosure(IClosureScope scope) {
		for (IExp exp : params) {
			exp.addClosure(scope);
		}
	}

	@Override
	public void setYield() {

	}
}
