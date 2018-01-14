package priv.bajdcc.LALR1.grammar.runtime;

import priv.bajdcc.util.HashListMapEx2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private String name = "extern";

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
	 * 参数
	 */
	private ArrayList<RuntimeObject> params = new ArrayList<>();

	/**
	 * 临时变量
	 */
	private List<HashMap<Integer, RuntimeObject>> tmp = new ArrayList<>();

	/**
	 * 函数闭包
	 */
	private Map<Integer, RuntimeObject> closure = new HashMap<>();

	/**
	 * YIELD
	 */
	private HashListMapEx2<String, RuntimeStack> yields = null;

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
            return "UNKNOWN";
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

	public ArrayList<RuntimeObject> getParams() {
		return params;
	}

	public void addParams(RuntimeObject param) {
		params.add(param);
	}

	public List<HashMap<Integer, RuntimeObject>> getTmp() {
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

	public RuntimeStack getYields(String hash) {
		return yields == null ? null : yields.get(hash);
	}

	public void addYieldStack(String hash, RuntimeStack stack) {
		if (yields == null) {
			yields = new HashListMapEx2<>();
		}
		yields.add(hash, stack);
	}

	public void popYieldStack() {
		yields.pop();
	}

	@Override
	public String toString() {
		return System.lineSeparator() +
				String.format("代码页：%s，地址：%d，名称：%s，参数：%s，变量：%s，闭包：%s",
						currentPage, currentPc, name, params, tmp, closure);
	}
}
