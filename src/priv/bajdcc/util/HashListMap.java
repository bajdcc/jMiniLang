package priv.bajdcc.util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 哈希表
 * <p>
 * T->id,T=list,id=list indexof T
 * </p>
 * 
 * @author bajdcc
 */
public class HashListMap<T> {

	public HashMap<T, Integer> map = new HashMap<T, Integer>();

	public ArrayList<T> list = new ArrayList<T>();

	public boolean contains(T t) {
		return map.containsKey(t);
	}

	public void add(T t) {
		if (map.put(t, list.size()) == null) {
			list.add(t);
		}
	}

	public int put(T t) {
		if (map.containsKey(t)) {
			return map.get(t);
		}
		add(t);
		return list.size() - 1;
	}

	public int indexOf(T t) {
		return map.get(t);
	}
}