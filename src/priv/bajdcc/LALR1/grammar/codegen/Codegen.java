package priv.bajdcc.LALR1.grammar.codegen;

import java.util.ArrayList;

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
public class Codegen implements ICodegen, ICodegenBlock {

	private HashListMap<Object> symbolList = new HashListMap<Object>();
	private HashListMapEx<String, Function> funcMap = new HashListMapEx<String, Function>();
	private CodegenData data = new CodegenData();
	private RuntimeDebugInfo info = new RuntimeDebugInfo();

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
		genCode(RuntimeInst.ipush, 0);
		genCode(RuntimeInst.icall);
		genCode(RuntimeInst.ipop);
		genCode(RuntimeInst.ihalt);
		genCode(RuntimeInst.inop);
		for (Function func : funcMap.list) {
			if (func.isExtern()) {
				info.addExports(func.getRealName(), data.getCodeIndex());
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
		ArrayList<Object> objs = new ArrayList<Object>();
		for (Object token : symbolList.list) {
			objs.add(token);
		}
		ArrayList<Integer> insts = new ArrayList<Integer>();
		for (RuntimeInstUnary unary : data.callsToWriteBack) {
			unary.op1 = data.funcEntriesMap.get(funcMap.list.get(unary.op1)
					.getRealName());
		}
		for (RuntimeInstBase inst : data.insts) {
			inst.gen(insts);
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
		sb.append(System.getProperty("line.separator"));
		for (RuntimeInstBase inst : data.insts) {
			sb.append(inst.toString("\t"));
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return getCodeString();
	}
}
