package priv.bajdcc.LALR1.grammar.codegen;

import java.util.ArrayList;
import java.util.HashMap;

import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInstBase;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInstUnary;

/**
 * 【中间代码】数据结构
 *
 * @author bajdcc
 */
public class CodegenData {

	public ArrayList<RuntimeInstBase> insts = new ArrayList<RuntimeInstBase>();
	public HashMap<String, Integer> funcEntriesMap = new HashMap<String, Integer>();
	public ArrayList<RuntimeInstUnary> callsToWriteBack = new ArrayList<RuntimeInstUnary>();
	private int idxCode = 0;

	public void pushCode(RuntimeInstBase code) {
		checkWriteBackInst(code);
		insts.add(code);
		idxCode += code.getAdvanceLength();
	}

	private void checkWriteBackInst(RuntimeInstBase code) {
		if (code.inst == RuntimeInst.ildfun || code.inst == RuntimeInst.icall) {
			callsToWriteBack
					.add((RuntimeInstUnary) insts.get(insts.size() - 1));
		}
	}

	public void pushFuncEntry(String name) {
		funcEntriesMap.put(name, idxCode);
	}

	public int getCodeIndex() {
		return idxCode;
	}
}
