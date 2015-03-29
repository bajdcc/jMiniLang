package priv.bajdcc.syntax.automata.npa;

/**
 * 非确定性下推自动机指令
 *
 * @author bajdcc
 */
public enum NPAInstruction {
	PASS("通过"), READ("读入"), SHIFT("移进"), TRANSLATE("翻译"), LEFT_RECURSION("左递归"), TRANSLATE_DISCARD(
			"丢弃翻译"), LEFT_RECURSION_DISCARD("丢弃左递归"), TRANSLATE_FINISH("翻译结束");

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
