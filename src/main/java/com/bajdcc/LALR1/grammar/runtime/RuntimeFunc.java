package com.bajdcc.LALR1.grammar.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 【运行时】调用函数基本单位
 *
 * @author bajdcc
 */
public class RuntimeFunc {

	/**
	 * 调用地址
	 */
	private int address = -1;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 当前的代码页名
	 */
	private int currentPc = 0;

	/**
	 * 当前的代码页名
	 */
	private String currentPage = "extern";

	/**
	 * 保存的返回指令地址
	 */
	private int retPc = 0;

	/**
	 * 保存的返回代码页名
	 */
	private String retPage = "";

	/**
	 * 保存的异常跳转地址
	 */
	private int tryJmp = -1;

	/**
	 * 参数
	 */
	private List<RuntimeObject> params = new ArrayList<>();

	/**
	 * 临时变量
	 */
	private List<Map<Integer, RuntimeObject>> tmp = new ArrayList<>();

	/**
	 * 函数闭包
	 */
	private Map<Integer, RuntimeObject> closure = new HashMap<>();

	/**
	 * YIELD
	 */
	private int yield = 0;

	public int getYield() {
		return yield;
	}

	public void addYield() {
		this.yield++;
	}

	public void popYield() {
		this.yield--;
	}

	public void resetYield() {
		this.yield = 0;
	}

	public RuntimeFunc() {
		enterScope();
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public String getSimpleName() {
		if (name == null)
			return "extern";
		String[] s = name.split("\\$");
		return s[s.length - 1];
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCurrentPc() {
		return currentPc;
	}

	public void setCurrentPc(int currentPc) {
		this.currentPc = currentPc;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public int getRetPc() {
		return retPc;
	}

	public void setRetPc(int retPc) {
		this.retPc = retPc;
	}

	public String getRetPage() {
		return retPage;
	}

	public void setRetPage(String retPage) {
		this.retPage = retPage;
	}

	public RuntimeObject getParam(int idx) {
		return params.get(idx);
	}

	public List<RuntimeObject> getParams() {
		return params;
	}

	public void addParams(RuntimeObject param) {
		params.add(param);
	}

	public List<Map<Integer, RuntimeObject>> getTmp() {
		return tmp;
	}

	public void addTmp(int idx, RuntimeObject val) {
		tmp.get(0).put(idx, val);
	}

	public void enterScope() {
		tmp.add(0, new HashMap<>());
	}

	public void leaveScope() {
		tmp.remove(0);
	}

	public Map<Integer, RuntimeObject> getClosure() {
		return closure;
	}

	public void addClosure(int idx, RuntimeObject val) {
		closure.put(idx, val);
	}

	public int getTryJmp() {
		return tryJmp;
	}

	public void setTryJmp(int tryJmp) {
		this.tryJmp = tryJmp;
	}

	@Override
	public String toString() {
		return System.lineSeparator() +
				String.format("代码页：%s，地址：%d，名称：%s，参数：%s，变量：%s，闭包：%s",
						currentPage, currentPc, name == null ? "extern" : name, params, tmp, closure);
	}

	public RuntimeFunc copy(RuntimeStack stack) {
		RuntimeFunc func = new RuntimeFunc();
		func.address = address;
		func.name = name;
		func.currentPc = currentPc;
		func.currentPage = currentPage;
		func.retPc = retPc;
		func.retPage = retPage;
		func.tryJmp = tryJmp;
		func.params = new ArrayList<>(params);
		func.tmp = tmp.stream().map(a -> a.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, c -> c.getValue().clone())))
				.collect(Collectors.toList());
		func.closure = closure.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, a -> a.getValue().clone()));
		func.yield = yield;
		return func;
	}
}
