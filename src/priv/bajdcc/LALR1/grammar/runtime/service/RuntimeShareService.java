package priv.bajdcc.LALR1.grammar.runtime.service;

import priv.bajdcc.LALR1.grammar.runtime.RuntimeObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 【运行时】运行时共享服务
 *
 * @author bajdcc
 */
public class RuntimeShareService implements IRuntimeShareService {

	class ShareStruct {
		public String name;
		public RuntimeObject obj;
		public int reference;
		public boolean locked;

		public ShareStruct(String name, RuntimeObject obj) {
			this.name = name;
			this.obj = obj;
			this.reference = 1;
			this.locked = false;
		}
	}

	private static final int MAX_SHARING = 1000;
	private Map<String, ShareStruct> mapShares;

	public RuntimeShareService() {
		this.mapShares = new HashMap<>();
	}

	@Override
	public int startSharing(String name, RuntimeObject obj) {
		if (mapShares.size() >= MAX_SHARING)
			return -1;
		if (mapShares.containsKey(name))
			return 0;
		mapShares.put(name, new ShareStruct(name, obj));
		System.out.println("Sharing '" + name + "' created");
		return 1;
	}

	@Override
	public int createSharing(String name, RuntimeObject obj) {
		if (mapShares.size() >= MAX_SHARING)
			return -1;
		mapShares.put(name, new ShareStruct(name, obj));
		System.out.println("Sharing '" + name + "' created");
		return 1;
	}

	@Override
	public RuntimeObject getSharing(String name, boolean reference) {
		if (mapShares.containsKey(name)) {
			ShareStruct ss = mapShares.get(name);
			if (reference)
				ss.reference++;
			return ss.obj;
		}
		return new RuntimeObject(null);
	}

	@Override
	public int stopSharing(String name) {
		if (!mapShares.containsKey(name))
			return -1;
		ShareStruct ss = mapShares.get(name);
		ss.reference--;
		if (ss.reference == 0) {
			mapShares.remove(name);
			return 1;
		}
		if (ss.reference < 0) {
			return 2;
		}
		return 0;
	}

	@Override
	public boolean isLocked(String name) {
		return mapShares.containsKey(name) && mapShares.get(name).locked;
	}

	@Override
	public void setLocked(String name, boolean lock) {
		if (mapShares.containsKey(name))
			mapShares.get(name).locked = lock;
	}

	@Override
	public long size() {
		return mapShares.size();
	}
}
