package com.bajdcc.LALR1.grammar.runtime;

import com.bajdcc.LALR1.grammar.Grammar;
import com.bajdcc.LALR1.grammar.runtime.service.IRuntimeProcessService;
import com.bajdcc.LALR1.grammar.runtime.service.RuntimeService;
import com.bajdcc.LALR1.syntax.handler.SyntaxException;
import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

import static com.bajdcc.LALR1.grammar.runtime.RuntimeMachine.Ring3Option.LOG_FILE;
import static com.bajdcc.LALR1.grammar.runtime.RuntimeMachine.Ring3Option.LOG_PIPE;

/**
 * 【虚拟机】运行时进程
 *
 * @author bajdcc
 */
public class RuntimeProcess implements IRuntimeProcessService {

	private static Deque<Pair<String, String>> pipeDeque = new ConcurrentLinkedDeque<>();
	private int currentPid;

	public static void writePipe(String name, String msg) {
		pipeDeque.add(new Pair<>(name, msg));
	}

	private class SchdProcess {

		public RuntimeMachine machine;
		public int priority;
		public int sleep;
		public int ring;
		public boolean runnable;
		public Queue<Integer> waiting_pids;

		private SchdProcess(RuntimeMachine machine) {
			this.machine = machine;
			this.priority = 128;
			this.sleep = 0;
			this.ring = 0;
			this.runnable = true;
			this.waiting_pids = new ArrayDeque<>();
		}

		public SchdProcess(RuntimeMachine machine, int ring) {
			this(machine);
			this.ring = ring;
		}
	}

	private static Logger logger = Logger.getLogger("proc");
	private static final int MAX_PROCESS = 1000;
	private static final int MAX_CYCLE = 150;
	private static final int TIME_SLEEP_FULL = 5;
	private static final int TIME_SLEEP_HALF = 10;
	private static final int CLOCK_ONCE_SLEEP = 50;
	private static final int CLOCK_WAIT_UI = 500;
	public static final String USER_PROC_FILE_PREFIX = "/proc/";
	public static final String USER_PROC_PIPE_PREFIX = "!USER_PROC#";
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
		stat.ticked = false;
		while (schd()) {
			long span = System.currentTimeMillis() - last_time;
			if (span > 1000) {
				stat.speed = 1000.0 * stat.cycle / span;
				stat.cycle = 0;
				stat.ticked = true;
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
		return setProcessId.stream().filter(id -> arrProcess[id].ring != 0).collect(Collectors.toList());
	}

	public List<Integer> getSysProcs() {
		return setProcessId.stream().filter(id -> arrProcess[id].ring == 0).collect(Collectors.toList());
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
		long pidNum = pids.stream().filter(a -> arrProcess[a].ring < 3).count();
		long pidUserNum = pids.size() - pidNum;
		long sleep = 0;
		long sleepUser = 0;
		LABEL_PID:
		for (int pid : pids) {
			currentPid = pid;
			SchdProcess process = arrProcess[pid];
			if (process != null) {
				if (process.runnable) {
					if (process.ring < 3) {
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
						int cycle = MAX_CYCLE - process.priority;
						try {
							for (int i = 0; i < cycle; i++) {
								if (process.sleep > 0) {
									process.sleep--;
									sleepUser++; // boost!
									break;
								}
								if (process.runnable) {
									stat.cycle++;
									switch (process.machine.runStep()) {
										case 1:
											return false;
										case 2:
											ring3Kill(pid, "正常退出");
											break LABEL_PID;
									}
								}
							}
						} catch (RuntimeException e) {
							String error = e.getError().getMessage() + " " + e.getPosition() + ": " + e.getInfo();
							System.err.println(error);
							e.printStackTrace();
							ring3Kill(pid, error);
						}
					}
				} else if (process.ring < 3) {
					sleep++;
				} else {
					sleepUser++;
				}
			}
		}
		if (sleep == pidNum) { // 都在休眠，等待并减掉休眠时间
			for (int pid : pids) {
				SchdProcess process = arrProcess[pid];
				if (process != null && process.runnable) {
					if (process.sleep < CLOCK_ONCE_SLEEP)
						process.sleep = 0;
					else
						process.sleep -= CLOCK_ONCE_SLEEP;
				}
			}
			if (sleepUser == pidUserNum)
				Thread.sleep(TIME_SLEEP_FULL);
			else if (stat.ticked) {
				Thread.sleep(TIME_SLEEP_HALF);
				stat.ticked = false;
			}
			if (!pipeDeque.isEmpty()) {
				Pair<String, String> pair = pipeDeque.poll();
				service.getPipeService().writeStringNew(pair.getKey(), pair.getValue());
			}
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
		RuntimeMachine machine = new RuntimeMachine(name, 0, cyclePtr, -1, this);
		machine.initStep(name, codePage, Collections.emptyList(), 0, null);
		setProcessId.add(cyclePtr);
		arrProcess[cyclePtr++] = new SchdProcess(machine);
	}

	/**
	 * 创建进程
	 *
	 * @param creatorId 创建者ID
	 * @param ring      RING层数
	 * @param name      页名
	 * @param page      页
	 * @param pc        起始指令（fork时为负）
	 * @param obj       参数
	 * @return 进程ID
	 * @throws Exception 运行时异常
	 */
	public int createProcess(int creatorId, int ring, String name, RuntimeCodePage page, int pc, RuntimeObject obj) throws Exception {
		if (setProcessId.size() >= MAX_PROCESS) {
			arrProcess[creatorId].machine.err(
					RuntimeException.RuntimeError.PROCESS_OVERFLOW, String.valueOf(page));
		}
		if (!(arrProcess[creatorId].ring == 0) && ring == 0) {
			arrProcess[creatorId].machine.err(
					RuntimeException.RuntimeError.ACCESS_FORBIDDEN, String.valueOf(page));
		}
		int pid;
		for (; ; ) {
			if (arrProcess[cyclePtr] == null) {
				if (ring == 3) {
					if (!name.startsWith("/"))
						name = USER_PROC_FILE_PREFIX + cyclePtr;
				}
				RuntimeMachine machine = new RuntimeMachine(name, ring, cyclePtr, creatorId, this);
				if (pc >= 0)
					machine.initStep(name, page, arrProcess[creatorId].machine.getPageRefers(name), pc, obj);
				else
					machine.copyFrom(arrProcess[creatorId].machine);
				setProcessId.add(cyclePtr);
				pid = cyclePtr;
				arrProcess[cyclePtr++] = new SchdProcess(machine, ring);
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
		logger.debug("RING-" + ring + " process #" + pid + " '" + name + "' created, " + setProcessId.size() + " now.");
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
	public int getPid() {
		return currentPid;
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
		public boolean ticked;
		public List<Object[]> procCache;

		public SystemStat() {
			procCache = new ArrayList<>();
		}
	}

	public void halt() {
		needToExit = true;
	}

	@Override
	public int ring3Kill(int pid, String error) {
		if (!setProcessId.contains(pid)) {
			return -1;
		}
		IRuntimeRing3 ring3 = arrProcess[pid].machine.getRing3();
		if (!ring3.isRing3()) {
			return -2;
		}
		int tmp = currentPid;
		currentPid = pid;
		service.getUserService().destroy();
		currentPid = tmp;
		if (ring3.isOptionsBool(LOG_FILE))
			service.getFileService().addVfs(USER_PROC_FILE_PREFIX + pid, error);
		if (!ring3.isOptionsBool(LOG_PIPE))
			service.getPipeService().destroyByName(pid, USER_PROC_PIPE_PREFIX + pid);
		for (int id : arrProcess[pid].waiting_pids) {
			wakeup(id);
		}
		setProcessId.remove(pid);
		arrProcess[pid] = null;
		logger.debug("RING3 proc #" + pid + " exit, " + setProcessId.size() + " left.");
		return 0;
	}

	@Override
	public IRuntimeRing3 getRing3() {
		return arrProcess[currentPid].machine;
	}

	@Override
	public IRuntimeRing3 getRing3(int pid) {
		if (!setProcessId.contains(pid))
			return null;
		return arrProcess[pid].machine.getRing3();
	}
}
