package priv.bajdcc.LALR1.grammar.runtime;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 【运行时】调用函数基本单位
 *
 * @author bajdcc
 */
public class RuntimeFunc {

	/**
	 * 调用地址
	 */
	private int address = 0;

	/**
	 * 名称
	 */
	private String name = "";

	/**
	 * 当前的代码页名
	 */
	private int currentPc = 0;

	/**
	 * 当前的代码页名
	 */
	private String currentPage = "";

	/**
	 * 保存的返回指令地址
	 */
	private int retPc = 0;

	/**
	 * 保存的返回代码页名
	 */
	private String retPage = "";

	/**
	 * 参数
	 */
	private ArrayList<RuntimeObject> params = new ArrayList<RuntimeObject>();

	/**
	 * 临时变量
	 */
	private HashMap<Integer, RuntimeObject> tmp = new HashMap<Integer, RuntimeObject>();

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public String getName() {
		return name;
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

	public ArrayList<RuntimeObject> getParams() {
		return params;
	}

	public void addParams(RuntimeObject param) {
		params.add(param);
	}

	public HashMap<Integer, RuntimeObject> getTmp() {
		return tmp;
	}

	public void addTmp(int idx, RuntimeObject val) {
		tmp.put(idx, val);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(System.getProperty("line.separator"));
		sb.append(String.format("代码页：%s，地址：%08x，名称：%s，参数：%s，变量：%s",
				currentPage, currentPc, name, params, tmp));
		return sb.toString();
	}
}
