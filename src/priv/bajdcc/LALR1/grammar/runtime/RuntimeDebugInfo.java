package priv.bajdcc.LALR1.grammar.runtime;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 【扩展】调试与开发
 *
 * @author bajdcc
 */
public class RuntimeDebugInfo implements IRuntimeDebugInfo, Serializable {

	private static final long serialVersionUID = 1L;
	private HashMap<String, Object> dataMap = new HashMap<String, Object>();
	private HashMap<String, Integer> exports = new HashMap<String, Integer>();
	private HashMap<Integer, String> func = new HashMap<Integer, String>();
	private HashMap<String, IRuntimeDebugValue> externalValue = new HashMap<String, IRuntimeDebugValue>();
	private HashMap<String, IRuntimeDebugExec> externalExec = new HashMap<String, IRuntimeDebugExec>();

	public void addExports(String name, Integer addr) {
		exports.put(name, addr);
	}

	public void addFunc(String name, int addr) {
		func.put(addr, name);
	}

	@Override
	public String getFuncNameByAddress(int addr) {
		return func.get(addr);
	}

	@Override
	public int getAddressOfExportFunc(String name) {
		if (exports.containsKey(name)) {
			return exports.get(name);
		} else {
			return -1;
		}
	}

	@Override
	public IRuntimeDebugValue getValueCallByName(String name) {
		return externalValue.get(name);
	}

	@Override
	public IRuntimeDebugExec getExecCallByName(String name) {
		return externalExec.get(name);
	}

	@Override
	public boolean addExternalValue(String name, IRuntimeDebugValue func) {
		return externalValue.put(name, func) != null;
	}

	@Override
	public boolean addExternalFunc(String name, IRuntimeDebugExec func) {
		return externalExec.put(name, func) != null;
	}

	@Override
	public HashMap<String, Object> getDataMap() {
		return dataMap;
	}
}
