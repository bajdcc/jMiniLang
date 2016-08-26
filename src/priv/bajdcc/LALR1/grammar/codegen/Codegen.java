package priv.bajdcc.LALR1.grammar.codegen;

import java.util.ArrayList;
import java.util.List;

import priv.bajdcc.LALR1.grammar.runtime.ICodegenByteWriter;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeDebugInfo;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInstBase;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInstBinary;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInstNon;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInstUnary;
import priv.bajdcc.LALR1.grammar.symbol.SymbolTable;
import priv.bajdcc.LALR1.grammar.tree.Function;
import priv.bajdcc.util.HashListMap;
import priv.bajdcc.util.HashListMapEx;

/**
 * 【中间代码】中间代码接口实现
 *
 * @author bajdcc
 */
public class Codegen implements ICodegen, ICodegenBlock, ICodegenByteWriter {

	private HashListMap<Object> symbolList = new HashListMap<Object>();
	private HashListMapEx<String, Function> funcMap = new HashListMapEx<String, Function>();
	private CodegenData data = new CodegenData();
	private RuntimeDebugInfo info = new RuntimeDebugInfo();
	private List<Byte> insts = null;

	public Codegen(SymbolTable symbol) {
		symbolList = symbol.getManageDataService().getSymbolList();
		funcMap = symbol.getManageDataService().getFuncMap();
	}

	@Override
	public int getFuncIndex(Function func) {
		return funcMap.indexOf(func);
	}

	/**
	 * 产生中间代码
	 */
	public void gencode() {
		genCode(RuntimeInst.iopena);
		genCodeWithFuncWriteBack(RuntimeInst.ipush, 0);
		genCode(RuntimeInst.icall);
		genCode(RuntimeInst.ipop);
		genCode(RuntimeInst.ihalt);
		genCode(RuntimeInst.inop);
		for (Function func : funcMap.list) {
			if (func.isExtern()) {
				info.addExports(func.getRealName(), data.getCodeIndex());
				info.addExternalFunc(func.getRealName(), new CodegenFuncDoc(
						func.getDoc()));
			}
			func.genCode(this);
		}
	}

	/**
	 * 产生代码页
	 * 
	 * @return 代码页
	 */
	public RuntimeCodePage genCodePage() {
		List<Object> objs = new ArrayList<Object>();
		for (Object token : symbolList.list) {
			objs.add(token);
		}
		insts = new ArrayList<Byte>();
		for (RuntimeInstUnary unary : data.callsToWriteBack) {
			unary.op1 = data.funcEntriesMap.get(funcMap.list.get(unary.op1)
					.getRealName());
		}
		for (RuntimeInstBase inst : data.insts) {
			inst.gen(this);
		}
		return new RuntimeCodePage(objs, insts, info);
	}

	@Override
	public void genFuncEntry(String funcName) {
		data.pushFuncEntry(funcName);
		info.addFunc(funcName, data.getCodeIndex());
	}

	@Override
	public RuntimeInstNon genCode(RuntimeInst inst) {
		RuntimeInstNon in = new RuntimeInstNon(inst);
		data.pushCode(in);
		return in;
	}

	@Override
	public RuntimeInstUnary genCode(RuntimeInst inst, int op1) {
		RuntimeInstUnary in = new RuntimeInstUnary(inst, op1);
		data.pushCode(in);
		return in;
	}

	@Override
	public RuntimeInstUnary genCodeWithFuncWriteBack(RuntimeInst inst, int op1) {
		RuntimeInstUnary in = new RuntimeInstUnary(inst, op1);
		data.pushCodeWithFuncWriteBack(in);
		return in;
	}

	@Override
	public RuntimeInstBinary genCode(RuntimeInst inst, int op1, int op2) {
		RuntimeInstBinary in = new RuntimeInstBinary(inst, op1, op2);
		data.pushCode(in);
		return in;
	}

	@Override
	public ICodegenBlock getBlockService() {
		return this;
	}

	@Override
	public RuntimeInstUnary genBreak() {
		CodegenBlock block = data.getBlock();
		if (block == null) {
			return null;
		}
		RuntimeInstUnary in = new RuntimeInstUnary(RuntimeInst.ijmp,
				block.breakId);
		data.pushCode(in);
		return in;
	}

	@Override
	public RuntimeInstUnary genContinue() {
		CodegenBlock block = data.getBlock();
		if (block == null) {
			return null;
		}
		RuntimeInstUnary in = new RuntimeInstUnary(RuntimeInst.ijmp,
				block.continueId);
		data.pushCode(in);
		return in;
	}

	@Override
	public void enterBlockEntry(CodegenBlock block) {
		data.enterBlockEntry(block);
	}

	@Override
	public void leaveBlockEntry() {
		data.leaveBlockEntry();
	}

	@Override
	public boolean isInBlock() {
		return data.hasBlock();
	}

	@Override
	public int genDataRef(Object object) {
		return symbolList.put(object);
	}

	@Override
	public int getCodeIndex() {
		return data.getCodeIndex();
	}

	public String getCodeString() {
		StringBuilder sb = new StringBuilder();
		sb.append("#### 中间代码 ####");
		sb.append(System.lineSeparator());
		int idx = 0;
		for (RuntimeInstBase inst : data.insts) {
			sb.append(String.format("%03d\t%s", idx, inst.toString("\t")));
			idx += inst.getAdvanceLength();
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return getCodeString();
	}

	@Override
	public void genInst(RuntimeInst inst) {
		insts.add((byte) inst.ordinal());
	}

	@Override
	public void genOp(int op) {
		for (int i = 0; i < 4; i++) {
			insts.add((byte) (op >> 8 * i & 0xFF));
		}
	}
}
