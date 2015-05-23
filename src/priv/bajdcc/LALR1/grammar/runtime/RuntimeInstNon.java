package priv.bajdcc.LALR1.grammar.runtime;

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
	public void gen(ICodegenByteWriter writer) {
		writer.genInst(inst);
	}
}
