package priv.bajdcc.LALR1.grammar.runtime;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.service.IRuntimeProcessService;
import priv.bajdcc.LALR1.grammar.runtime.service.RuntimeService;
import priv.bajdcc.LALR1.syntax.handler.SyntaxException;
import priv.bajdcc.util.lexer.error.RegexException;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 【虚拟机】运行时进程
 *
 * @author bajdcc
 */
public class RuntimeProcess implements IRuntimeProcessService {

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
		public int sleep;
		public boolean kernel;

		private SchdProcess(RuntimeMachine machine) {
			this.machine = machine;
			this.priority = 128;
			this.sleep = 0;
			this.kernel = true;
		}

		public SchdProcess(RuntimeMachine machine, boolean kernel) {
			this(machine);
			this.kernel = kernel;
		}
	}

	private static final int SLEEP_TURN = 10;
	private static final int MAX_PROCESS = 1000;
	private static final int PER_CYCLE = 10;
	private static final int USR_PER_CYCLE = 5;
	private int cyclePtr = 0;
	private String name;
	private RuntimeCodePage codePage;
	private Map<String, String> arrCodes;
	private Map<String, RuntimeCodePage> arrPages;
	private SchdProcess arrProcess[];
	private Set<Integer> setProcessId;
	private Queue<SchdParticle> queue;
	private Set<Integer> destroyedProcess;
	private RuntimeService service;
	private List<Integer> arrActiveUsrProcs;

	public RuntimeProcess(String name, InputStream input) throws Exception {
		runMainProcess(name, input);
	}

	public String getName() {
		return name;
	}

	public int getPriority(int pid) {
		return arrProcess[pid].priority;
	}

	public boolean setPriority(int pid, int priority) {
		arrProcess[pid].priority = priority;
		return true;
	}

	public RuntimeService getService() {
		return service;
	}

	public List<Integer> getUsrProcs() {
		return setProcessId.stream().filter(id -> !arrProcess[id].kernel).collect(Collectors.toList());
	}

	public RuntimeCodePage getPage(String name) throws Exception {
		if (arrCodes.containsKey(name)) {
			String code = arrCodes.get(name);
			if (arrPages.containsKey(code)) {
				return arrPages.get(code);
			} else {
				Grammar grammar = new Grammar(code);
				RuntimeCodePage page = grammar.getCodePage();
				arrPages.put(name, page);
				return page;
			}
		}
		return null;
	}

	public void run() throws Exception {
		while (schd() && step());
	}

	private void runMainProcess(String name, InputStream input) throws Exception {
		initMainProcess(name, input);
	}

	public int stepUsrProcess(int pid) {
		arrActiveUsrProcs.add(pid);
		return pid;
	}

	private boolean schd() {
		if (setProcessId.isEmpty())
			return false;
		List<SchdParticle> parts = setProcessId.stream().filter(id -> arrProcess[id].kernel).map(id ->
				new SchdParticle(id, PER_CYCLE, arrProcess[id].priority)).collect(Collectors.toList());
		queue.addAll(parts);
		for (Integer pid : arrActiveUsrProcs) {
			if (setProcessId.contains(pid)) {
				queue.add(new SchdParticle(pid, USR_PER_CYCLE, arrProcess[pid].priority));
			}
		}
		if (!arrActiveUsrProcs.isEmpty())
			arrActiveUsrProcs.clear();
		return true;
	}

	private boolean step() throws Exception {
		int n = setProcessId.size();
		while (!queue.isEmpty()) {
			SchdParticle part = queue.poll();
			CYCLE_FOR:
			for (int i = 0; i < part.cycle; i++) {
				SchdProcess process = arrProcess[part.processId];
				if (process.sleep > 0) {
					process.sleep--;
					n--;
					break;
				}
				switch (arrProcess[part.processId].machine.runStep()) {
					case 1:
						return false;
					case 2:
						n--;
						break CYCLE_FOR;
				}
			}
		}
		if (n == 0) {
			Thread.sleep(SLEEP_TURN);
		}
		return true;
	}

	private void initMainProcess(String name, InputStream input) throws Exception {
		this.arrProcess = new SchdProcess[MAX_PROCESS];
		this.name = name;
		this.codePage = RuntimeCodePage.importFromStream(input);
		this.arrCodes = new HashMap<>();
		this.arrPages = new HashMap<>();
		this.setProcessId = new HashSet<>();
		this.queue = new PriorityQueue<>((a, b) -> a.priority - b.priority);
		this.destroyedProcess = new HashSet<>();
		this.service = new RuntimeService(this);
		this.arrActiveUsrProcs = new ArrayList<>();
		RuntimeMachine machine = new RuntimeMachine(cyclePtr, -1, this);
		machine.initStep(name, codePage, Collections.emptyList(), 0, null);
		setProcessId.add(cyclePtr);
		arrProcess[cyclePtr++] = new SchdProcess(machine);
	}

	/**
	 * 创建进程
	 * @param creatorId 创建者ID
	 * @param kernel 是否为内核进程
	 * @param name 页名
	 * @param page 页
	 * @param pc 起始指令
	 * @param obj 参数
	 * @return 进程ID
	 * @throws Exception 运行时异常
	 */
	public int createProcess(int creatorId, boolean kernel, String name, RuntimeCodePage page, int pc, RuntimeObject obj) throws Exception {
		if (setProcessId.size() >= MAX_PROCESS) {
			arrProcess[creatorId].machine.err(
					RuntimeException.RuntimeError.PROCESS_OVERFLOW, String.valueOf(page));
		}
		if (!arrProcess[creatorId].kernel && kernel) {
			arrProcess[creatorId].machine.err(
					RuntimeException.RuntimeError.ACCESS_FORBIDDEN, String.valueOf(page));
		}
		int pid;
		for (;;) {
			if (arrProcess[cyclePtr] == null && !destroyedProcess.contains(cyclePtr)) {
				RuntimeMachine machine = new RuntimeMachine(cyclePtr, creatorId,this);
				machine.initStep(name, page, arrProcess[creatorId].machine.getPageRefers(name), pc, obj);
				setProcessId.add(cyclePtr);
				pid = cyclePtr;
				arrProcess[cyclePtr++] = new SchdProcess(machine, kernel);
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
		return pid;
	}

	public void destroyProcess(int processId) throws Exception {
		arrProcess[processId] = null;
		destroyedProcess.add(processId);
		setProcessId.remove(processId);
	}

	@Override
	public int sleep(int pid, int turn) {
		if (!setProcessId.contains(pid)) {
			return -1;
		}
		if (turn < 0)
			turn = 0;
		arrProcess[pid].sleep += turn;
		return arrProcess[pid].sleep;
	}

	@Override
	public int join(int joined, int pid, int turn) {
		if (!setProcessId.contains(pid)) {
			return -1;
		}
		if (!setProcessId.contains(joined))
			return 0;
		if (turn < 0)
			turn = 0;
		arrProcess[pid].sleep += turn;
		return arrProcess[pid].sleep;
	}

	@Override
	public boolean addCodePage(String name, String code) {
		if (arrCodes.containsKey(name)) {
			return false;
		}
		arrCodes.put(name, code);
		return true;
	}
}
