package priv.bajdcc.semantic.tracker;

import priv.bajdcc.syntax.automata.npa.NPAInstruction;

/**
 * 下推自动机指令结构
 *
 * @author bajdcc
 */
public class Instruction {

	/**
	 * 指令
	 */
	public NPAInstruction m_Inst = NPAInstruction.PASS;

	/**
	 * 参数
	 */
	public int m_iIndex = -1;

	/**
	 * 处理器
	 */
	public int m_iHandler = -1;

	public Instruction() {

	}

	public Instruction(NPAInstruction inst, int index, int handler) {
		m_Inst = inst;
		m_iIndex = index;
		m_iHandler = handler;
	}

	@Override
	public String toString() {
		return String.format("指令：%s \t参数：%d 处理：%d", m_Inst.getName(), m_iIndex,
				m_iHandler);
	}
}
