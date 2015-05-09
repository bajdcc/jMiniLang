package priv.bajdcc.LALR1.grammar.tree;

import java.util.ArrayList;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.util.lexer.token.KeywordType;

/**
 * 【语义分析】函数调用表达式
 *
 * @author bajdcc
 */
public class ExpInvoke implements IExp {

	/**
	 * 调用函数
	 */
	private Function func = null;

	/**
	 * 参数
	 */
	private ArrayList<IExp> params = new ArrayList<IExp>();

	public Function getFunc() {
		return func;
	}

	public void setFunc(Function func) {
		this.func = func;
	}

	public ArrayList<IExp> getParams() {
		return params;
	}

	public void setParams(ArrayList<IExp> params) {
		this.params = params;
	}

	@Override
	public boolean isConstant() {
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

	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		StringBuilder sb = new StringBuilder();
		sb.append(KeywordType.CALL.getName() + " ");
		if (!func.getRealName().startsWith("~")) {
			sb.append(func.getName().toRealString());
		} else {
			sb.append(func.print(prefix));
		}
		sb.append("(");
		for (IExp param : params) {
			sb.append(param.print(prefix));
			sb.append(",");
		}
		if (!params.isEmpty()) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(")");
		return sb.toString();
	}
}
