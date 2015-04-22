package priv.bajdcc.LALR1.grammar.expression;

import java.util.ArrayList;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;

/**
 * 【语义分析】基本表达式元组
 *
 * @author bajdcc
 */
public class ExpressionList extends Expression {

	/**
	 * 子表达式
	 */
	private ArrayList<Expression> arrExpressions = new ArrayList<Expression>();

	@Override
	public void analysis(ISemanticRecorder recorder) {
		for (Expression expression : arrExpressions) {
			expression.analysis(recorder);
		}
	}

	@Override
	public void genValueCode(ICodegen codegen) {

	}

	@Override
	public void genRefCode(ICodegen codegen) {

	}
}
