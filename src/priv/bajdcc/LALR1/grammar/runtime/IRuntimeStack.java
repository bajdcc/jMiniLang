package priv.bajdcc.LALR1.grammar.runtime;

/**
 * 运行时堆栈接口
 *
 * @author bajdcc
 */
public interface IRuntimeStack {

	public RuntimeObject load() throws RuntimeException;
	public void store(RuntimeObject obj);
	public void push() throws RuntimeException;
	public void pop() throws RuntimeException;
	public void opLoad() throws RuntimeException;
	public void opLoadFunc() throws RuntimeException;
	public void opStore() throws RuntimeException;
	public void opStoreDirect() throws RuntimeException;
	public void opAssign() throws RuntimeException;
	public void opOpenFunc();
	public void opLoadArgs() throws RuntimeException;
	public void opPushArgs() throws RuntimeException;
	public void opReturn() throws RuntimeException;
	public void opCall() throws RuntimeException;
	public void opPushNull();
	public void opPushZero();
	public void opLoadVar() throws RuntimeException;
	public void opJmp() throws RuntimeException;
	public void opImport() throws RuntimeException;
	public void opLoadExtern() throws RuntimeException;
	public void opCallExtern() throws RuntimeException;
}