package com.bajdcc.LALR1.grammar.runtime.service;

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import com.bajdcc.LALR1.grammar.runtime.RuntimeObjectType;
import org.apache.log4j.Logger;

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
		PIPE,
		SHARE,
		FILE
	}

	interface IUserHandler {
		void destroy();
		boolean isEmpty();
		void enqueue(int pid);
		void dequeue();
		RuntimeObject read();
		void write(RuntimeObject obj);
	}

	abstract class UserHandler implements IUserHandler {

		private Queue<Integer> waiting_pids;

		UserHandler() {
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
		}

		@Override
		public void dequeue() {
			if (waiting_pids.isEmpty())
				return;
			int pid = waiting_pids.poll();
			service.getProcessService().wakeup(pid);
		}
	}

	class UserPipeHandler extends UserHandler {
		private int id;
		private Queue<RuntimeObject> queue;

		UserPipeHandler(int id) {
			this.id = id;
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
	}

	class UserShareHandler extends UserHandler {

		@Override
		public RuntimeObject read() {
			return null;
		}

		@Override
		public void write(RuntimeObject obj) {

		}
	}

	class UserFileHandler extends UserHandler {

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
	}

	@Override
	public boolean destroy(int handle) {
		if (!mapNames.containsKey(arrUsers[handle].name))
			return false;
		arrUsers[handle].handler.destroy();
		mapNames.remove(arrUsers[handle].name);
		arrUsers[handle] = null;
		return true;
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
		arrUsers[id] = new UserStruct(name, page, UserType.SHARE, new UserShareHandler());
		service.getProcessService().getRing3().addHandle(id);
		return id;
	}

	private int createFile(String name, String page) {
		if (mapNames.containsKey(name)) {
			return mapNames.getOrDefault(name, -31);
		}
		int id = newId();
		mapNames.put(name, id);
		arrUsers[id] = new UserStruct(name, page, UserType.FILE, new UserFileHandler());
		service.getProcessService().getRing3().addHandle(id);
		return id;
	}
}
