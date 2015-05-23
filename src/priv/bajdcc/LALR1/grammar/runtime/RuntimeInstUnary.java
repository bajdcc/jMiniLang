package priv.bajdcc.LALR1.grammar.runtime;

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
		return 4 + super.getAdvanceLength();
	}

	@Override
	public void gen(ICodegenByteWriter writer) {
		super.gen(writer);
		writer.genOp(op1);
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
