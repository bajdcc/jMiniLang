package priv.bajdcc.LALR1.grammar.runtime;

/**
 * 【中间代码】指令结构基类
 *
 * @author bajdcc
 */
public abstract class RuntimeInstBase {

	public RuntimeInst inst = RuntimeInst.inop;

	public abstract int getAdvanceLength();

	public String toString(String delim) {
		return inst.toString();
	}
	
	@Override
	public String toString() {
		return toString(" ");
	}
	
	public abstract void gen(ICodegenByteWriter writer);
}
