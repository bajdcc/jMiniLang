package com.bajdcc.LALR1.grammar.tree;

import com.bajdcc.LALR1.grammar.codegen.ICodegen;
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import com.bajdcc.util.lexer.token.OperatorType;
import com.bajdcc.util.lexer.token.Token;

/**
 * 【语义分析】间接寻址赋值
 *
 * @author bajdcc
 */
public class ExpIndexAssign implements IExp {

	/**
	 * 操作符
	 */
	private Token token = null;

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

	public void setToken(Token token) {
		this.token = token;
	}

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
		if (token == null || token.object == OperatorType.EQ_ASSIGN) {
			obj.genCode(codegen);
		} else {
			exp.genCode(codegen);
			index.genCode(codegen);
			codegen.genCode(RuntimeInst.iidx);
			obj.genCode(codegen);
			switch ((OperatorType) token.object) {
				case PLUS_ASSIGN:
					codegen.genCode(RuntimeInst.iadd);
					break;
				case MINUS_ASSIGN:
					codegen.genCode(RuntimeInst.isub);
					break;
				case TIMES_ASSIGN:
					codegen.genCode(RuntimeInst.imul);
					break;
				case DIV_ASSIGN:
					codegen.genCode(RuntimeInst.idiv);
					break;
				case AND_ASSIGN:
					codegen.genCode(RuntimeInst.iand);
					break;
				case OR_ASSIGN:
					codegen.genCode(RuntimeInst.ior);
					break;
				case XOR_ASSIGN:
					codegen.genCode(RuntimeInst.ixor);
					break;
				case MOD_ASSIGN:
					codegen.genCode(RuntimeInst.imod);
					break;
			}
		}
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
		String sb = exp.print(prefix) +
				OperatorType.LSQUARE.getName() +
				index.print(prefix) +
				OperatorType.RSQUARE.getName() +
				" " +
				((token == null) ? OperatorType.ASSIGN.getName() : token.toRealString()) +
				" " +
				obj.print(prefix);
		return sb;
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
