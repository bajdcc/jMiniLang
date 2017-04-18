package priv.bajdcc.LALR1.grammar.runtime.service;

import java.util.*;

/**
 * 【运行时】运行时管道服务
 *
 * @author bajdcc
 */
public class RuntimePipeService implements IRuntimePipeService {

	class PipeStruct {
		public String name;
		public Queue<Character> queue;

		public PipeStruct(String name) {
			this.name = name;
			this.queue = new ArrayDeque<>();
		}
	}

	private static final int MAX_PIPE = 1000;
	private PipeStruct arrPipes[];
	private Set<Integer> setPipeId;
	private Map<String, Integer> mapPipeNames;
	private int cyclePtr = 0;

	public RuntimePipeService() {
		this.arrPipes = new PipeStruct[MAX_PIPE];
		this.setPipeId = new HashSet<>();
		this.mapPipeNames = new HashMap<>();
	}

	@Override
	public int create(String name) {
		if (setPipeId.size() >= MAX_PIPE) {
			return -1;
		}
		if (mapPipeNames.containsKey(name)) {
			return mapPipeNames.get(name);
		}
		int handle;
		for (;;) {
			if (arrPipes[cyclePtr] == null) {
				handle = cyclePtr;
				setPipeId.add(cyclePtr);
				mapPipeNames.put(name, cyclePtr);
				arrPipes[cyclePtr++] = new PipeStruct(name);
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
		System.out.println("Pipe #" + handle + " '" + name + "' created");
		return handle;
	}

	@Override
	public boolean destroy(int handle) {
		if (!setPipeId.contains(handle)) {
			return false;
		}
		System.out.println("Pipe #" + handle + " '" + arrPipes[handle].name + "' destroyed");
		mapPipeNames.remove(arrPipes[handle].name);
		arrPipes[handle] = null;
		setPipeId.remove(handle);
		return true;
	}

	@Override
	public boolean destroyByName(String name) {
		if (!mapPipeNames.containsKey(name)) {
			return false;
		}
		return destroy(mapPipeNames.get(name));
	}

	@Override
	public char read(int handle) {
		if (!setPipeId.contains(handle)) {
			return '\uffff';
		}
		PipeStruct ps = arrPipes[handle];
		if (ps.queue.isEmpty()) {
			return '\ufffe';
		}
		return ps.queue.poll();
	}

	@Override
	public boolean write(int handle, char ch) {
		return setPipeId.contains(handle) && arrPipes[handle].queue.add(ch);
	}

	@Override
	public boolean isEmpty(int handle) {
		return !setPipeId.contains(handle) || arrPipes[handle].queue.isEmpty();
	}

	@Override
	public boolean query(String name) {
		return mapPipeNames.containsKey(name);
	}

	@Override
	public long size() {
		return setPipeId.size();
	}
}
