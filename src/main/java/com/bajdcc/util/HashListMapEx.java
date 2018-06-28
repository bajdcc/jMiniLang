package com.bajdcc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 哈希表
 * <p>
 * T-&gt;id,V=list,id= list indexof T
 * </p>
 *
 * @author bajdcc
 */
public class HashListMapEx<K, V> {

	public Map<K, V> map = new HashMap<>();

	public List<V> list = new ArrayList<>();

	public boolean contains(K k) {
		return map.containsKey(k);
	}

	public void add(K k, V v) {
		V vv = map.put(k, v);
		if (vv == null) {
			list.add(v);
		} else {
			list.set(list.indexOf(vv), v);
		}
	}

	public boolean put(K k, V v) {
		V vv = map.put(k, v);
		if (vv == null) {
			list.add(v);
			return false;
		} else {
			list.set(list.indexOf(vv), v);
			return true;
		}
	}

	public V get(K k) {
		return map.get(k);
	}

	public int indexOf(V v) {
		return list.indexOf(v);
	}
}