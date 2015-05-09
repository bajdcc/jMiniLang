package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.util.lexer.token.OperatorType;
import priv.bajdcc.util.lexer.token.Token;

/**
 * 【语义分析】赋值语句
 *
 * @author bajdcc
 */
public class StmtAssign implements IStmt {

	/**
	 * 变量名
	 */
	private Token name = null;

	/**
	 * 表达式
	 */
	private IExp exp = null;

	/**
	 * 限定符
	 */
	private Token spec = null;

	/**
	 * 是否为声明
	 */
	private boolean decleared = false;

	public Token getName() {
		return name;
	}

	public void setName(Token name) {
		this.name = name;
	}

	public IExp getExp() {
		return exp;
	}

	public void setExp(IExp exp) {
		this.exp = exp;
	}

	public Token getSpec() {
		return spec;
	}

	public void setSpec(Token spec) {
		this.spec = spec;
	}

	public boolean isDecleared() {
		return decleared;
	}

	public void setDecleared(boolean decleared) {
		this.decleared = decleared;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {

	}

	@Override
	public void genCode(ICodegen codegen) {

	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		StringBuilder sb = new StringBuilder();
		sb.append(prefix.toString());
		sb.append(spec.toRealString());
		sb.append(" " + name.toRealString());
		sb.append(" " + OperatorType.ASSIGN.getName() + " ");
		sb.append(exp.print(prefix));
		sb.append(OperatorType.SEMI.getName());
		return sb.toString();
	}
}
