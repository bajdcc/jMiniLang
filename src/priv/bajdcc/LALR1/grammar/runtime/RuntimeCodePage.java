package priv.bajdcc.LALR1.grammar.runtime;

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

	public RuntimeCodePage(List<Object> data, List<Byte> insts,
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

	public List<Object> getData() {
		return data;
	}

	public List<Byte> getInsts() {
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
