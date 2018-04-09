package priv.bajdcc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 哈希表
 * <p>
 * T-&gt;id,T=list,id=list indexof T
 * </p>
 *
 * @author bajdcc
 */
public class HashListMap<T> {

	public HashMap<T, Integer> map = new HashMap<>();

	public List<T> list = new ArrayList<>();

	public boolean contains(T t) {
		return map.containsKey(t);
	}

	public void add(T t) {
		if (!map.containsKey(t)) {
			map.put(t, list.size());
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