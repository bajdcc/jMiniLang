package com.bajdcc.LALR1.grammar.runtime;

import com.bajdcc.util.intervalTree.Interval;
import com.bajdcc.util.intervalTree.IntervalTree;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 【目标代码】代码页
 *
 * @author bajdcc
 */
public class RuntimeCodePage implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 数据
	 */
	private List<Object> data;

	/**
	 * 指令
	 */
	private List<Byte> insts;

	/**
	 * 调试开发
	 */
	private IRuntimeDebugInfo info;

	/**
	 * 汇编调试的符号表
	 */
	private List<Interval<Object>> itvList;

	private transient IntervalTree<Object> tree = null;

	public RuntimeCodePage(List<Object> data, List<Byte> insts,
	                       IRuntimeDebugInfo info, List<Interval<Object>> itvList) {
		this.data = data;
		this.insts = insts;
		this.info = info;
		this.itvList = itvList;
	}

	public static RuntimeCodePage importFromStream(InputStream input) {
		return ObjectTools.deserialize(input);
	}

	public static void exportFromStream(RuntimeCodePage page,
	                                    OutputStream output) {
		ObjectTools.serialize(page, output);
	}

	public List<Object> getData() {
		return data;
	}

	public List<Byte> getInsts() {
		return insts;
	}

	public String getDebugInfoByInc(long index) {
		if (tree == null)
			tree = new IntervalTree<>(itvList);
		List<Object> list = tree.get(index);
		if (list.isEmpty())
			return "[NO DEBUG INFO]";
		return list.stream().map(String::valueOf).map(a -> "---=== 代码片段 ===---" + (System.lineSeparator() + a)).collect(Collectors.joining(System.lineSeparator()));
	}

	public IRuntimeDebugInfo getInfo() {
		return info;
	}

	public String getCodeString() {
		return "#### 目标代码 ####" +
				System.lineSeparator() +
				insts +
				System.lineSeparator();
	}

	@Override
	public String toString() {
		return getCodeString();
	}
}
