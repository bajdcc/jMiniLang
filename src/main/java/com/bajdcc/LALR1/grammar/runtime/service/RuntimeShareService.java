package com.bajdcc.LALR1.grammar.runtime.service;

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray;
import org.apache.log4j.Logger;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * 【运行时】运行时共享服务
 *
 * @author bajdcc
 */
public class RuntimeShareService implements IRuntimeShareService {

	public RuntimeShareService(RuntimeService service) {
		this.service = service;
		this.mapShares = new HashMap<>();
	}

	class ShareStruct {
		public String name;
		public RuntimeObject obj;
		public String page;
		public int reference;
		public boolean locked;

		public ShareStruct(String name, RuntimeObject obj, String page) {
			this.name = name;
			this.obj = obj;
			this.page = page;
			this.reference = 1;
			this.locked = false;
		}

		public String getName() {
			return name;
		}

		public String getObjType() {
			return obj.getType().name();
		}
	}

	private static Logger logger = Logger.getLogger("share");
	private static final int MAX_SHARING = 1000;
	private RuntimeService service;
	private Map<String, ShareStruct> mapShares;

	@Override
	public int startSharing(String name, RuntimeObject obj, String page) {
		if (mapShares.size() >= MAX_SHARING)
			return -1;
		if (mapShares.containsKey(name))
			return 0;
		mapShares.put(name, new ShareStruct(name, obj, page));
		logger.debug("Sharing '" + name + "' created");
		return 1;
	}

	@Override
	public int createSharing(String name, RuntimeObject obj, String page) {
		if (mapShares.size() >= MAX_SHARING)
			return -1;
		mapShares.put(name, new ShareStruct(name, obj, page));
		logger.debug("Sharing '" + name + "' created");
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

	@Override
	public RuntimeArray stat(boolean api) {
		RuntimeArray array = new RuntimeArray();
		if (api) {
			mapShares.values().stream().sorted(Comparator.comparing(ShareStruct::getObjType).thenComparing(ShareStruct::getName))
					.forEach((value) -> {
						RuntimeArray item = new RuntimeArray();
						item.add(new RuntimeObject(value.name));
						item.add(new RuntimeObject(value.obj.getType().getName()));
						item.add(new RuntimeObject(value.page));
						item.add(new RuntimeObject(String.valueOf(value.reference)));
						item.add(new RuntimeObject(value.locked ? "是" : "否"));
						array.add(new RuntimeObject(item));
					});
		} else {
			array.add(new RuntimeObject(String.format("   %-20s   %-15s   %-5s   %-5s",
					"Name", "Type", "Ref", "Locked")));
			mapShares.values().stream().sorted(Comparator.comparing(ShareStruct::getObjType).thenComparing(ShareStruct::getName))
					.forEach((value) -> array.add(new RuntimeObject(String.format("   %-20s   %-15s   %-5s   %-5s",
							value.name, value.obj.getType().name(), String.valueOf(value.reference), String.valueOf(value.locked)))));
		}
		return array;
	}
}
