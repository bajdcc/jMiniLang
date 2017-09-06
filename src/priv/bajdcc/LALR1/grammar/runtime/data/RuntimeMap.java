package priv.bajdcc.LALR1.grammar.runtime.data;

import priv.bajdcc.LALR1.grammar.runtime.RuntimeObject;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * 【运行时】运行时数组
 *
 * @author bajdcc
 */
public class RuntimeMap implements Cloneable {

	private Map<String, RuntimeObject> map;

	public RuntimeMap() {
		map = new HashMap<>();
	}

	public RuntimeMap(RuntimeMap obj) {
		copyFrom(obj);
	}

	public void put(String key, RuntimeObject obj) {
		map.put(key, obj);
	}

	public RuntimeObject get(String key) {
		if (map.containsKey(key)) {
			return map.get(key);
		}
		return null;
	}

	public RuntimeObject size() {
		return new RuntimeObject(BigInteger.valueOf(map.size()));
	}

	public boolean contains(String key) {
		return map.containsKey(key);
	}

	public RuntimeObject remove(String key) {
		if (map.containsKey(key)) {
			return map.remove(key);
		}
		return null;
	}

	public void clear() {
		map.clear();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public void copyFrom(RuntimeMap obj) {
		map = new HashMap<>(obj.map);
	}

	public RuntimeMap clone() {
		try {
			return (RuntimeMap) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return new RuntimeMap();
	}

	@Override
	public String toString() {
		return String.valueOf(map.size());
	}
}
