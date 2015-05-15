package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.type.TokenTools;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

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
	public IExp simplify(ISemanticRecorder recorder) {
		if (!isConstant()) {
			return this;
		}
		if (leftOperand instanceof ExpValue && rightOperand instanceof ExpValue) {
			if (token.kToken == TokenType.OPERATOR) {
				if (TokenTools.binop(recorder, this)) {
					return leftOperand;
				}
			}
		}
		return this;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {
		leftOperand.analysis(recorder);
		rightOperand.analysis(recorder);
	}

	@Override
	public void genCode(ICodegen codegen) {
		rightOperand.genCode(codegen);
		leftOperand.genCode(codegen);
		codegen.genCode(TokenTools.op2ins(token));
	}

	@Override
	public String toString() {
		return "( " + leftOperand.toString() + " " + token.toRealString() + " "
				+ rightOperand.toString() + " )";
	}

	@Override
	public String print(StringBuilder prefix) {
		return toString();
	}
}
