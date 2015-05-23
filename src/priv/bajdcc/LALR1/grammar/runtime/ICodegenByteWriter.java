package priv.bajdcc.LALR1.grammar.runtime;

/**
 * 【中间代码生成】字节码生成
 *
 * @author bajdcc
 */
public interface ICodegenByteWriter {

	public void genInst(RuntimeInst inst);
	public void genOp(int op);
}
