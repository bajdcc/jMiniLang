package priv.bajdcc.LALR1.grammar.runtime;

import java.util.ArrayList;
import java.util.Stack;

/**
 * 【运行时】运行时堆栈
 *
 * @author bajdcc
 */
public class RuntimeStack {

	private static final int MAX_DATASTACKSIZE = 100;
	private static final int MAX_CALLSTACKSIZE = 20;
	private static final int MAX_ARGSIZE = 12;

	/**
	 * 数据堆栈
	 */
	private Stack<RuntimeObject> stkData = new Stack<RuntimeObject>();

	/**
	 * 调用堆栈，临时变量堆栈
	 */
	private ArrayList<RuntimeFunc> stkCall = new ArrayList<RuntimeFunc>();

	public RuntimeStack() {

	}

	public void pushData(RuntimeObject obj) {
		stkData.push(obj);
		if (stkData.size() > MAX_DATASTACKSIZE) {
			throw new StackOverflowError("数据堆栈溢出");
		}
	}

	public RuntimeObject popData() {
		return stkData.pop();
	}
	
	public RuntimeObject top() {
		return stkData.peek();
	}

	public boolean isEmptyStack() {
		return stkData.isEmpty();
	}

	public RuntimeObject findVariable(int idx) {
		for (RuntimeFunc func : stkCall) {
			RuntimeObject obj = func.getTmp().get(idx);
			if (obj != null) {
				return obj;
			}
		}
		return null;
	}

	public void storeVariableDirect(int idx, RuntimeObject obj) {
		stkCall.get(0).getTmp().put(idx, obj);
	}

	public boolean pushFuncData() {
		stkCall.add(0, new RuntimeFunc());
		return stkCall.size() < MAX_CALLSTACKSIZE;
	}

	public boolean pushFuncArgs(RuntimeObject obj) {
		stkCall.get(0).addParams(obj);
		return stkCall.get(0).getParams().size() < MAX_ARGSIZE;
	}

	public void opReturn(RuntimeRegister reg) {
		reg.execId = stkCall.get(0).getRetPc();
		reg.pageId = stkCall.get(0).getRetPage();
		stkCall.remove(0);
	}

	public int getFuncArgsCount() {
		return stkCall.get(0).getParams().size();
	}

	public RuntimeObject loadFuncArgs(int idx) {
		return stkCall.get(0).getParam(idx);
	}

	public void opCall(int jmpPc, String jmpPage, int retPc, String retPage,
			String funcName) {
		stkCall.get(0).setCurrentPc(jmpPc);
		stkCall.get(0).setCurrentPage(jmpPage);
		stkCall.get(0).setRetPc(retPc);
		stkCall.get(0).setRetPage(retPage);
		stkCall.get(0).setName(funcName);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("=========================");
		sb.append(System.getProperty("line.separator"));
		sb.append("Data: " + stkData);
		sb.append(System.getProperty("line.separator"));
		sb.append("Call: " + stkCall);
		sb.append(System.getProperty("line.separator"));
		return sb.toString();
	}
}
