package priv.bajdcc.LALR1.grammar.runtime;

/**
 * 运行时堆栈接口
 *
 * @author bajdcc
 */
public interface IRuntimeStack {

	RuntimeObject load() throws RuntimeException;
	void store(RuntimeObject obj);
	void push() throws RuntimeException;
	void pop() throws RuntimeException;
	void opLoad() throws RuntimeException;
	void opLoadFunc() throws RuntimeException;
	void opStore() throws RuntimeException;
	void opStoreDirect() throws RuntimeException;
	void opOpenFunc() throws RuntimeException;
	void opLoadArgs() throws RuntimeException;
	void opPushArgs() throws RuntimeException;
	void opReturn() throws RuntimeException;
	void opCall() throws RuntimeException;
	void opPushNull();
	void opPushZero();
	void opPushNan();
	void opLoadVar() throws RuntimeException;
	void opJump() throws RuntimeException;
	void opJumpBool(boolean bool) throws RuntimeException;
	void opJumpBoolRetain(boolean bool) throws RuntimeException;
	void opJumpZero(boolean bool) throws RuntimeException;
	void opJumpYield() throws RuntimeException;
	void opJumpNan() throws RuntimeException;
	void opImport() throws RuntimeException;
	void opLoadExtern() throws RuntimeException;
	void opCallExtern(boolean invoke) throws Exception;
	void opYield(boolean input) throws RuntimeException;
	void opYieldSwitch(boolean forward) throws RuntimeException;
	void opYieldCreateContext() throws Exception;
	void opYieldDestroyContext() throws RuntimeException;
	void opScope(boolean enter) throws RuntimeException;
	void opArr();
	void opMap();
}