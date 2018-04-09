package priv.bajdcc.LALR1.syntax.automata.nga;

import priv.bajdcc.LALR1.semantic.token.ISemanticAction;
import priv.bajdcc.LALR1.syntax.exp.RuleExp;
import priv.bajdcc.LALR1.syntax.exp.TokenExp;
import priv.bajdcc.LALR1.syntax.handler.IErrorHandler;

/**
 * 非确定性文法自动机边数据
 *
 * @author bajdcc
 */
public class NGAEdgeData {
	/**
	 * 边类型
	 */
	public NGAEdgeType kAction = NGAEdgeType.EPSILON;

	/**
	 * 终结符数据
	 */
	public TokenExp token = null;

	/**
	 * 非终结符数据
	 */
	public RuleExp rule = null;

	/**
	 * 存储序号（-1为无效）
	 */
	public int iStorage = -1;

	/**
	 * 错误处理器
	 */
	public IErrorHandler handler = null;

	/**
	 * 语义动作
	 */
	public ISemanticAction action = null;
}
