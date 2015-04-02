package priv.bajdcc.semantic.tracker;

import java.util.ArrayList;

/**
 * 下推自动机指令记录器链表
 *
 * @author bajdcc
 */
public class InstructionRecord {
	
	/**
	 * 指令集
	 */
	public ArrayList<Instruction> m_arrInsts = new ArrayList<Instruction>();
	
	/**
	 * 前向指针
	 */
	public InstructionRecord m_prevInstRecord = null;
	
	public InstructionRecord(InstructionRecord prev) {
		m_prevInstRecord = prev;	
	}
}
