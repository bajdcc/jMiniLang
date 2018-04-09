package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import priv.bajdcc.util.lexer.token.OperatorType;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

/**
 * 【语义分析】间接寻址
 *
 * @author bajdcc
 */
public class ExpIndex implements IExp {

	/**
	 * 单词
	 */
	private Token token = null;

	/**
	 * 单词
	 */
	private IExp exp = null;

	public Token getToken() {
		return token;
	}

	public Token setToken(Token token) {
		return this.token = token;
	}

	public IExp getExp() {
		return exp;
	}

	public void setExp(IExp exp) {
		this.exp = exp;
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
		return this;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {

	}

	@Override
	public void genCode(ICodegen codegen) {
		codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(token.object));
		codegen.genCode(RuntimeInst.iloadv);
		exp.genCode(codegen);
		codegen.genCode(RuntimeInst.iidx);
	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		StringBuilder sb = new StringBuilder();
		sb.append(token.toRealString());
		sb.append(OperatorType.LSQUARE.getName());
		sb.append(exp.print(prefix));
		sb.append(OperatorType.RSQUARE.getName());
		return sb.toString();
	}

	@Override
	public void addClosure(IClosureScope scope) {
		if (token.kToken == TokenType.ID) {
			scope.addRef(token.object);
		}
	}

	@Override
	public void setYield() {

	}
}
