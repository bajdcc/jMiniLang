package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.type.TokenTools;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

/**
 * 【语义分析】一元表达式
 *
 * @author bajdcc
 */
public class ExpSinop implements IExp {

	/**
	 * 操作符
	 */
	private Token token = null;

	/**
	 * 操作数
	 */
	private IExp operand = null;

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public IExp getOperand() {
		return operand;
	}

	public void setOperand(IExp operand) {
		this.operand = operand;
	}

	@Override
	public boolean isConstant() {
		return operand.isConstant();
	}

	@Override
	public IExp simplify(ISemanticRecorder recorder) {
		if (!isConstant()) {
			return this;
		}
		if (operand instanceof ExpValue) {
			if (token.kToken == TokenType.OPERATOR) {
				if (TokenTools.sinop(recorder, this)) {
					return operand;
				}
			}
		}
		return this;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {

	}

	@Override
	public void genCode(ICodegen codegen) {

	}

	@Override
	public String toString() {
		return "( " + token.toRealString() + " " + operand.toString() + " )";
	}

	@Override
	public String print(StringBuilder prefix) {
		return toString();
	}
}
