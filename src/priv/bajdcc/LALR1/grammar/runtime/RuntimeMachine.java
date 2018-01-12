package priv.bajdcc.LALR1.grammar.runtime;

import org.apache.log4j.Logger;
import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeException.RuntimeError;
import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeArray;
import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeFuncObject;
import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeMap;
import priv.bajdcc.LALR1.grammar.runtime.service.IRuntimeService;
import priv.bajdcc.LALR1.grammar.type.TokenTools;
import priv.bajdcc.LALR1.interpret.module.*;
import priv.bajdcc.LALR1.syntax.handler.SyntaxException;
import priv.bajdcc.util.HashListMapEx;
import priv.bajdcc.util.lexer.token.OperatorType;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

/**
 * 【虚拟机】运行时自动机
 *
 * @author bajdcc
 */
public class RuntimeMachine implements IRuntimeStack, IRuntimeStatus {

	private static Logger logger = Logger.getLogger("machine");
	private static IInterpreterModule[] modules;

	private HashListMapEx<String, RuntimeCodePage> pageMap = new HashListMapEx<>();
	private Map<String, ArrayList<RuntimeCodePage>> pageRefer = new HashMap<>();
	private Stack<RuntimeObject> stkYieldData = new Stack<>();
	private RuntimeStack stack = new RuntimeStack();
	private RuntimeCodePage currentPage;
	private String pageName;
	private String name;
	private String description;
	private RuntimeProcess process;
	private int pid;
	private int parentId;
	private boolean debug = false;

	public RuntimeMachine() throws Exception {
		if (modules == null) {
			logger.debug("Loading modules...");
			modules = new IInterpreterModule[]{
					ModuleBase.getInstance(),
					ModuleMath.getInstance(),
					ModuleList.getInstance(),
					ModuleFunction.getInstance(),
					ModuleString.getInstance(),
					ModuleProc.getInstance(),
					ModuleUI.getInstance(),
					ModuleTask.getInstance(),
					ModuleRemote.getInstance(),
					ModuleLisp.getInstance(),
					ModuleNet.getInstance(),
					ModuleFile.getInstance(),
			};
		}

		for (IInterpreterModule module : modules) {
			try {
				run(module.getModuleName(), module.getCodePage());
			} catch (SyntaxException e) {
				e.setPageName(module.getModuleName());
				e.setFileName(module.getClass().getSimpleName() + ".txt");
				throw e;
			}
		}
	}

	public RuntimeMachine(String name, int id, int parentId, RuntimeProcess process) throws Exception {
		this();
		this.name = name;
		this.description = "none";
		this.pid = id;
		this.parentId = parentId;
		this.process = process;
	}

	public void run(String name, InputStream input) throws Exception {
		run(name, RuntimeCodePage.importFromStream(input));
	}

	@Override
	public void runPage(String name) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(name));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
			sb.append(System.lineSeparator());
		}
		br.close();
		logger.debug("Loading file: " + name);
		Grammar grammar = new Grammar(sb.toString());
		run(name, grammar.getCodePage());
	}

	@Override
	public int runProcess(String name) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(name));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
			sb.append(System.lineSeparator());
		}
		br.close();
		logger.debug("Loading file: " + name);
		Grammar grammar = new Grammar(sb.toString());
		return process.createProcess(pid, true, name, grammar.getCodePage(), 0, null);
	}

	@Override
	public int runProcessX(String name) throws Exception {
		RuntimeCodePage page = process.getPage(name);
		if (page == null) {
			return -1;
		}
		return process.createProcess(pid, true, name, page, 0, null);
	}

	@Override
	public int runUsrProcess(String name) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(name));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
			sb.append(System.lineSeparator());
		}
		br.close();
		Grammar grammar = new Grammar(sb.toString());
		return process.createProcess(pid, false, name, grammar.getCodePage(), 0, null);
	}

	@Override
	public int runUsrProcessX(String name) throws Exception {
		RuntimeCodePage page = process.getPage(name);
		if (page == null) {
			return -1;
		}
		return process.createProcess(pid, false, name, page, 0, null);
	}

	public void add(String name, RuntimeCodePage page) throws Exception {
		if (pageMap.contains(name)) {
			throw new RuntimeException(RuntimeError.DUP_PAGENAME, -1, "请更改名称");
		}
		pageMap.add(name, page);
		pageRefer.put(name, new ArrayList<>());
		pageRefer.get(name).add(page);
		page.getInfo().getDataMap().put("name", name);
	}

	public void run(String name, RuntimeCodePage page) throws Exception {
		add(name, page);
		currentPage = page;
		stack.reg.pageId = name;
		stack.reg.execId = 0;
		switchPage();
		runInsts();
	}

	private void runInsts() throws Exception {
		while (runByStep());
	}

	public void initStep(String name, RuntimeCodePage page, List<RuntimeCodePage> refers, int pc, RuntimeObject obj) throws Exception {
		add(name, page);
		currentPage = page;
		stack.reg.pageId = name;
		stack.reg.execId = -1;
		switchPage();
		if (refers != null)
			pageRefer.get(pageName).addAll(refers);
		opOpenFunc();
		if (obj != null) {
			opPushObj(obj);
			opPushArgs();
		}
		opPushPtr(pc);
		opCall();
	}

	public int runStep() throws Exception {
		RuntimeInst inst = RuntimeInst.values()[currentInst()];
		if (inst == RuntimeInst.ihalt) {
			process.destroyProcess(pid);
			return 2;
		}
		return runByStep() ? 0 : 1;
	}

	private boolean runByStep() throws Exception {
		RuntimeInst inst = RuntimeInst.values()[currentInst()];
		if (inst == RuntimeInst.ihalt) {
			return false;
		}
//		if (debug) {
//			System.err.println();
//			System.err.print(stack.reg.execId + ": " + inst.toString());
//		}
		OperatorType op = TokenTools.ins2op(inst);
		nextInst();
		if (op != null) {
			if (!RuntimeTools.calcOp(stack.reg, inst, this)) {
				err(RuntimeError.UNDEFINED_CONVERT, op.getName());
			}
		} else {
			if (!RuntimeTools.calcData(stack.reg, inst, this)) {
				if (!RuntimeTools.calcJump(stack.reg, inst, this)) {
					err(RuntimeError.WRONG_INST, inst.toString());
				}
			}
		}
//		if (debug) {
//			System.err.println();
//			System.err.print(stack.toString());
//			System.err.print("协程栈：");
//			System.err.print(stkYieldData.toString());
//			System.err.println();
//		}
		return true;
	}

	@Override
	public RuntimeObject load() throws RuntimeException {
		if (stack.isEmptyStack()) {
			err(RuntimeError.NULL_STACK);
		}
		return stack.popData();
	}

	@Override
	public void store(RuntimeObject obj) {
		stack.pushData(obj);
	}

	private RuntimeObject dequeue() throws RuntimeException {
		if (stkYieldData.isEmpty()) {
			err(RuntimeError.NULL_QUEUE);
		}
		return stkYieldData.pop();
	}

	private void enqueue(RuntimeObject obj) {
		stkYieldData.add(obj);
	}

	public RuntimeObject top() throws RuntimeException {
		if (stack.isEmptyStack()) {
			err(RuntimeError.NULL_STACK);
		}
		return stack.top();
	}

	private int loadInt() throws RuntimeException {
		RuntimeObject obj = load();
		if (!(obj.getObj() instanceof Integer)) {
			err(RuntimeError.WRONG_OPERATOR, RuntimeObjectType.kInt.getName() + " " + obj.toString());
		}
		return (int) obj.getObj();
	}

	private boolean loadBool() throws RuntimeException {
		RuntimeObject obj = load();
		if (!(obj.getObj() instanceof Boolean)) {
			err(RuntimeError.WRONG_OPERATOR, RuntimeObjectType.kBool.getName() + " " + obj.toString());
		}
		return (boolean) obj.getObj();
	}

	private boolean loadBoolRetain() throws RuntimeException {
		RuntimeObject obj = top();
		if (!(obj.getObj() instanceof Boolean)) {
			err(RuntimeError.WRONG_OPERATOR, RuntimeObjectType.kBool.getName() + " " + obj.toString());
		}
		return (boolean) obj.getObj();
	}

	@Override
	public void push() throws RuntimeException {
		RuntimeObject obj = new RuntimeObject(current());
		obj.setReadonly(true);
		store(obj);
		next();
	}

	@Override
	public void pop() throws RuntimeException {
		if (stack.isEmptyStack()) {
			err(RuntimeError.NULL_STACK);
		}
		stack.popData();
	}

	private void nextInst() throws RuntimeException {
		stack.reg.execId++;
		if (!available()) {
			err(RuntimeError.WRONG_CODEPAGE);
		}
	}

	private void next() throws RuntimeException {
		if (debug) {
			System.err.print(" " + current());
		}
		stack.reg.execId += 4;
		if (!available()) {
			err(RuntimeError.WRONG_CODEPAGE);
		}
	}

	@Override
	public void err(RuntimeError type) throws RuntimeException {
		System.err.println(stack);
		throw new RuntimeException(type, stack.reg.execId, type.getMessage() + "\n\n[ CODE ]\n" + currentPage.getDebugInfoByInc(stack.reg.execId));
	}

	@Override
	public void err(RuntimeError type, String message) throws RuntimeException {
		System.err.println(stack);
		throw new RuntimeException(type, stack.reg.execId, type.getMessage() + " " + message + "\n\n[ CODE ]\n" + currentPage.getDebugInfoByInc(stack.reg.execId));
	}

	@Override
	public int createProcess(RuntimeFuncObject func) throws Exception {
		return process.createProcess(pid, true, func.getPage(), pageMap.get(func.getPage()), func.getAddr(), null);
	}

	@Override
	public int createProcess(RuntimeFuncObject func, RuntimeObject obj) throws Exception {
		return process.createProcess(pid, true, func.getPage(), pageMap.get(func.getPage()), func.getAddr(), obj);
	}

	@Override
	public int createUsrProcess(RuntimeFuncObject func) throws Exception {
		return process.createProcess(pid, false, func.getPage(), pageMap.get(func.getPage()), func.getAddr(), null);
	}

	@Override
	public int createUsrProcess(RuntimeFuncObject func, RuntimeObject obj) throws Exception {
		return process.createProcess(pid, false, func.getPage(), pageMap.get(func.getPage()), func.getAddr(), obj);
	}

	@Override
	public List<RuntimeCodePage> getPageRefers(String page) {
		return pageRefer.get(page);
	}

	@Override
	public int getPid() {
		return pid;
	}

	@Override
	public int getParentPid() {
		return parentId;
	}

	@Override
	public int getPriority() {
		return process.getPriority(pid);
	}

	@Override
	public boolean setPriority(int priority) {
		return process.setPriority(pid, priority);
	}

	@Override
	public IRuntimeService getService() {
		return process.getService();
	}

	@Override
	public int sleep() {
		return 0;
	}

	@Override
	public List<Integer> getUsrProcs() {
		return process.getUsrProcs();
	}

	@Override
	public List<Integer> getSysProcs() {
		return process.getSysProcs();
	}

	@Override
	public List<Integer> getAllProcs() {
		return process.getAllProcs();
	}

	@Override
	public Object[] getProcInfo() {
		return new Object[]{
				process.isBlock(pid) ? "B" : "R",
				name,
				stack.getFuncName(),
				description,
		};
	}

	@Override
	public void setProcDesc(String desc) {
		description = desc;
	}

	@Override
	public Object[] getProcInfoById(int id) {
		return process.getProcInfoById(id);
	}

	private void switchPage() throws RuntimeException {
		if (!stack.reg.pageId.isEmpty()) {
			currentPage = pageMap.get(stack.reg.pageId);
			pageName = currentPage.getInfo().getDataMap().get("name").toString();
		} else {
			err(RuntimeError.WRONG_CODEPAGE);
		}
	}

	private Byte getInst(int pc) throws RuntimeException {
		List<Byte> code = currentPage.getInsts();
		if (pc < 0 || pc >= code.size()) {
			err(RuntimeError.WRONG_INST, String.valueOf(pc));
		}
		return code.get(pc);
	}

	private Byte currentInst() throws RuntimeException {
		if (stack.reg.execId != -1) {
			return getInst(stack.reg.execId);
		} else {
			return (byte) RuntimeInst.ihalt.ordinal();
		}
	}

	private int current() throws RuntimeException {
		int op = 0;
		byte b;
		for (int i = 0; i < 4; i++) {
			b = getInst(stack.reg.execId + i);
			op += (b & 0xFF) << (8 * i);
		}
		return op;
	}

	private boolean available() {
		return stack.reg.execId >= 0
				&& stack.reg.execId < currentPage.getInsts().size();
	}

	private RuntimeObject fetchFromGlobalData(int index)
			throws RuntimeException {
		if (index < 0 || index >= currentPage.getData().size()) {
			err(RuntimeError.WRONG_OPERATOR, String.valueOf(index));
		}
		return new RuntimeObject(currentPage.getData().get(index));
	}

	@Override
	public void opLoad() throws RuntimeException {
		int idx = loadInt();
		RuntimeObject obj = fetchFromGlobalData(idx);
		if (obj.getSymbol() == null)
			obj.setSymbol(currentPage.getData().get(idx));
		stack.pushData(obj);
	}

	@Override
	public void opLoadFunc() throws RuntimeException {
		int idx = loadInt();
		RuntimeFuncObject func = new RuntimeFuncObject(pageName, idx);
		RuntimeObject obj = new RuntimeObject(func);
		int envSize = loadInt();
		for (int i = 0; i < envSize; i++) {
			int id = loadInt();
			func.addEnv(id, stack.findVariable(id));
		}
		stack.pushData(obj);
	}

	@Override
	public void opStore() throws RuntimeException {
		int idx = loadInt();
		RuntimeObject obj = load();
		RuntimeObject target = stack.findVariable(idx);
		if (target == null) {
			err(RuntimeError.WRONG_OPERATOR);
		}
		if (target.isReadonly()) {
			err(RuntimeError.READONLY_VAR, target.toString());
		}
		target.copyFrom(obj);
		store(target);
	}

	@Override
	public void opStoreDirect() throws RuntimeException {
		int idx = loadInt();
		RuntimeObject obj = load();
		obj.setReadonly(false);
		if (obj.getSymbol() == null)
			obj.setSymbol(currentPage.getData().get(idx));
		stack.storeVariableDirect(idx, obj);
		store(obj);
	}

	@Override
	public void opOpenFunc() throws RuntimeException {
		if (!stack.pushFuncData()) {
			err(RuntimeError.STACK_OVERFLOW);
		}
	}

	@Override
	public void opLoadArgs() throws RuntimeException {
		int idx = current();
		next();
		if (idx < 0 || idx >= stack.getFuncArgsCount()) {
			err(RuntimeError.WRONG_ARGINVALID, String.valueOf(idx));
		}
		store(stack.loadFuncArgs(idx));
	}

	@Override
	public void opPushArgs() throws RuntimeException {
		RuntimeObject obj = load();
		obj.setReadonly(true);
		if (!stack.pushFuncArgs(obj)) {
			err(RuntimeError.ARG_OVERFLOW, obj.toString());
		}
	}

	@Override
	public void opReturn() throws RuntimeException {
		if (stack.isEmptyStack()) {
			err(RuntimeError.NULL_STACK);
		}
		stack.opReturn(stack.reg);
		switchPage();
	}

	@Override
	public void opCall() throws RuntimeException {
		int address = loadInt();
		stack.opCall(address, pageName, stack.reg.execId, pageName, currentPage
				.getInfo().getFuncNameByAddress(address));
		stack.reg.execId = address;
		stack.reg.pageId = pageName;
	}

	@Override
	public void opPushNull() {
		store(new RuntimeObject(null, true, false));
	}

	@Override
	public void opPushZero() {
		store(new RuntimeObject(0, true, false));
	}

	@Override
	public void opPushNan() {
		store(new RuntimeObject(null, RuntimeObjectType.kNan, true, false));
	}

	@Override
	public void opPushPtr(int pc) {
		store(new RuntimeObject(pc));
	}

	@Override
	public void opPushObj(RuntimeObject obj) {
		store(obj);
	}

	@Override
	public void opLoadVar() throws RuntimeException {
		int idx = loadInt();
		store(RuntimeObject.createObject((stack.findVariable(idx))));
	}

	@Override
	public void opJump() throws RuntimeException {
		stack.reg.execId = current();
	}

	@Override
	public void opJumpBool(boolean bool) throws RuntimeException {
		boolean tf = loadBool();
		if (tf == bool) {
			stack.reg.execId = current();
		} else {
			next();
		}
	}

	@Override
	public void opJumpBoolRetain(boolean bool) throws RuntimeException {
		boolean tf = loadBoolRetain();
		if (tf == bool) {
			stack.reg.execId = current();
		} else {
			next();
		}
	}

	@Override
	public void opJumpZero(boolean bool) throws RuntimeException {
		int val = loadInt();
		if ((val == 0) == bool) {
			stack.reg.execId = current();
		} else {
			next();
		}
	}

	@Override
	public void opJumpYield() throws RuntimeException {
		String hash = RuntimeTools.getYieldHash(stack.level,
				stack.getFuncLevel(), pageName, stack.reg.execId - 1);
		if (stack.getYieldStack(hash) != null) {
			stack.reg.execId = current();
		} else {
			next();
		}
	}

	@Override
	public void opJumpNan() throws RuntimeException {
		RuntimeObject obj = top();
		if (obj.getType() == RuntimeObjectType.kNan) {
			stack.reg.execId = current();
		} else {
			next();
		}
	}

	@Override
	public void opImport() throws RuntimeException {
		int idx = loadInt();
		RuntimeObject obj = fetchFromGlobalData(idx);
		RuntimeCodePage page = pageMap.get(obj.getObj().toString());
		if (page == null) {
			err(RuntimeError.WRONG_IMPORT, obj.toString());
		}
		pageRefer.get(pageName).add(page);
	}

	@Override
	public void opLoadExtern() throws RuntimeException {
		int idx = loadInt();
		RuntimeObject obj = fetchFromGlobalData(idx);
		String name = obj.getObj().toString();
		List<RuntimeCodePage> refers = pageRefer.get(currentPage.getInfo()
				.getDataMap().get("name").toString());
		for (RuntimeCodePage page : refers) {
			IRuntimeDebugValue value = page.getInfo().getValueCallByName(name);
			if (value != null) {
				store(value.getRuntimeObject());
				return;
			}
		}
		err(RuntimeError.WRONG_LOAD_EXTERN, name);
	}

	@Override
	public void opCallExtern(boolean invoke) throws Exception {
		int idx = loadInt();
		String name = "";
		if (invoke) {
			RuntimeStack itStack = stack;
			RuntimeObject obj = null;
			while (obj == null && itStack != null) {
				obj = itStack.findVariable(idx);
				itStack = itStack.prev;
			}
			if (obj == null) {
				err(RuntimeError.WRONG_LOAD_EXTERN, String.valueOf(idx));
			}
			if (obj.getType() == RuntimeObjectType.kFunc) {
				RuntimeFuncObject func = (RuntimeFuncObject) obj.getObj();
				Map<Integer, RuntimeObject> env = func.getEnv();
				if (env != null) {
					for (Entry<Integer, RuntimeObject> entry : env.entrySet()) {
						int id = entry.getKey();
						RuntimeObject o = entry.getValue();
						if (o != null) {
							if (o.getSymbol() == null)
								o.setSymbol(currentPage.getData().get(id));
							o.setReadonly(false);
						}
						stack.storeClosure(id, o);
					}
					stack.pushData(obj);
				}
				int address = func.getAddr();
				stack.opCall(address, func.getPage(), stack.reg.execId,
						pageName, pageMap.get(func.getPage()).getInfo()
								.getFuncNameByAddress(address));
				stack.reg.execId = address;
				stack.reg.pageId = func.getPage();
				switchPage();
				pop();
				return;
			} else if (obj.getType() == RuntimeObjectType.kString) {
				name = obj.getObj().toString();
			} else {
				err(RuntimeError.WRONG_LOAD_EXTERN, obj.toString());
			}
		} else {
			RuntimeObject obj = fetchFromGlobalData(idx);
			name = obj.getObj().toString();
		}
		List<RuntimeCodePage> refers = pageRefer.get(pageName);
		for (RuntimeCodePage page : refers) {
			int address = page.getInfo().getAddressOfExportFunc(name);
			if (address != -1) {
				String jmpPage = page.getInfo().getDataMap().get("name")
						.toString();
				stack.opCall(address, jmpPage, stack.reg.execId,
						stack.reg.pageId, name);
				stack.reg.execId = address;
				stack.reg.pageId = jmpPage;
				switchPage();
				return;
			}
		}
		for (RuntimeCodePage page : refers) {
			IRuntimeDebugExec exec = page.getInfo().getExecCallByName(name);
			if (exec != null) {
				int argsCount = stack.getFuncArgsCount();
				RuntimeObjectType[] types = exec.getArgsType();
				if ((types == null && argsCount != 0)
						|| (types != null && types.length != argsCount)) {
					err(RuntimeError.WRONG_ARGCOUNT, name + " " + String.valueOf(argsCount));
				}
				List<RuntimeObject> args = new ArrayList<>();
				for (int i = 0; i < argsCount; i++) {
					RuntimeObjectType type = types[i];
					RuntimeObject objParam = stack.loadFuncArgs(i);
					if (type != RuntimeObjectType.kObject) {
						RuntimeObjectType objType = objParam.getType();
						if (objType != type) {
							Token token = Token.createFromObject(objParam
									.getObj());
							TokenType objTokenType = RuntimeObject
									.toTokenType(type);
							if (objTokenType == TokenType.ERROR) {
								err(RuntimeError.WRONG_ARGTYPE, name + " " + objTokenType.getName());
							}
							if (!TokenTools.promote(objTokenType, token)) {
								err(RuntimeError.UNDEFINED_CONVERT, name + " " + token.toString() + " " + objTokenType.getName());
							} else {
								objParam.setObj(token.object);
							}
						}
					}
					args.add(objParam);
				}
				stack.opCall(stack.reg.execId, stack.reg.pageId,
						stack.reg.execId, stack.reg.pageId, name);
				RuntimeObject retVal = exec.ExternalProcCall(args, this);
				if (retVal == null) {
					store(new RuntimeObject(null));
				} else {
					store(retVal);
				}
				opReturn();
				return;
			}
		}
		err(RuntimeError.WRONG_LOAD_EXTERN, name);
	}

	@Override
	public String getHelpString(String name) {
		List<RuntimeCodePage> refers = pageRefer.get(pageName);
		for (RuntimeCodePage page : refers) {
			IRuntimeDebugExec exec = page.getInfo().getExecCallByName(name);
			if (exec != null) {
				String doc = exec.getDoc();
				return doc == null ? "过程无文档" : doc;
			}
		}
		return "过程不存在";
	}

	@Override
	public int getFuncAddr(String name) throws RuntimeException {
		List<RuntimeCodePage> refers = pageRefer.get(pageName);
		for (RuntimeCodePage page : refers) {
			int address = page.getInfo().getAddressOfExportFunc(name);
			if (address != -1) {
				return address;
			}
		}
		err(RuntimeError.WRONG_FUNCNAME, name);
		return -1;
	}

	@Override
	public void opYield(boolean input) throws RuntimeException {
		if (input) {
			enqueue(load());
		} else {
			store(dequeue());
		}
	}

	@Override
	public void opYieldSwitch(boolean forward) throws RuntimeException {
		if (forward) {
			int yldLine = current();
			next();
			String hash = RuntimeTools.getYieldHash(stack.level,
					stack.getFuncLevel(), pageName, yldLine);
			RuntimeStack newStack = stack.getYieldStack(hash);
			if (newStack != null) {
				stack = newStack;
			} else {
				err(RuntimeError.WRONG_COROUTINE, hash);
			}
		} else {
			if (stack.prev == null) {
				err(RuntimeError.WRONG_COROUTINE);
			}
			stack = stack.prev;
		}
		switchPage();
	}

	private int loadYieldData() throws RuntimeException {
		int size = stkYieldData.size();
		while (!stkYieldData.isEmpty()) {
			opYield(false);
		}
		return size;
	}

	private void loadYieldArgs(int argsSize) throws RuntimeException {
		for (int i = 0; i < argsSize; i++) {
			opPushArgs();
		}
	}

	@Override
	public void opYieldCreateContext() throws Exception {
		RuntimeStack newStack = new RuntimeStack(stack);
		int yldLine = current();
		next();
		String hash = RuntimeTools.getYieldHash(stack.level,
				stack.getFuncLevel(), pageName, yldLine);
		stack.addYieldStack(hash, newStack);
		stack = newStack;
		int yieldSize = loadYieldData();
		int type = loadInt();
		opOpenFunc();
		loadYieldArgs(yieldSize - 2);
		switch (type) {
		case 1:
			opCall();
			break;
		case 2:
			opCallExtern(true);
			break;
		case 3:
			opCallExtern(false);
			break;
		default:
			break;
		}
	}

	@Override
	public void opYieldDestroyContext() throws RuntimeException {
		stack.popYieldStack();
	}

	@Override
	public void opScope(boolean enter) throws RuntimeException {
		if (stack.getFuncLevel() == 0)
			err(RuntimeError.EMPTY_CALLSTACK);
		if (enter) {
			stack.enterScope();
		} else {
			stack.leaveScope();
		}
	}

	@Override
	public void opArr() {
		stack.pushData(new RuntimeObject(new RuntimeArray()));
	}

	@Override
	public void opMap() {
		stack.pushData(new RuntimeObject(new RuntimeMap()));
	}
}
