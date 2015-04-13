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
	public NPAInstruction inst = NPAInstruction.PASS;

	/**
	 * 参数
	 */
	public int iIndex = -1;

	/**
	 * 处理器
	 */
	public int iHandler = -1;

	public Instruction() {

	}

	public Instruction(NPAInstruction inst, int index, int handler) {
		this.inst = inst;
		iIndex = index;
		iHandler = handler;
	}

	@Override
	public String toString() {
		return String.format("指令：%5s \t参数：%4d \t处理：%d", inst.getName(), iIndex,
				iHandler);
	}
}
