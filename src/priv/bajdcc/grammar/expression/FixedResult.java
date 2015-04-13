package priv.bajdcc.grammar.expression;

/**
 * 【语义分析】混合返回类型
 *
 * @author bajdcc
 */
public class FixedResult {

	/**
	 * 表达式
	 */
	public Expression expression = null;

	/**
	 * 语句
	 */
	public Sentence sentence = null;

	public FixedResult(Expression exp) {
		expression = exp;
	}

	public FixedResult(Sentence sen) {
		sentence = sen;
	}
}
