package priv.bajdcc.syntax.automata.npa;

/**
 * 非确定性下推自动机指令
 *
 * @author bajdcc
 */
public enum NPAInstruction {
	PASS("通过"), READ("读入"), SHIFT("移进"), TRANSLATE("归约"), LEFT_RECURSION(
			"左递归"), TRANSLATE_DISCARD("归约"), LEFT_RECURSION_DISCARD("左递归"), TRANSLATE_FINISH(
			"归约结束");

	private String name;

	NPAInstruction(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
