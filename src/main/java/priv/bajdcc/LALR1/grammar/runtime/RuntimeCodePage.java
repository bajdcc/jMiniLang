package priv.bajdcc.LALR1.grammar.runtime;

import priv.bajdcc.util.intervalTree.Interval;
import priv.bajdcc.util.intervalTree.IntervalTree;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

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
	private List<Object> data = null;

	/**
	 * 指令
	 */
	private List<Byte> insts = null;

	/**
	 * 调试开发
	 */
	private IRuntimeDebugInfo info = null;

	/**
	 * 汇编调试的符号表
	 */
	private List<Interval<Object>> itvList = null;

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

	public Object getDebugInfoByInc(long index) {
		if (tree == null)
			tree = new IntervalTree<>(itvList);
		List list = tree.get(index);
		if (list.isEmpty())
			return "[NO DEBUG INFO]";
		return String.join("\n", list);
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
