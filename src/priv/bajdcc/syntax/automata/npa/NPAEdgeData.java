package priv.bajdcc.syntax.automata.npa;

import java.util.ArrayList;

import priv.bajdcc.syntax.error.IErrorHandler;

/**
 * 非确定性下推自动机边数据
 * 
 * @author bajdcc
 *
 */
public class NPAEdgeData {
	/**
	 * 边类型
	 */
	public NPAEdgeType m_Action = NPAEdgeType.MOVE;

	/**
	 * 指令
	 */
	public NPAInstruction m_Inst = NPAInstruction.PASS;
	
	/**
	 * 指令参数
	 */
	public int m_iIndex = -1;
	
	/**
	 * 处理序号
	 */
	public int m_iHandler = -1;
	
	/**
	 * 状态参数
	 */
	public NPAStatus m_Status = null;
	
	/**
	 * 记号参数
	 */
	public int m_iToken = -1;
	
	/**
	 * LookAhead表
	 */
	public ArrayList<Integer> m_arrLookAhead = null;
	
	/**
	 * 错误处理器
	 */
	public IErrorHandler m_Handler = null;
	
	/**
	 * 出错后跳转的状态
	 */
	public NPAStatus m_ErrorJump = null;
}
