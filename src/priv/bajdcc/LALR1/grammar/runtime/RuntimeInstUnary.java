package priv.bajdcc.LALR1.grammar.runtime;

import java.util.List;

/**
 * 【中间代码】一元操作符
 *
 * @author bajdcc
 */
public class RuntimeInstUnary extends RuntimeInstNon {

	public int op1;

	public RuntimeInstUnary(RuntimeInst inst, int op1) {
		super(inst);
		this.op1 = op1;
	}

	@Override
	public int getAdvanceLength() {
		return 2;
	}

	@Override
	public void gen(List<Integer> insts) {
		super.gen(insts);
		insts.add(op1);
	}

	@Override
	public String toString(String delim) {
		return super.toString(delim) + delim + op1;
	}

	@Override
	public String toString() {
		return toString(" ");
	}
}
