package com.bajdcc.LALR1.grammar.runtime.service;

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * 【运行时】运行时管道服务
 *
 * @author bajdcc
 */
public class RuntimePipeService implements IRuntimePipeService {

	public RuntimePipeService(RuntimeService service) {
		this.service = service;
		this.arrPipes = new PipeStruct[MAX_PIPE];
		this.setPipeId = new HashSet<>();
		this.mapPipeNames = new HashMap<>();
	}

	class PipeStruct {
		public String name;
		public String page;
		public Queue<Character> queue;
		public Queue<Integer> waiting_pids;

		public PipeStruct(String name, String page) {
			this.name = name;
			this.page = page;
			this.queue = new ArrayDeque<>();
			this.waiting_pids = new ArrayDeque<>();
		}

		public String getName() {
			return name;
		}
	}

	private static Logger logger = Logger.getLogger("pipe");
	private static final int OFFSET_PIPE = 10000;
	private static final int MAX_PIPE = 1000;
	private RuntimeService service;
	private PipeStruct arrPipes[];
	private Set<Integer> setPipeId;
	private Map<String, Integer> mapPipeNames;
	private int cyclePtr = 0;

	private int encodeHandle(int handle) {
		return handle + OFFSET_PIPE;
	}

	private int decodeHandle(int handle) {
		return handle - OFFSET_PIPE;
	}

	@Override
	public int create(String name, String page) {
		if (setPipeId.size() >= MAX_PIPE) {
			return -1;
		}
		if (mapPipeNames.containsKey(name)) {
			return encodeHandle(mapPipeNames.get(name));
		}
		int handle;
		for (; ; ) {
			if (arrPipes[cyclePtr] == null) {
				handle = cyclePtr;
				setPipeId.add(cyclePtr);
				mapPipeNames.put(name, cyclePtr);
				arrPipes[cyclePtr++] = new PipeStruct(name, page);
				if (cyclePtr >= MAX_PIPE) {
					cyclePtr -= MAX_PIPE;
				}
				break;
			}
			cyclePtr++;
			if (cyclePtr >= MAX_PIPE) {
				cyclePtr -= MAX_PIPE;
			}
		}
		logger.debug("Pipe #" + handle + " '" + name + "' created");
		return encodeHandle(handle);
	}

	@Override
	public boolean destroy(int handle) {
		handle = decodeHandle(handle);
		if (!setPipeId.contains(handle)) {
			return false;
		}
		for (int pid : arrPipes[handle].waiting_pids) {
			service.getProcessService().wakeup(pid);
		}
		logger.debug("Pipe #" + handle + " '" + arrPipes[handle].name + "' destroyed");
		mapPipeNames.remove(arrPipes[handle].name);
		arrPipes[handle] = null;
		setPipeId.remove(handle);
		return true;
	}

	@Override
	public boolean destroyByName(int pid, String name) {
		if (!mapPipeNames.containsKey(name)) {
			return false;
		}
		return destroy(encodeHandle(mapPipeNames.get(name)));
	}

	@Override
	public char read(int pid, int handle) {
		handle = decodeHandle(handle);
		if (!setPipeId.contains(handle)) {
			return '\uffff';
		}
		PipeStruct ps = arrPipes[handle];
		if (ps.queue.isEmpty()) { // 阻塞进程
			service.getProcessService().block(pid);
			arrPipes[handle].waiting_pids.add(pid); // 放入等待队列
			return '\ufffe';
		}
		return ps.queue.poll();
	}

	@Override
	public char readNoBlock(int pid, int handle) {
		handle = decodeHandle(handle);
		if (!setPipeId.contains(handle)) {
			return '\uffff';
		}
		PipeStruct ps = arrPipes[handle];
		if (ps.queue.isEmpty()) { // 阻塞进程
			return '\ufffe';
		}
		return ps.queue.poll();
	}

	@Override
	public boolean write(int handle, char ch) {
		handle = decodeHandle(handle);
		if (setPipeId.contains(handle)) {
			if (!arrPipes[handle].waiting_pids.isEmpty()) {
				Integer pid = arrPipes[handle].waiting_pids.poll();
				if (pid != null)
					service.getProcessService().wakeup(pid);
			}
			return arrPipes[handle].queue.add(ch);
		}
		return false;
	}

	@Override
	public boolean writeString(String name, String data) {
		if (!mapPipeNames.containsKey(name)) {
			return false;
		}
		int handle = mapPipeNames.get(name);
		if (!arrPipes[handle].waiting_pids.isEmpty()) {
			Integer pid = arrPipes[handle].waiting_pids.poll();
			if (pid != null)
				service.getProcessService().wakeup(pid);
		}
		for (int i = 0; i < data.length(); i++) {
			arrPipes[handle].queue.add(data.charAt(i));
		}
		return true;
	}

	@Override
	public void writeStringNew(String name, String data) {
		if (!mapPipeNames.containsKey(name)) {
			create(name, "/system/pipe");
		}
		int handle = mapPipeNames.get(name);
		if (!arrPipes[handle].waiting_pids.isEmpty()) {
			Integer pid = arrPipes[handle].waiting_pids.poll();
			if (pid != null)
				service.getProcessService().wakeup(pid);
		}
		for (int i = 0; i < data.length(); i++) {
			arrPipes[handle].queue.add(data.charAt(i));
		}
	}

	@Override
	public String readAndDestroy(String name) {
		if (!mapPipeNames.containsKey(name)) {
			return null;
		}
		int handle = mapPipeNames.get(name);
		PipeStruct ps = arrPipes[handle];
		if (ps.queue.isEmpty()) {
			destroy(encodeHandle(handle));
			return "";
		}
		StringBuilder sb = new StringBuilder();
		while (!ps.queue.isEmpty()) {
			sb.append(ps.queue.poll());
		}
		destroy(encodeHandle(handle));
		return sb.toString();
	}

	@Override
	public String readAll(String name) {
		if (!mapPipeNames.containsKey(name)) {
			return null;
		}
		int handle = mapPipeNames.get(name);
		PipeStruct ps = arrPipes[handle];
		if (ps.queue.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		while (!ps.queue.isEmpty()) {
			sb.append(ps.queue.poll());
		}
		return sb.toString();
	}

	@Override
	public boolean hasData(int handle) {
		handle = decodeHandle(handle);
		return setPipeId.contains(handle) && !arrPipes[handle].queue.isEmpty();
	}

	@Override
	public boolean query(String name) {
		return mapPipeNames.containsKey(name);
	}

	@Override
	public long size() {
		return setPipeId.size();
	}

	@Override
	public RuntimeArray stat(boolean api) {
		RuntimeArray array = new RuntimeArray();
		if (api) {
			mapPipeNames.values().stream().sorted(Comparator.naturalOrder())
					.forEach((value) -> {
						RuntimeArray item = new RuntimeArray();
						item.add(new RuntimeObject(String.valueOf(value)));
						item.add(new RuntimeObject(arrPipes[value].name));
						item.add(new RuntimeObject(arrPipes[value].page));
						item.add(new RuntimeObject(arrPipes[value].queue.size()));
						item.add(new RuntimeObject(arrPipes[value].waiting_pids.size()));
						array.add(new RuntimeObject(item));
					});
		} else {
			array.add(new RuntimeObject(String.format("   %-5s   %-20s   %-15s   %-15s",
					"Id", "Name", "Queue", "Waiting")));
			mapPipeNames.values().stream().sorted(Comparator.naturalOrder())
					.forEach((value) -> array.add(new RuntimeObject(String.format("   %-5s   %-20s   %-15d   %-15d",
							String.valueOf(value), arrPipes[value].name, arrPipes[value].queue.size(), arrPipes[value].waiting_pids.size()))));
		}
		return array;
	}
}
