package priv.bajdcc.lexer.automata;

/**
 * 自动机边类型
 * 
 * @author bajdcc
 *
 */
public enum EdgeType {
	EPSILON("Epsilon边"),CHARSET("字符区间");

	private String name;

	EdgeType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
