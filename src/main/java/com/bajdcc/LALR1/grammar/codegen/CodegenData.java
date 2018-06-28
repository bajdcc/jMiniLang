package com.bajdcc.LALR1.grammar.codegen;

import com.bajdcc.LALR1.grammar.runtime.RuntimeInstBase;
import com.bajdcc.LALR1.grammar.runtime.RuntimeInstUnary;

import java.util.*;

/**
 * 【中间代码】数据结构
 *
 * @author bajdcc
 */
public class CodegenData {

	public List<RuntimeInstBase> insts = new ArrayList<>();
	public Map<String, Integer> funcEntriesMap = new HashMap<>();
	public List<RuntimeInstUnary> callsToWriteBack = new ArrayList<>();
	private Stack<CodegenBlock> stkBlock = new Stack<>();
	private int idxCode = 0;

	public void pushCode(RuntimeInstBase code) {
		insts.add(code);
		idxCode += code.getAdvanceLength();
	}

	public void pushCodeWithFuncWriteBack(RuntimeInstBase code) {
		checkWriteBackInst(code);
		pushCode(code);
	}

	private void checkWriteBackInst(RuntimeInstBase code) {
		callsToWriteBack.add((RuntimeInstUnary) code);
	}

	public void pushFuncEntry(String name) {
		funcEntriesMap.put(name, idxCode);
	}

	public int getCodeIndex() {
		return idxCode;
	}

	public void enterBlockEntry(CodegenBlock block) {
		stkBlock.push(block);
	}

	public void leaveBlockEntry() {
		stkBlock.pop();
	}

	public CodegenBlock getBlock() {
		return stkBlock.peek();
	}

	public boolean hasBlock() {
		return !stkBlock.isEmpty();
	}
}
