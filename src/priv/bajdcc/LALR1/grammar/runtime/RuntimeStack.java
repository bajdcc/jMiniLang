package priv.bajdcc.LALR1.grammar.runtime;

import java.util.*;

/**
 * 【运行时】运行时堆栈
 *
 * @author bajdcc
 */
public class RuntimeStack {

	private static final int MAX_DATASTACKSIZE = 100;
	private static final int MAX_CALLSTACKSIZE = 20;
	private static final int MAX_ARGSIZE = 12;

	public RuntimeStack prev = null;
	public int level = 0;

	public RuntimeRegister reg = new RuntimeRegister();

	/**
	 * 数据堆栈
	 */
	private Stack<RuntimeObject> stkData = new Stack<>();

	/**
	 * 调用堆栈，临时变量堆栈
	 */
	private ArrayList<RuntimeFunc> stkCall = new ArrayList<>();

	public RuntimeStack() {

	}

	public RuntimeStack(RuntimeStack prev) {
		this.prev = prev;
		this.level = prev.level + 1;
	}

	public void pushData(RuntimeObject obj) {
		if (obj == null) {
			throw new NullPointerException("obj");
		}
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
			List<HashMap<Integer, RuntimeObject>> tmp = func.getTmp();
			RuntimeObject obj;
			for (Map<Integer, RuntimeObject> scope : tmp) {
				obj = scope.get(idx);
				if (obj != null) {
					return obj;
				}
			}
			obj = func.getClosure().get(idx);
			if (obj != null) {
				return obj;
			}
		}
		return null;
	}

	public void storeVariableDirect(int idx, RuntimeObject obj) {
		stkCall.get(0).addTmp(idx, obj);
	}

	public void enterScope() {
		stkCall.get(0).enterScope();
	}

	public void leaveScope() {
		stkCall.get(0).leaveScope();
	}

	public void storeClosure(int idx, RuntimeObject obj) {
		stkCall.get(0).addClosure(idx, obj);
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

	public int getFuncLevel() {
		return stkCall.size();
	}

	public String getFuncName() {
		return stkCall.get(0).getName();
	}

	public RuntimeStack getYieldStack(String hash) {
		return stkCall.get(0).getYields(hash);
	}

	public void addYieldStack(String hash,
			RuntimeStack stack) {
		stkCall.get(0).addYieldStack(hash, stack);
	}

	public void popYieldStack() {
		stkCall.get(0).popYieldStack();
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
		return "=========================" +
				System.lineSeparator() +
				"数据栈: " + stkData +
				System.lineSeparator() +
				"调用栈: " + stkCall +
				System.lineSeparator();
	}
}
