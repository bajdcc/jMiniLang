package priv.bajdcc.LALR1.grammar.tree;

import java.util.ArrayList;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.error.SemanticException.SemanticError;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.util.lexer.token.KeywordType;
import priv.bajdcc.util.lexer.token.Token;

/**
 * 【语义分析】函数调用表达式
 *
 * @author bajdcc
 */
public class ExpInvoke implements IExp {

	/**
	 * 调用名
	 */
	private Token name = null;

	/**
	 * 调用函数
	 */
	private Function func = null;

	/**
	 * 外部函数名
	 */
	private Token extern = null;

	/**
	 * 参数
	 */
	private ArrayList<IExp> params = new ArrayList<IExp>();

	public Token getName() {
		return name;
	}

	public void setName(Token name) {
		this.name = name;
	}

	public Function getFunc() {
		return func;
	}

	public void setFunc(Function func) {
		this.func = func;
	}

	public Token getExtern() {
		return extern;
	}

	public void setExtern(Token extern) {
		this.extern = extern;
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
		if (func != null) {
			checkArgsCount(recorder);
			if (func.getRealName().startsWith("~")) {
				func.analysis(recorder);
			}
		}
	}

	/**
	 * 参数个数检查
	 * 
	 * @param recorder
	 */
	private void checkArgsCount(ISemanticRecorder recorder) {
		int invokeArgsCount = params.size();
		int funcArgsCount = func.getParams().size();
		if (invokeArgsCount != funcArgsCount) {
			recorder.add(SemanticError.MISMATCH_ARGS, name);
		}
	}

	@Override
	public void genCode(ICodegen codegen) {
		codegen.genCode(RuntimeInst.iopena);
		for (IExp exp : params) {
			exp.genCode(codegen);
			codegen.genCode(RuntimeInst.ipusha);
		}
		if (func != null) {
			codegen.genCode(RuntimeInst.ipush, codegen.getFuncIndex(func));
			codegen.genCode(RuntimeInst.icall);
		} else {
			codegen.genCode(RuntimeInst.ipush,
					codegen.genDataRef(extern.object));
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
		sb.append(KeywordType.CALL.getName() + " ");
		if (func != null) {
			if (!func.getRealName().startsWith("~")) {
				sb.append(func.getName().toRealString());
			} else {
				sb.append(func.print(prefix));
			}
		} else {
			sb.append(KeywordType.EXTERN.getName() + " "
					+ extern.toRealString());
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
