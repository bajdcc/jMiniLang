package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import priv.bajdcc.util.lexer.token.KeywordType;
import priv.bajdcc.util.lexer.token.OperatorType;
import priv.bajdcc.util.lexer.token.Token;

/**
 * 【语义分析】类属性赋值表达式
 *
 * @author bajdcc
 */
public class ExpAssignProperty implements IExp {

	/**
	 * 操作符
	 */
	private Token token = null;

	/**
	 * 对象
	 */
	private IExp obj = null;

	/**
	 * 属性
	 */
	private IExp property = null;

	/**
	 * 表达式
	 */
	private IExp exp = null;

	public void setToken(Token token) {
		this.token = token;
	}

	public IExp getObj() {
		return obj;
	}

	public void setObj(IExp obj) {
		this.obj = obj;
	}

	public IExp getProperty() {
		return property;
	}

	public void setProperty(IExp property) {
		this.property = property;
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
		obj.analysis(recorder);
		property.analysis(recorder);
		if (exp != null)
			exp.analysis(recorder);
	}

	@Override
	public void genCode(ICodegen codegen) {
		if (token == null || token.object == OperatorType.EQ_ASSIGN) {
			codegen.genCode(RuntimeInst.iopena);
			obj.genCode(codegen);
			codegen.genCode(RuntimeInst.ipusha);
			property.genCode(codegen);
			codegen.genCode(RuntimeInst.ipusha);
			exp.genCode(codegen);
			codegen.genCode(RuntimeInst.ipusha);
			codegen.genCode(RuntimeInst.ipush, codegen.genDataRef("g_set_property_unary"));
			codegen.genCode(RuntimeInst.icallx);
		} else if (token.object == OperatorType.PLUS_PLUS || token.object == OperatorType.MINUS_MINUS) {
			codegen.genCode(RuntimeInst.iopena);
			obj.genCode(codegen);
			codegen.genCode(RuntimeInst.ipusha);
			property.genCode(codegen);
			codegen.genCode(RuntimeInst.ipusha);
			codegen.genCode(RuntimeInst.iopena);
			obj.genCode(codegen);
			codegen.genCode(RuntimeInst.ipusha);
			property.genCode(codegen);
			codegen.genCode(RuntimeInst.ipusha);
			codegen.genCode(RuntimeInst.ipush, codegen.genDataRef("g_get_property"));
			codegen.genCode(RuntimeInst.icallx);
			if (token.object == OperatorType.PLUS_PLUS) {
				codegen.genCode(RuntimeInst.iinc);
			} else {
				codegen.genCode(RuntimeInst.idec);
			}
			codegen.genCode(RuntimeInst.ipusha);
			codegen.genCode(RuntimeInst.ipush, codegen.genDataRef("g_set_property_unary"));
			codegen.genCode(RuntimeInst.icallx);
		} else {
			codegen.genCode(RuntimeInst.iopena);
			obj.genCode(codegen);
			codegen.genCode(RuntimeInst.ipusha);
			property.genCode(codegen);
			codegen.genCode(RuntimeInst.ipusha);
			codegen.genCode(RuntimeInst.iopena);
			obj.genCode(codegen);
			codegen.genCode(RuntimeInst.ipusha);
			property.genCode(codegen);
			codegen.genCode(RuntimeInst.ipusha);
			codegen.genCode(RuntimeInst.ipush, codegen.genDataRef("g_get_property"));
			codegen.genCode(RuntimeInst.icallx);
			exp.genCode(codegen);
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
			codegen.genCode(RuntimeInst.ipusha);
			codegen.genCode(RuntimeInst.ipush, codegen.genDataRef("g_set_property_unary"));
			codegen.genCode(RuntimeInst.icallx);
		}
	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		if (token != null && (token.object == OperatorType.PLUS_PLUS || token.object == OperatorType.MINUS_MINUS)) {
			return obj.print(prefix) + "." + property.print(prefix) +
					" " + token.toRealString();
		}
		return obj.print(prefix) + "." + property.print(prefix) +
				" " + ((token == null) ? OperatorType.ASSIGN.getName() : token.toRealString()) + " " +
				exp.print(prefix);
	}

	@Override
	public void addClosure(IClosureScope scope) {
		if (exp != null)
			exp.addClosure(scope);
	}

	@Override
	public void setYield() {

	}
}