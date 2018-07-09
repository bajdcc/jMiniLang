package com.bajdcc.LALR1.grammar.runtime.service;

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import com.bajdcc.LALR1.grammar.runtime.RuntimeObjectType;
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray;
import com.vladsch.flexmark.util.collection.BitIntegerSet;
import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.util.*;

/**
 * 【运行时】运行时用户服务
 *
 * @author bajdcc
 */
public class RuntimeUserService implements IRuntimeUserService {

	private static final int MAX_USER = 1000;
	private static Logger logger = Logger.getLogger("user");
	private RuntimeService service;

	RuntimeUserService(RuntimeService service) {
		this.service = service;
		this.arrUsers = new UserStruct[MAX_USER];
		this.setUserId = new HashSet<>();
		this.mapNames = new HashMap<>();
	}

	enum UserType {
		PIPE("管道"),
		SHARE("共享"),
		FILE("文件");

		String name;

		UserType(String name) {
			this.name = name;
		}
	}

	interface IUserHandler {
		void destroy();

		boolean isEmpty();

		void enqueue(int pid);

		void dequeue();

		void dequeue(int pid);

		RuntimeObject read();

		void write(RuntimeObject obj);
	}

	abstract class UserHandler implements IUserHandler {

		protected int id;
		private Deque<Integer> waiting_pids;

		UserHandler(int id) {
			this.id = id;
			this.waiting_pids = new ArrayDeque<>();
		}

		@Override
		public void destroy() {

		}

		@Override
		public boolean isEmpty() {
			return true;
		}

		@Override
		public void enqueue(int pid) {
			waiting_pids.add(pid);
			service.getProcessService().block(pid);
			service.getProcessService().getRing3().setBlockHandle(id);
		}

		@Override
		public void dequeue() {
			if (waiting_pids.isEmpty())
				return;
			int pid = waiting_pids.poll();
			service.getProcessService().getRing3().setBlockHandle(-1);
			service.getProcessService().wakeup(pid);
		}

		@Override
		public void dequeue(int pid) {
			waiting_pids.remove(pid);
		}

		@Override
		public String toString() {
			return String.format("队列：%s", waiting_pids.toString());
		}
	}

	class UserPipeHandler extends UserHandler {
		private Queue<RuntimeObject> queue;

		UserPipeHandler(int id) {
			super(id);
			this.queue = new ArrayDeque<>();
			service.getProcessService().getRing3().addHandle(id);
		}

		@Override
		public void destroy() {
			service.getProcessService().getRing3().removeHandle(id);
		}

		@Override
		public boolean isEmpty() {
			return queue.isEmpty();
		}

		@Override
		public RuntimeObject read() {
			return queue.poll();
		}

		@Override
		public void write(RuntimeObject obj) {
			queue.add(obj);
		}

		@Override
		public String toString() {
			return String.format("%s 管道：%s", super.toString(), queue.toString());
		}
	}

	class UserShareHandler extends UserHandler {

		UserShareHandler(int id) {
			super(id);
		}

		@Override
		public RuntimeObject read() {
			return null;
		}

		@Override
		public void write(RuntimeObject obj) {

		}
	}

	class UserFileHandler extends UserHandler {

		UserFileHandler(int id) {
			super(id);
		}

		@Override
		public RuntimeObject read() {
			return null;
		}

		@Override
		public void write(RuntimeObject obj) {

		}
	}

	class UserStruct {
		public String name;
		public String page;
		public UserType type;
		public IUserHandler handler;

		UserStruct(String name, String page, UserType type, IUserHandler handler) {
			this.name = name;
			this.page = page;
			this.type = type;
			this.handler = handler;
		}

		public String getName() {
			return name;
		}
	}

	private Map<String, Integer> mapNames;
	private UserStruct[] arrUsers;
	private Set<Integer> setUserId;
	private int cyclePtr = 0;

	private int newId() {
		while (true) {
			if (arrUsers[cyclePtr] == null) {
				int id = cyclePtr;
				setUserId.add(id);
				cyclePtr++;
				if (cyclePtr >= MAX_USER) {
					cyclePtr -= MAX_USER;
				}
				return id;
			}
			cyclePtr++;
			if (cyclePtr >= MAX_USER) {
				cyclePtr -= MAX_USER;
			}
		}
	}

	@Override
	public int create(String name, String page) {
		if (setUserId.size() >= MAX_USER) {
			return -1;
		}
		String[] sp = name.split("\\|");
		if (sp.length != 2)
			return -2;
		if (sp[0].equals("pipe")) {
			return createPipe(sp[1], page);
		}
		if (sp[0].equals("share")) {
			return createShare(sp[1], page);
		}
		if (sp[0].equals("file")) {
			return createFile(sp[1], page);
		}
		return -3;
	}

	@Override
	public RuntimeObject read(int id) {
		if (arrUsers[id] == null) {
			return new RuntimeObject(true, RuntimeObjectType.kNoop);
		}
		UserStruct user = arrUsers[id];
		if (user.handler.isEmpty()) {
			int pid = service.getProcessService().getPid();
			arrUsers[id].handler.enqueue(pid);
			return new RuntimeObject(false, RuntimeObjectType.kNoop);
		}
		return user.handler.read();
	}

	@Override
	public boolean write(int id, RuntimeObject obj) {
		if (arrUsers[id] == null) {
			return false;
		}
		UserStruct user = arrUsers[id];
		user.handler.write(obj);
		user.handler.dequeue();
		return true;
	}

	@Override
	public void destroy() {
		List<Integer> handles = new ArrayList<>(service.getProcessService().getRing3().getHandles());
		for (int handle : handles) {
			destroy(handle);
		}
		int blockHandle = service.getProcessService().getRing3().getBlockHandle();
		if (blockHandle != -1) {
			if (setUserId.contains(blockHandle)) {
				UserStruct us = arrUsers[blockHandle];
				assert (us.type == UserType.PIPE);
				us.handler.dequeue(service.getProcessService().getPid());
			}
		}
	}

	@Override
	public boolean destroy(int handle) {
		if (!mapNames.containsKey(arrUsers[handle].name))
			return false;
		setUserId.remove(handle);
		mapNames.remove(arrUsers[handle].name);
		arrUsers[handle].handler.destroy();
		arrUsers[handle] = null;
		return true;
	}

	@Override
	public RuntimeArray stat(boolean api) {
		RuntimeArray array = new RuntimeArray();
		if (api) {
			mapNames.values().stream().sorted(Comparator.naturalOrder())
					.forEach((value) -> {
						RuntimeArray item = new RuntimeArray();
						item.add(new RuntimeObject(BigInteger.valueOf(value)));
						item.add(new RuntimeObject(arrUsers[value].type.name));
						item.add(new RuntimeObject(arrUsers[value].name));
						item.add(new RuntimeObject(arrUsers[value].page));
						item.add(new RuntimeObject(arrUsers[value].handler.toString()));
						array.add(new RuntimeObject(item));
					});
		} else {
			array.add(new RuntimeObject(String.format("   %-5s   %-15s   %-15s   %-20s",
					"Id", "Name", "Type", "Description")));
			mapNames.values().stream().sorted(Comparator.naturalOrder())
					.forEach((value) -> array.add(
							new RuntimeObject(String.format("   %-5s   %-15s   %-15s   %-20s",
									BigInteger.valueOf(value), arrUsers[value].name,
									arrUsers[value].type.toString(), arrUsers[value].handler.toString()))));
		}
		return array;
	}

	private int createPipe(String name, String page) {
		if (mapNames.containsKey(name)) {
			return mapNames.getOrDefault(name, -11);
		}
		int id = newId();
		mapNames.put(name, id);
		arrUsers[id] = new UserStruct(name, page, UserType.PIPE, new UserPipeHandler(id));
		return id;
	}

	private int createShare(String name, String page) {
		if (mapNames.containsKey(name)) {
			return mapNames.getOrDefault(name, -21);
		}
		int id = newId();
		mapNames.put(name, id);
		arrUsers[id] = new UserStruct(name, page, UserType.SHARE, new UserShareHandler(id));
		service.getProcessService().getRing3().addHandle(id);
		return id;
	}

	private int createFile(String name, String page) {
		if (mapNames.containsKey(name)) {
			return mapNames.getOrDefault(name, -31);
		}
		int id = newId();
		mapNames.put(name, id);
		arrUsers[id] = new UserStruct(name, page, UserType.FILE, new UserFileHandler(id));
		service.getProcessService().getRing3().addHandle(id);
		return id;
	}
}
