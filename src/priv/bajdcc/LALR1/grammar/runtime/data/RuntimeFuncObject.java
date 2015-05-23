package priv.bajdcc.LALR1.grammar.runtime.data;

import java.util.HashMap;
import java.util.Map;

import priv.bajdcc.LALR1.grammar.runtime.RuntimeObject;

/**
 * 【运行时】函数调用
 *
 * @author bajdcc
 */
public class RuntimeFuncObject {

	private String page = null;
	private int addr = -1;
	private Map<Integer, RuntimeObject> env = new HashMap<Integer, RuntimeObject>();

	public RuntimeFuncObject(String page, int addr) {
		this.page = page;
		this.addr = addr;
	}

	public String getPage() {
		return page;
	}

	public int getAddr() {
		return addr;
	}

	public Map<Integer, RuntimeObject> getEnv() {
		return env;
	}

	public void addEnv(int id, RuntimeObject obj) {
		env.put(id, obj);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(page);
		sb.append(',');
		sb.append(addr);
		if (!env.isEmpty()) {
			sb.append(',');
			sb.append(env.toString());
		}
		return sb.toString();
	}
}
