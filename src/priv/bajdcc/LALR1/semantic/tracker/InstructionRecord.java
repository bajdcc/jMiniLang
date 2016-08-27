package priv.bajdcc.LALR1.semantic.tracker;

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
	public ArrayList<Instruction> arrInsts = new ArrayList<>();
	
	/**
	 * 前向指针
	 */
	public InstructionRecord prev = null;
	
	public InstructionRecord(InstructionRecord prev) {
		this.prev = prev;	
	}
}
