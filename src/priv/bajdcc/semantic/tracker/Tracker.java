package priv.bajdcc.semantic.tracker;

import java.util.Stack;

import priv.bajdcc.lexer.regex.IRegexStringIterator;
import priv.bajdcc.syntax.automata.npa.NPAStatus;

/**
 * 跟踪器（链表）
 *
 * @author bajdcc
 */
public class Tracker {
	/**
	 * 指令记录集
	 */
	public InstructionRecord m_rcdInst = null;
	
	/**
	 * 错误记录集
	 */
	public ErrorRecord m_rcdError = null;
	
	/**
	 * 当前PDA状态
	 */
	public NPAStatus m_npaStatus = null;
	/**
	 * PDA状态堆栈
	 */
	public Stack<NPAStatus> m_stkStatus = new Stack<NPAStatus>();
	
	/**
	 * 单词遍历接口
	 */
	public IRegexStringIterator m_iterToken = null;
	
	/**
	 * 是否产生了错误
	 */
	public boolean m_bRaiseError = false;
	
	/**
	 * 当前步骤是否产生了错误
	 */
	public boolean m_bInStepError = false;
	
	/**
	 * 是否已经中止
	 */
	public boolean m_bFinished = false;
	
	/**
	 * 前向指针
	 */
	public Tracker m_prevTracker = null;
	
	/**
	 * 后向指针
	 */
	public Tracker m_nextTracker = null;
}
