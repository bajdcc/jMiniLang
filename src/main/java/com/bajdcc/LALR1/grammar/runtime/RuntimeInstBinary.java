package com.bajdcc.LALR1.grammar.runtime;

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
		return 4 + super.getAdvanceLength();
	}

	@Override
	public void gen(ICodegenByteWriter writer) {
		super.gen(writer);
		writer.genOp(op2);
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
