package com.bajdcc.LALR1.grammar.runtime;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 【运行时】运行时堆栈
 *
 * @author bajdcc
 */
public class RuntimeStack {

	private static final int MAX_DATASTACKSIZE = 100;
	private static final int MAX_CALLSTACKSIZE = 100;
	private static final int MAX_ARGSIZE = 16;

	private int parent = -1;
	private int level = 0;

	RuntimeRegister reg = new RuntimeRegister();
	private List<Integer> dataTryCounts = new ArrayList<>();
	private boolean catchState = false;

	/**
	 * 数据堆栈
	 */
	private Stack<RuntimeObject> stkData = new Stack<>();

	/**
	 * 调用堆栈，临时变量堆栈
	 */
	private List<RuntimeFunc> stkCall = new ArrayList<>();

	public RuntimeStack() {

	}

	public RuntimeStack(int level) {
		this.level = level;
	}

	private RuntimeFunc getCall() {
		return stkCall.get(stkCall.size() - 1);
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isYield() {
		return getCall().getYield() > 0;
	}

	public void addYield(int id) {
		getCall().addYield();
	}

	void popYield() {
		getCall().popYield();
	}

	public int getYield() {
		return getCall().getYield();
	}

	void resetYield() {
		getCall().resetYield();
	}

	void pushData(RuntimeObject obj) throws RuntimeException {
		if (obj == null) {
			throw new NullPointerException("obj");
		}
		stkData.push(obj);
		if (stkData.size() > MAX_DATASTACKSIZE) {
			throw new RuntimeException(RuntimeException.RuntimeError.THROWS_EXCEPTION, 0, "堆栈溢出");
		}
	}

	RuntimeObject popData() {
		return stkData.pop();
	}

	public RuntimeObject top() {
		return stkData.peek();
	}

	boolean isEmptyStack() {
		return stkData.isEmpty();
	}

	RuntimeObject findVariable(String codePage, int idx) {
		for (int i = stkCall.size() - 1; i >= 0; i--) {
			RuntimeFunc func = stkCall.get(i);
			if (func.getCurrentPage().equals(codePage)) {
				List<Map<Integer, RuntimeObject>> tmp = func.getTmp();
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

	void storeVariableDirect(int idx, RuntimeObject obj) {
		getCall().addTmp(idx, obj);
	}

	void enterScope() {
		getCall().enterScope();
	}

	void leaveScope() {
		getCall().leaveScope();
	}

	void storeClosure(int idx, RuntimeObject obj) {
		getCall().addClosure(idx, obj);
	}

	boolean pushFuncData() {
		stkCall.add(new RuntimeFunc());
		return stkCall.size() < MAX_CALLSTACKSIZE;
	}

	boolean pushFuncArgs(RuntimeObject obj) {
		getCall().addParams(obj);
		return getCall().getParams().size() < MAX_ARGSIZE;
	}

	void opReturn(RuntimeRegister reg) {
		reg.execId = getCall().getRetPc();
		reg.pageId = getCall().getRetPage();
		stkCall.remove(stkCall.size() - 1);
	}

	int getFuncArgsCount() {
		return getCall().getParams().size();
	}

	int getFuncArgsCount1() {
		return stkCall.get(stkCall.size() - 2).getParams().size();
	}

	RuntimeObject getFuncArgs(int index) {
		RuntimeFunc func = null;
		for (int i = stkCall.size() - 2; i >= 0; i--) {
			if (stkCall.get(i).getName() != null) {
				func = stkCall.get(i);
				break;
			}
		}
		if (func == null)
			return null;
		if (index >= 0 && index < func.getParams().size())
			return func.getParams().get(index);
		return null;
	}

	int getFuncLevel() {
		return stkCall.size();
	}

	public String getFuncName() {
		return getCall().getName();
	}

	String getFuncSimpleName() {
		return getCall().getSimpleName();
	}

	RuntimeObject loadFuncArgs(int idx) {
		return getCall().getParam(idx);
	}

	void opCall(int jmpPc, String jmpPage, int retPc, String retPage,
	            String funcName) {
		getCall().setCurrentPc(jmpPc);
		getCall().setCurrentPage(jmpPage);
		getCall().setRetPc(retPc);
		getCall().setRetPage(retPage);
		getCall().setName(funcName == null ? "unknown" : funcName);
	}

	boolean hasCatch() {
		return catchState;
	}

	boolean hasNoTry() {
		return dataTryCounts.isEmpty();
	}

	public int getTry() {
		return getCall().getTryJmp();
	}

	public void setTry(int jmp) {
		if (jmp != -1)
			dataTryCounts.add(stkData.size());
		getCall().setTryJmp(jmp);
		catchState = false;
	}

	void resetTry() {
		int last = dataTryCounts.get(dataTryCounts.size() - 1);
		dataTryCounts.remove(dataTryCounts.size() - 1);
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

	public RuntimeStack copy() {
		RuntimeStack stack = new RuntimeStack();
		stack.reg = reg.copy();
		stack.catchState = catchState;
		stack.stkData = new Stack<>();
		Arrays.stream(stkData.toArray(new RuntimeObject[0])).map(RuntimeObject::clone).forEach(a -> stack.stkData.push(a));
		stack.dataTryCounts = new ArrayList<>(dataTryCounts);
		stack.stkCall = stkCall.stream().map(a -> a.copy(stack)).collect(Collectors.toList());
		stack.parent = parent;
		stack.level = level;
		return stack;
	}
}
