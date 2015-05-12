package priv.bajdcc.LALR1.grammar.runtime;

import java.util.List;

/**
 * 【中间代码】零元操作数指令
 *
 * @author bajdcc
 */
public class RuntimeInstNon extends RuntimeInstBase {

	public RuntimeInstNon(RuntimeInst inst) {
		this.inst = inst;
	}

	@Override
	public int getAdvanceLength() {
		return 1;
	}

	@Override
	public void gen(List<Integer> insts) {
		insts.add(inst.ordinal());
	}
}
