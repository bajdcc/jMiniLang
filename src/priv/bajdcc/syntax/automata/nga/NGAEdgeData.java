package priv.bajdcc.syntax.automata.nga;

import priv.bajdcc.syntax.error.IErrorHandler;
import priv.bajdcc.syntax.exp.RuleExp;
import priv.bajdcc.syntax.exp.TokenExp;

/**
 * 非确定性文法自动机边数据
 * 
 * @author bajdcc
 *
 */
public class NGAEdgeData {
	/**
	 * 边类型
	 */
	public NGAEdgeType m_Action = NGAEdgeType.EPSILON;

	/**
	 * 终结符数据
	 */
	public TokenExp m_Token = null;
	
	/**
	 * 非终结符数据
	 */
	public RuleExp m_Rule = null;
	
	/**
	 * 存储序号（-1为无效）
	 */
	public int m_iStorage = -1;
	
	/**
	 * 错误处理器
	 */
	public IErrorHandler m_Handler = null;
}
