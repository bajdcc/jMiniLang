package priv.bajdcc.LALR1.grammar.runtime;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

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
	private ArrayList<Object> data = null;

	/**
	 * 指令
	 */
	private ArrayList<Integer> insts = null;

	/**
	 * 调试开发
	 */
	private IRuntimeDebugInfo info = null;

	public RuntimeCodePage(ArrayList<Object> data, ArrayList<Integer> insts,
			IRuntimeDebugInfo info) {
		this.data = data;
		this.insts = insts;
		this.info = info;
	}

	public static RuntimeCodePage importFromStream(InputStream input) {
		return ObjectTools.deserialize(input);
	}

	public static void exportFromStream(RuntimeCodePage page,
			OutputStream output) {
		ObjectTools.serialize(page, output);
	}

	public ArrayList<Object> getData() {
		return data;
	}

	public ArrayList<Integer> getInsts() {
		return insts;
	}

	public IRuntimeDebugInfo getInfo() {
		return info;
	}

	public String getCodeString() {
		StringBuilder sb = new StringBuilder();
		sb.append("#### 目标代码 ####");
		sb.append(System.getProperty("line.separator"));
		sb.append(insts);
		sb.append(System.getProperty("line.separator"));
		return sb.toString();
	}

	@Override
	public String toString() {
		return getCodeString();
	}
}
