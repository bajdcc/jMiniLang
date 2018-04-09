package priv.bajdcc.LALR1.grammar.runtime;

import org.apache.log4j.Logger;
import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.service.IRuntimeProcessService;
import priv.bajdcc.LALR1.grammar.runtime.service.RuntimeService;
import priv.bajdcc.LALR1.syntax.handler.SyntaxException;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 【虚拟机】运行时进程
 *
 * @author bajdcc
 */
public class RuntimeProcess implements IRuntimeProcessService {

	private class SchdProcess {
		public RuntimeMachine machine;
		public int priority;
		public int sleep;
		public boolean kernel;
		public boolean runnable;
		public Queue<Integer> waiting_pids;

		private SchdProcess(RuntimeMachine machine) {
			this.machine = machine;
			this.priority = 128;
			this.sleep = 0;
			this.kernel = true;
			this.runnable = true;
			this.waiting_pids = new ArrayDeque<>();
		}

		public SchdProcess(RuntimeMachine machine, boolean kernel) {
			this(machine);
			this.kernel = kernel;
		}
	}

	private static Logger logger = Logger.getLogger("proc");
	private static final int MAX_PROCESS = 1000;
	private static final int MAX_CYCLE = 150;
	private static final int TIME_SLEEP_FULL = 5;
	private static final int CLOCK_ONCE_SLEEP = 50;
	private static final int CLOCK_WAIT_UI = 500;
	private int cyclePtr = 0;
	private String name;
	private RuntimeCodePage codePage;
	private Map<String, String> arrCodes;
	private Map<String, RuntimeCodePage> arrPages;
	private SchdProcess arrProcess[];
	private Set<Integer> setProcessId;
	private RuntimeService service;
	private boolean isWaitingForUI = false;
	private boolean needToExit = false;
	private boolean highSpeed = false;
	private Map<String, String> pageFileMap;

	private SystemStat stat = new SystemStat();

	public void run() throws Exception {
		long last_time = System.currentTimeMillis();
		stat.cycle = 0;
		while (schd()) {
			long span = System.currentTimeMillis() - last_time;
			if (span > 1000) {
				stat.speed = 1000.0 * stat.cycle / span;
				stat.cycle = 0;
				last_time = System.currentTimeMillis();
				stat.procCache.clear();
				Set<Integer> sorts = new TreeSet<>(setProcessId);
				for (int pid : sorts) {
					stat.procCache.add(getProcInfoById(pid));
				}
			}
		}
	}

	public RuntimeProcess(String name, InputStream input, Map<String, String> pageFileMap) throws Exception {
		this.pageFileMap = pageFileMap;
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

	public List<Integer> getSysProcs() {
		return setProcessId.stream().filter(id -> arrProcess[id].kernel).collect(Collectors.toList());
	}

	public List<Integer> getAllProcs() {
		return new ArrayList<>(setProcessId);
	}

	public Object[] getProcInfoById(int id) {
		if (!setProcessId.contains(id)) {
			return null;
		}
		return arrProcess[id].machine.getProcInfo();
	}

	public RuntimeCodePage getPage(String name) throws Exception {
		String vfs = null;
		if (arrCodes.containsKey(name) || (vfs = service.getFileService().getVfs(name)) != null) {
			String code = vfs == null ? arrCodes.get(name) : vfs;
			if (arrPages.containsKey(code)) {
				return arrPages.get(code);
			} else {
				logger.debug("Loading page: " + name);
				try {
					Grammar grammar = new Grammar(code);
					RuntimeCodePage page = grammar.getCodePage();
					service.getFileService().addVfs(name, code);
					arrPages.put(name, page);
					return page;
				} catch (SyntaxException e) {
					String filename = pageFileMap.get(name);
					e.setFileName(filename == null ? "Unknown Source" : (filename + ".txt"));
					e.setPageName(name);
					System.err.println("#PAGE ERROR# --> " + name);
					throw e;
				}
			}
		}
		return null;
	}

	private boolean schd() throws Exception {
		if (setProcessId.isEmpty())
			return false;
		if (needToExit)
			return false;
		if (isWaitingForUI) {
			if (!highSpeed)
				Thread.sleep(CLOCK_WAIT_UI);
			isWaitingForUI = false;
			return true;
		}
		List<Integer> pids = new ArrayList<>(setProcessId);
		int sleep = 0;
		LABEL_PID:
		for (int pid : pids) {
			SchdProcess process = arrProcess[pid];
			if (process.runnable) {
				int cycle = MAX_CYCLE - process.priority;
				for (int i = 0; i < cycle; i++) {
					if (process.sleep > 0) {
						process.sleep--;
						sleep++;
						break;
					}
					if (process.runnable) {
						stat.cycle++;
						switch (process.machine.runStep()) {
							case 1:
								return false;
							case 2:
								break LABEL_PID;
						}
					}
				}
			} else {
				sleep++;
			}
		}
		if (sleep == pids.size()) { // 都在休眠，等待并减掉休眠时间
			for (int pid : pids) {
				SchdProcess process = arrProcess[pid];
				if (process.runnable) {
					if (process.sleep < CLOCK_ONCE_SLEEP)
						process.sleep = 0;
					else
						process.sleep -= CLOCK_ONCE_SLEEP;
				}
			}
			Thread.sleep(TIME_SLEEP_FULL);
		}
		return true;
	}

	private void runMainProcess(String name, InputStream input) throws Exception {
		initMainProcess(name, input);
	}

	public boolean isBlock(int pid) {
		return setProcessId.contains(pid) && !arrProcess[pid].runnable;
	}

	private void initMainProcess(String name, InputStream input) throws Exception {
		this.arrProcess = new SchdProcess[MAX_PROCESS];
		this.name = name;
		this.codePage = RuntimeCodePage.importFromStream(input);
		this.arrCodes = new HashMap<>();
		this.arrPages = new HashMap<>();
		this.setProcessId = new HashSet<>();
		this.service = new RuntimeService(this);
		RuntimeMachine machine = new RuntimeMachine(name, cyclePtr, -1, this);
		machine.initStep(name, codePage, Collections.emptyList(), 0, null);
		setProcessId.add(cyclePtr);
		arrProcess[cyclePtr++] = new SchdProcess(machine);
	}

	/**
	 * 创建进程
	 *
	 * @param creatorId 创建者ID
	 * @param kernel    是否为内核进程
	 * @param name      页名
	 * @param page      页
	 * @param pc        起始指令
	 * @param obj       参数
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
		for (; ; ) {
			if (arrProcess[cyclePtr] == null) {
				RuntimeMachine machine = new RuntimeMachine(name, cyclePtr, creatorId, this);
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
		logger.debug((kernel ? "Kernel" : "User") + " process #" + pid + " '" + name + "' created, " + setProcessId.size() + " now.");
		return pid;
	}

	public void destroyProcess(int processId) {
		for (int pid : arrProcess[processId].waiting_pids) {
			wakeup(pid);
		}
		arrProcess[processId] = null;
		setProcessId.remove(processId);
		logger.debug("Process #" + processId + " exit, " + setProcessId.size() + " left.");
	}

	@Override
	public boolean block(int pid) {
		if (!setProcessId.contains(pid)) {
			return false;
		}
		if (arrProcess[pid].runnable) {
			arrProcess[pid].runnable = false;
			return true;
		}
		logger.warn("Process #" + pid + " is blocked, but previous status was blocked.");
		return false;
	}

	@Override
	public double getSpeed() {
		return stat.speed;
	}

	@Override
	public boolean wakeup(int pid) {
		if (!setProcessId.contains(pid)) {
			return false;
		}
		if (!arrProcess[pid].runnable) {
			arrProcess[pid].runnable = true;
			return true;
		}
		logger.warn("Process #" + pid + " is running, but previous status was running.");
		return false;
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
	public boolean join(int joined, int pid) {
		if (!setProcessId.contains(pid)) {
			return false;
		}
		if (!setProcessId.contains(joined))
			return false;
		block(pid);
		arrProcess[joined].waiting_pids.add(pid);
		return false;
	}

	@Override
	public boolean live(int pid) {
		return setProcessId.contains(pid);
	}

	@Override
	public boolean addCodePage(String name, String code) {
		if (arrCodes.containsKey(name)) {
			return false;
		}
		service.getFileService().addVfs(name, code);
		arrCodes.put(name, code);
		return true;
	}

	@Override
	public void waitForUI() {
		isWaitingForUI = true;
	}

	@Override
	public List<Object[]> getProcInfoCache() {
		return stat.procCache;
	}

	@Override
	public void setDebug(int pid, boolean debug) {
		if (setProcessId.contains(pid)) {
			arrProcess[pid].machine.setDebug(debug);
		}
	}

	@Override
	public void setHighSpeed(boolean mode) {
		highSpeed = mode;
	}

	private class SystemStat {
		public double speed;
		public int cycle;
		public List<Object[]> procCache;

		public SystemStat() {
			procCache = new ArrayList<>();
		}
	}

	public void halt() {
		needToExit = true;
	}
}
