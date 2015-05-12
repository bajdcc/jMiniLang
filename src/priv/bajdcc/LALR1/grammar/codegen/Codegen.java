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
public class Codegen implements ICodegen {

	private HashListMap<Object> symbolList = new HashListMap<Object>();
	private HashListMapEx<String, Function> funcMap = new HashListMapEx<String, Function>();
	private CodegenData data = new CodegenData();
	private RuntimeDebugInfo info = new RuntimeDebugInfo();

	public static final int MAX_NOPCODE = 20;

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
	public void genCode(RuntimeInst inst) {
		data.pushCode(new RuntimeInstNon(inst));
	}

	@Override
	public void genCode(RuntimeInst inst, int op1) {
		data.pushCode(new RuntimeInstUnary(inst, op1));
	}

	@Override
	public void genCode(RuntimeInst inst, int op1, int op2) {
		data.pushCode(new RuntimeInstBinary(inst, op1, op2));
	}

	@Override
	public int genDataRef(Object object) {
		return symbolList.put(object);
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
