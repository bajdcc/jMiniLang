package priv.bajdcc.LALR1.grammar.runtime;

import java.util.List;

/**
 * 【中间代码】二元操作符
 *
 * @author bajdcc
 */
public class RuntimeInstBinary extends RuntimeInstUnary {

	public int op2;

	public RuntimeInstBinary(RuntimeInst inst, int op1, int op2) {
		super(inst, op1);
		this.op2 = op2;
	}

	@Override
	public int getAdvanceLength() {
		return 3;
	}

	@Override
	public void gen(List<Integer> insts) {
		super.gen(insts);
		insts.add(op2);
	}

	@Override
	public String toString(String delim) {
		return super.toString(delim) + delim + op2;
	}

	@Override
	public String toString() {
		return toString(" ");
	}
}
