package priv.bajdcc.util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 哈希表
 * 
 * @author bajdcc
 */
public class HashListMapEx2<K, V> {

	public HashMap<K, V> map = new HashMap<>();

	public ArrayList<K> list = new ArrayList<>();

	public boolean contains(K k) {
		return map.containsKey(k);
	}

	public void add(K k, V v) {
		V vv = map.put(k, v);
		if (vv == null) {
			list.add(k);
		}
	}

	public boolean put(K k, V v) {
		V vv = map.put(k, v);
		if (vv == null) {
			list.add(k);
			return false;
		} else {
			return true;
		}
	}

	public V get(K k) {
		return map.get(k);
	}

	public int indexOf(K k) {
		return list.indexOf(k);
	}
	
	public void pop() {
		int last = list.size() - 1;
		map.remove(list.get(last));
		list.remove(last);
	}
}