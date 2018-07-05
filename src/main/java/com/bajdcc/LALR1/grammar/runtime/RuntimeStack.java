package com.bajdcc.LALR1.grammar.runtime;

import java.util.*;

/**
 * 【运行时】运行时堆栈
 *
 * @author bajdcc
 */
public class RuntimeStack {

	private static final int MAX_DATASTACKSIZE = 100;
	private static final int MAX_CALLSTACKSIZE = 100;
	private static final int MAX_ARGSIZE = 16;

	public RuntimeStack prev = null;
	public int level = 0;

	public RuntimeRegister reg = new RuntimeRegister();
	private Stack<Integer> dataTryCounts = new Stack<>();
	private boolean catchState = false;

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

	public void pushData(RuntimeObject obj) throws RuntimeException {
		if (obj == null) {
			throw new NullPointerException("obj");
		}
		stkData.push(obj);
		if (stkData.size() > MAX_DATASTACKSIZE) {
			throw new RuntimeException(RuntimeException.RuntimeError.THROWS_EXCEPTION, 0, "堆栈溢出");
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

	public RuntimeObject findVariable(String codePage, int idx) {
		for (RuntimeFunc func : stkCall) {
			if (func.getCurrentPage().equals(codePage)) {
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
		}
		return new RuntimeObject(null);
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

	public int getFuncArgsCount1() {
		return stkCall.get(1).getParams().size();
	}

	public RuntimeObject getFuncArgs(int index) {
		RuntimeFunc func = stkCall.stream().skip(1).filter(a -> a.getName() != null).findFirst().orElse(null);
		if (func == null)
			return null;
		if (index >= 0 && index < func.getParams().size())
			return func.getParams().get(index);
		return null;
	}

	public int getFuncLevel() {
		return stkCall.size();
	}

	public String getFuncName() {
		return stkCall.get(0).getName();
	}

	public String getFuncSimpleName() {
		return stkCall.get(0).getSimpleName();
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
		stkCall.get(0).setName(funcName == null ? "unknown" : funcName);
	}

	public boolean hasCatch() {
		return catchState;
	}

	public void setTry(int jmp) {
		if (jmp != -1)
			dataTryCounts.push(stkData.size());
		stkCall.get(0).setTryJmp(jmp);
		catchState = false;
	}

	public boolean hasNoTry() {
		return dataTryCounts.empty();
	}

	public int getTry() {
		return stkCall.get(0).getTryJmp();
	}

	public void resetTry() {
		int last = dataTryCounts.pop();
		RuntimeObject obj = stkData.pop();
		while (stkData.size() > last)
			stkData.pop();
		stkData.push(obj);
		catchState = true;
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
