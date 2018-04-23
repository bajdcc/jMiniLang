package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.error.SemanticException.SemanticError;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import priv.bajdcc.util.lexer.token.KeywordType;
import priv.bajdcc.util.lexer.token.OperatorType;
import priv.bajdcc.util.lexer.token.Token;

import java.util.ArrayList;

/**
 * 【语义分析】类方法调用表达式
 *
 * @author bajdcc
 */
public class ExpInvokeProperty implements IExp {

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
	 * 参数
	 */
	private ArrayList<IExp> params = new ArrayList<>();

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
		if (params.size() > 9) {
			recorder.add(SemanticError.TOO_MANY_ARGS, token);
		}
		for (IExp exp : params) {
			exp.analysis(recorder);
		}
	}

	@Override
	public void genCode(ICodegen codegen) {
		codegen.genCode(RuntimeInst.iopena);
		obj.genCode(codegen);
		codegen.genCode(RuntimeInst.ipusha);
		property.genCode(codegen);
		codegen.genCode(RuntimeInst.ipusha);
		if (params.isEmpty()) {
			codegen.genCode(RuntimeInst.ipush, codegen.genDataRef("g_invoke_method"));
			codegen.genCode(RuntimeInst.icallx);
		} else {
			for (IExp exp : params) {
				exp.genCode(codegen);
				codegen.genCode(RuntimeInst.ipusha);
			}
			codegen.genCode(RuntimeInst.ipush, codegen.genDataRef("g_invoke_method_" + params.size()));
			codegen.genCode(RuntimeInst.icallx);
		}
	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		StringBuilder sb = new StringBuilder();
		sb.append(obj.print(prefix)).append(".").append(property.print(prefix));
		if (!params.isEmpty()) {
			sb.append(OperatorType.LPARAN.getName()).append(" ");
			if (params.size() == 1) {
				sb.append(params.get(0).print(prefix));
			} else {
				for (int i = 0; i < params.size(); i++) {
					sb.append(params.get(i).print(prefix));
					if (i != params.size() - 1) {
						sb.append(OperatorType.COMMA.getName()).append(" ");
					}
				}
			}
			sb.append(" ").append(OperatorType.RPARAN.getName());
		}
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
