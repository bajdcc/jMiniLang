package com.bajdcc.LALR1.grammar.tree;

import com.bajdcc.LALR1.grammar.codegen.ICodegen;
import com.bajdcc.LALR1.grammar.error.SemanticException;
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import com.bajdcc.LALR1.grammar.runtime.RuntimeInstUnary;
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import com.bajdcc.LALR1.grammar.type.TokenTools;
import com.bajdcc.util.lexer.token.OperatorType;
import com.bajdcc.util.lexer.token.Token;
import com.bajdcc.util.lexer.token.TokenType;

/**
 * 【语义分析】二元表达式
 *
 * @author bajdcc
 */
public class ExpBinop implements IExp {

	/**
	 * 操作符
	 */
	private Token token = null;

	/**
	 * 左操作数
	 */
	private IExp leftOperand = null;

	/**
	 * 右操作数
	 */
	private IExp rightOperand = null;

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public IExp getLeftOperand() {
		return leftOperand;
	}

	public void setLeftOperand(IExp leftOperand) {
		this.leftOperand = leftOperand;
	}

	public IExp getRightOperand() {
		return rightOperand;
	}

	public void setRightOperand(IExp rightOperand) {
		this.rightOperand = rightOperand;
	}

	@Override
	public boolean isConstant() {
		return leftOperand.isConstant() && rightOperand.isConstant();
	}

	@Override
	public boolean isEnumerable() {
		return leftOperand.isEnumerable() ^ rightOperand.isEnumerable();
	}

	@Override
	public IExp simplify(ISemanticRecorder recorder) {
		if (!isConstant()) {
			return this;
		}
		if (leftOperand instanceof ExpValue && rightOperand instanceof ExpValue) {
			if (token.kToken == TokenType.OPERATOR) {
				OperatorType op = (OperatorType) token.object;
				if (op == OperatorType.COLON)
					return this;
				if (TokenTools.binop(recorder, this)) {
					return leftOperand;
				}
			}
		}
		return this;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {
		if (token.kToken == TokenType.OPERATOR) {
			OperatorType op = (OperatorType) token.object;
			if (TokenTools.isAssignment(op)) {
				if (!(leftOperand instanceof ExpValue)) {
					recorder.add(SemanticException.SemanticError.INVALID_ASSIGNMENT, token);
				}
			}
		}
		leftOperand.analysis(recorder);
		rightOperand.analysis(recorder);
	}

	@Override
	public void genCode(ICodegen codegen) {
		if (token.kToken == TokenType.OPERATOR && token.object == OperatorType.DOT) {
			codegen.genCode(RuntimeInst.iopena);
			leftOperand.genCode(codegen);
			codegen.genCode(RuntimeInst.ipusha);
			rightOperand.genCode(codegen);
			codegen.genCode(RuntimeInst.ipusha);
			codegen.genCode(RuntimeInst.ipush, codegen.genDataRef("g_get_property"));
			codegen.genCode(RuntimeInst.icallx);
			return;
		}
		if (token.kToken == TokenType.OPERATOR) {
			OperatorType op = (OperatorType) token.object;
			if (TokenTools.isAssignment(op)) {
				RuntimeInst ins = TokenTools.op2ins(token);
				ExpValue left = (ExpValue) leftOperand;
				if (ins == RuntimeInst.ice) {
					rightOperand.genCode(codegen);
					codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(left.getToken().object));
					codegen.genCode(RuntimeInst.istore);
					return;
				}
				leftOperand.genCode(codegen);
				rightOperand.genCode(codegen);
				codegen.genCode(ins);
				codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(left.getToken().object));
				codegen.genCode(RuntimeInst.istore);
				return;
			} else if (op == OperatorType.COLON) {
				rightOperand.genCode(codegen);
				leftOperand.genCode(codegen);
				return;
			}
		}
		RuntimeInst inst = TokenTools.op2ins(token);
		leftOperand.genCode(codegen);
		RuntimeInstUnary jmp = null;
		switch (inst) {
			case iandl:
				jmp = codegen.genCode(RuntimeInst.ijfx, -1);
				break;
			case iorl:
				jmp = codegen.genCode(RuntimeInst.ijtx, -1);
				break;
			default:
				break;
		}
		rightOperand.genCode(codegen);
		codegen.genCode(inst);
		if (jmp != null) {
			jmp.op1 = codegen.getCodeIndex();
		}
	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		if (token.kToken == TokenType.OPERATOR && token.object == OperatorType.COLON) {
			return leftOperand.print(prefix) + " " + token.toRealString() + " "
					+ rightOperand.print(prefix);
		}
		return "( " + leftOperand.print(prefix) + " " + token.toRealString() + " "
				+ rightOperand.print(prefix) + " )";
	}

	@Override
	public void addClosure(IClosureScope scope) {
		leftOperand.addClosure(scope);
		rightOperand.addClosure(scope);
	}

	@Override
	public void setYield() {
		leftOperand.setYield();
		rightOperand.setYield();
	}
}
