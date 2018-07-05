package com.bajdcc.LALR1.grammar.runtime.data;

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

	public RuntimeArray getKeys() {
		return new RuntimeArray(map.keySet().stream().map(RuntimeObject::new).collect(Collectors.toList()));
	}

	public RuntimeArray getValues() {
		return new RuntimeArray(new ArrayList<>(map.values()));
	}

	/**
	 * 深拷贝
	 *
	 * @param obj 原对象
	 */
	public void copyFrom(RuntimeMap obj) {
		map = new HashMap<>();
		for (Map.Entry<String, RuntimeObject> o : obj.map.entrySet()) {
			map.put(o.getKey(), o.getValue().clone());
		}
	}

	public RuntimeMap clone() {
		try {
			return (RuntimeMap) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return new RuntimeMap();
	}

	public Map<String, RuntimeObject> getMap() {
		return Collections.unmodifiableMap(map);
	}

	@Override
	public String toString() {
		if (map.containsKey("__type__"))
			return "class=" + String.valueOf(map.get("__type__"));
		return String.valueOf(map.size());
	}
}
