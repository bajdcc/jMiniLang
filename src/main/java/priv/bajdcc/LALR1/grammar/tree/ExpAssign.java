package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import priv.bajdcc.util.lexer.token.KeywordType;
import priv.bajdcc.util.lexer.token.OperatorType;
import priv.bajdcc.util.lexer.token.Token;

/**
 * 【语义分析】赋值表达式
 *
 * @author bajdcc
 */
public class ExpAssign implements IExp {

	/**
	 * 变量名
	 */
	private Token name = null;

	/**
	 * 表达式
	 */
	private IExp exp = null;

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

	public boolean isDecleared() {
		return decleared;
	}

	public void setDecleared(boolean decleared) {
		this.decleared = decleared;
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
		exp.analysis(recorder);
	}

	@Override
	public void genCode(ICodegen codegen) {
		exp.genCode(codegen);
		codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(name.object));
		if (decleared) {
			codegen.genCode(RuntimeInst.ialloc);
		} else {
			codegen.genCode(RuntimeInst.istore);
		}
	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		return (decleared ? KeywordType.VARIABLE.getName() : KeywordType.LET
				.getName()) +
				" " + name.toRealString() +
				" " + OperatorType.ASSIGN.getName() + " " +
				exp.print(prefix);
	}

	@Override
	public void addClosure(IClosureScope scope) {
		if (decleared) {
			scope.addDecl(name.object);
		}
		exp.addClosure(scope);
	}

	@Override
	public void setYield() {

	}
}