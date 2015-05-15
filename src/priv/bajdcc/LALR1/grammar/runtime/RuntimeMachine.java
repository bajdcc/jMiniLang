package priv.bajdcc.LALR1.grammar.runtime;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import priv.bajdcc.LALR1.grammar.runtime.RuntimeException.RuntimeError;
import priv.bajdcc.LALR1.grammar.type.TokenTools;
import priv.bajdcc.util.HashListMapEx;
import priv.bajdcc.util.lexer.token.OperatorType;

/**
 * 【虚拟机】运行时自动机
 *
 * @author bajdcc
 */
public class RuntimeMachine implements IRuntimeStack, IRuntimeStatus {

	private HashListMapEx<String, RuntimeCodePage> pageMap = new HashListMapEx<String, RuntimeCodePage>();
	private HashMap<String, ArrayList<RuntimeCodePage>> pageRefer = new HashMap<String, ArrayList<RuntimeCodePage>>();

	/**
	 * 堆栈
	 */
	private RuntimeStack stack = new RuntimeStack();

	/**
	 * 寄存器
	 */
	private RuntimeRegister reg = new RuntimeRegister();

	private RuntimeCodePage currentPage = null;
	private String pageName = null;
	protected boolean debug = true;

	public void run(String name, InputStream input) throws Exception {
		run(name, RuntimeCodePage.importFromStream(input));
	}

	public void add(String name, RuntimeCodePage page) throws Exception {
		if (pageMap.contains(name)) {
			throw new RuntimeException(RuntimeError.DUP_PAGENAME, -1, "请更改名称");
		}
		pageMap.add(name, page);
		pageRefer.put(name, new ArrayList<RuntimeCodePage>());
		pageRefer.get(name).add(page);
		page.getInfo().getDataMap().put("name", name);
	}

	public void run(String name, RuntimeCodePage page) throws Exception {
		add(name, page);
		currentPage = page;
		reg.pageId = name;
		reg.execId = 0;
		switchPage();
		runInsts();
	}

	private void runInsts() throws Exception {
		while (runByStep())
			;
	}

	private boolean runByStep() throws Exception {
		RuntimeInst inst = RuntimeInst.values()[current()];
		if (inst == RuntimeInst.ihalt) {
			return false;
		}
		if (debug) {
			System.err.println();
			System.err.print(reg.execId + ": " + inst.toString());
		}
		OperatorType op = TokenTools.ins2op(inst);
		next();
		if (op != null) {
			if (!RuntimeTools.calcOp(reg, inst, this)) {
				err(RuntimeError.WRONG_OPERTAOR);
			}
		} else {
			if (!RuntimeTools.calcData(reg, inst, this)) {
				if (!RuntimeTools.calcJump(reg, inst, this)) {
					err(RuntimeError.WRONG_INST);
				}
			}
		}
		if (debug) {
			System.err.println();
			System.err.print(stack.toString());
		}
		return true;
	}

	@Override
	public RuntimeObject load() throws RuntimeException {
		if (stack.isEmptyStack()) {
			err(RuntimeError.NULL_STACK);
		}
		return stack.popData();
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
			err(RuntimeError.WRONG_OPERTAOR);
		}
		return (int) obj.getObj();
	}

	private boolean loadBool() throws RuntimeException {
		RuntimeObject obj = top();
		if (!(obj.getObj() instanceof Boolean)) {
			err(RuntimeError.WRONG_OPERTAOR);
		}
		return (boolean) obj.getObj();
	}

	@Override
	public void store(RuntimeObject obj) {
		stack.pushData(obj);
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

	private void next() throws RuntimeException {
		if (debug) {
			System.err.print(" " + current());
		}
		reg.execId++;
		if (!available()) {
			err(RuntimeError.WRONG_CODEPAGE);
		}
	}

	private void err(RuntimeError type) throws RuntimeException {
		System.err.println(stack);
		throw new RuntimeException(type, reg.execId, type.getMessage());
	}

	private void switchPage() {
		currentPage = pageMap.get(reg.pageId);
		pageName = currentPage.getInfo().getDataMap().get("name").toString();
	}

	private int current() {
		if (reg.execId != -1) {
			return currentPage.getInsts().get(reg.execId);
		} else {
			return RuntimeInst.ihalt.ordinal();
		}
	}

	private boolean available() {
		return reg.execId >= 0 && reg.execId < currentPage.getInsts().size();
	}

	private RuntimeObject fetchFromGlobalData(int index)
			throws RuntimeException {
		if (index < 0 || index >= currentPage.getData().size()) {
			err(RuntimeError.WRONG_OPERTAOR);
		}
		return new RuntimeObject(currentPage.getData().get(index));
	}

	@Override
	public void opLoad() throws RuntimeException {
		int idx = loadInt();
		RuntimeObject obj = fetchFromGlobalData(idx);
		obj.setReadonly(true);
		stack.pushData(obj);
	}

	@Override
	public void opLoadFunc() throws RuntimeException {
		int idx = loadInt();
		RuntimeObject obj = new RuntimeObject(idx, RuntimeObjectType.kFunc);
		obj.setReadonly(true);
		stack.pushData(obj);
	}

	@Override
	public void opStore() throws RuntimeException {
		int idx = loadInt();
		RuntimeObject obj = load();
		RuntimeObject target = stack.findVariable(idx);
		if (target == null) {
			err(RuntimeError.WRONG_OPERTAOR);
		}
		if (target.isReadonly()) {
			err(RuntimeError.READONLY_VAR);
		}
		target.copyFrom(obj);
	}

	@Override
	public void opStoreDirect() throws RuntimeException {
		int idx = loadInt();
		RuntimeObject obj = load();
		stack.storeVariableDirect(idx, obj);
	}

	@Override
	public void opAssign() throws RuntimeException {
		RuntimeObject obj = load();
		int idx = loadInt();
		stack.storeVariableDirect(idx, obj);
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
			err(RuntimeError.WRONG_OPERTAOR);
		}
		store(stack.loadFuncArgs(idx));
	}

	@Override
	public void opPushArgs() throws RuntimeException {
		RuntimeObject obj = load();
		obj.setReadonly(true);
		if(!stack.pushFuncArgs(obj)){
			err(RuntimeError.ARG_OVERFLOW);
		}
	}

	@Override
	public void opReturn() throws RuntimeException {
		if (stack.isEmptyStack()) {
			err(RuntimeError.NULL_STACK);
		}
		stack.opReturn(reg);
		switchPage();
	}

	@Override
	public void opCall() throws RuntimeException {
		int address = loadInt();
		stack.opCall(address, pageName, reg.execId, pageName, currentPage
				.getInfo().getFuncNameByAddress(address));
		reg.execId = address;
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
	public void opLoadVar() throws RuntimeException {
		int idx = loadInt();
		store(RuntimeObject.createObject((stack.findVariable(idx))));
	}

	@Override
	public void opJump() throws RuntimeException {
		reg.execId = current();
	}

	@Override
	public void opJumpBool(boolean bool) throws RuntimeException {
		boolean tf = loadBool();
		if (!(tf ^ bool)) {
			reg.execId = current();
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
			err(RuntimeError.WRONG_IMPORT);
		}
		pageRefer.get(pageName).add(page);
	}

	@Override
	public void opLoadExtern() throws RuntimeException {
		int idx = loadInt();
		RuntimeObject obj = fetchFromGlobalData(idx);
		String name = obj.getObj().toString();
		List<RuntimeCodePage> refers = pageRefer.get(currentPage.getInfo()
				.getDataMap().get("name"));
		for (RuntimeCodePage page : refers) {
			IRuntimeDebugValue value = page.getInfo().getValueCallByName(name);
			if (value != null) {
				store(value.getRuntimeObject());
				return;
			}
		}
		err(RuntimeError.WRONG_LOAD_EXTERN);
	}

	@Override
	public void opCallExtern(boolean invoke) throws Exception {
		int idx = loadInt();
		String name = "";
		if (invoke) {
			RuntimeObject obj = stack.findVariable(idx);
			if (obj.getType() == RuntimeObjectType.kFunc) {
				int address = (int) obj.getObj();
				stack.opCall(address, pageName, reg.execId, pageName,
						currentPage.getInfo().getFuncNameByAddress(address));
				reg.execId = address;
				return;
			} else {
				err(RuntimeError.WRONG_LOAD_EXTERN);
			}
		} else {
			RuntimeObject obj = fetchFromGlobalData(idx);
			name = obj.getObj().toString();
		}
		List<RuntimeCodePage> refers = pageRefer.get(currentPage.getInfo()
				.getDataMap().get("name"));
		for (RuntimeCodePage page : refers) {
			IRuntimeDebugExec exec = page.getInfo().getExecCallByName(name);
			if (exec != null) {
				int argsCount = stack.getFuncArgsCount();
				RuntimeObjectType[] types = exec.getArgsType();
				if ((types == null && argsCount != 0)
						|| (types != null && types.length != argsCount)) {
					err(RuntimeError.WRONG_ARGCOUNT);
				}
				ArrayList<RuntimeObject> args = new ArrayList<RuntimeObject>();
				for (int i = 0; i < argsCount; i++) {
					if (types != null) {
						RuntimeObjectType type = types[i];
						if (type != RuntimeObjectType.kObject) {
							RuntimeObject objParam = stack.loadFuncArgs(i);
							RuntimeObjectType objType = objParam.getType();
							if (objType != type) {
								err(RuntimeError.WRONG_ARGTYPE);
							}
						}
					}
					args.add(stack.loadFuncArgs(i));
				}
				stack.opCall(reg.execId, reg.pageId, reg.execId, reg.pageId,
						name);
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
		for (RuntimeCodePage page : refers) {
			int address = page.getInfo().getAddressOfExportFunc(name);
			if (address != -1) {
				String jmpPage = page.getInfo().getDataMap().get("name")
						.toString();
				stack.opCall(address, jmpPage, reg.execId, reg.pageId, name);
				reg.execId = address;
				reg.pageId = jmpPage;
				switchPage();
				return;
			}
		}
		err(RuntimeError.WRONG_LOAD_EXTERN);
	}

	@Override
	public String getHelpString(String name) {
		List<RuntimeCodePage> refers = pageRefer.get(currentPage.getInfo()
				.getDataMap().get("name"));
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
		List<RuntimeCodePage> refers = pageRefer.get(currentPage.getInfo()
				.getDataMap().get("name"));
		for (RuntimeCodePage page : refers) {
			int address = page.getInfo().getAddressOfExportFunc(name);
			if (address != -1) {
				return address;
			}
		}
		err(RuntimeError.WRONG_FUNCNAME);
		return -1;
	}
}
