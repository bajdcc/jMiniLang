package priv.bajdcc.LALR1.grammar.runtime;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 【虚拟机】运行时进程
 *
 * @author bajdcc
 */
public class RuntimeProcess {

	private class SchdParticle {
		public int processId;
		public int cycle;
		public int priority;

		public SchdParticle(int processId, int cycle, int priority) {
			this.processId = processId;
			this.cycle = cycle;
			this.priority = priority;
		}
	}

	private class SchdProcess {
		public RuntimeMachine machine;
		public int priority;

		public SchdProcess(RuntimeMachine machine) {
			this.machine = machine;
			this.priority = 0;
		}

		public SchdProcess(RuntimeMachine machine, int priority) {
			this.machine = machine;
			this.priority = priority;
		}
	}

	private static final int MAX_PROCESS = 100;
	private static final int PER_CYCLE = 10;
	private int cyclePtr = 0;
	private String name;
	private RuntimeCodePage codePage;
	private SchdProcess arrProcess[];
	private Set<Integer> setProcessId;
	private Queue<SchdParticle> queue;
	private Set<Integer> destroyedProcess;

	public RuntimeProcess(String name, InputStream input) throws Exception {
		runMainProcess(name, input);
	}

	public int getPriority(int pid) {
		return arrProcess[pid].priority;
	}

	public void run() throws Exception {
		while (schd() && step());
	}

	private void runMainProcess(String name, InputStream input) throws Exception {
		initMainProcess(name, input);
	}

	private boolean schd() {
		if (setProcessId.isEmpty())
			return false;
		queue.addAll(setProcessId.stream().map(id ->
				new SchdParticle(id, PER_CYCLE, arrProcess[id].priority)).collect(Collectors.toList()));
		return true;
	}

	private boolean step() throws Exception {
		while (!queue.isEmpty()) {
			SchdParticle part = queue.poll();
			CYCLE_FOR:
			for (int i = 0; i < part.cycle; i++) {
				switch (arrProcess[part.processId].machine.runStep()) {
					case 1:
						return false;
					case 2:
						break CYCLE_FOR;
				}
			}
		}
		return true;
	}

	private void initMainProcess(String name, InputStream input) throws Exception {
		this.arrProcess = new SchdProcess[MAX_PROCESS];
		this.name = name;
		this.codePage = RuntimeCodePage.importFromStream(input);
		this.setProcessId = new HashSet<>();
		this.queue = new PriorityQueue<>((a, b) -> a.priority - b.priority);
		this.destroyedProcess = new HashSet<>();
		RuntimeMachine machine = new RuntimeMachine(cyclePtr, this);
		machine.initStep(name, codePage, Collections.emptyList(), 0);
		setProcessId.add(cyclePtr);
		arrProcess[cyclePtr++] = new SchdProcess(machine);
	}

	public void createProcess(int creatorId, String page, int pc) throws Exception {
		if (setProcessId.size() >= MAX_PROCESS) {
			arrProcess[creatorId].machine.err(
					RuntimeException.RuntimeError.PROCESS_OVERFLOW, String.valueOf(page));
		}
		for (;;) {
			if (arrProcess[cyclePtr] == null && !destroyedProcess.contains(cyclePtr)) {
				RuntimeMachine machine = new RuntimeMachine(cyclePtr, this);
				machine.initStep(name, codePage, arrProcess[creatorId].machine.getPageRefers(page), pc);
				setProcessId.add(cyclePtr);
				arrProcess[cyclePtr++] = new SchdProcess(machine);
				if (cyclePtr >= MAX_PROCESS) {
					cyclePtr -= MAX_PROCESS;
				}
				break;
			}
			cyclePtr++;
			if (cyclePtr >= MAX_PROCESS) {
				cyclePtr -= MAX_PROCESS;
			}
		}
	}

	public void destroyProcess(int processId) throws Exception {
		arrProcess[processId] = null;
		destroyedProcess.add(processId);
		setProcessId.remove(processId);
	}
}
