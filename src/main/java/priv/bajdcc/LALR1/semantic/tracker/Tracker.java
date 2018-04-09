package priv.bajdcc.LALR1.semantic.tracker;

import priv.bajdcc.LALR1.syntax.automata.npa.NPAStatus;
import priv.bajdcc.util.lexer.regex.IRegexStringIterator;

import java.util.Stack;

/**
 * 跟踪器（链表）
 *
 * @author bajdcc
 */
public class Tracker {
	/**
	 * 指令记录集
	 */
	public InstructionRecord rcdInst = null;

	/**
	 * 错误记录集
	 */
	public ErrorRecord rcdError = null;

	/**
	 * 当前PDA状态
	 */
	public NPAStatus npaStatus = null;
	/**
	 * PDA状态堆栈
	 */
	public Stack<NPAStatus> stkStatus = new Stack<>();

	/**
	 * 单词遍历接口
	 */
	public IRegexStringIterator iter = null;

	/**
	 * 是否产生了错误
	 */
	public boolean bRaiseError = false;

	/**
	 * 当前步骤是否产生了错误
	 */
	public boolean bInStepError = false;

	/**
	 * 是否已经中止
	 */
	public boolean bFinished = false;

	/**
	 * 前向指针
	 */
	public Tracker prev = null;

	/**
	 * 后向指针
	 */
	public Tracker next = null;
}
