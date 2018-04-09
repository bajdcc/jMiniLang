package priv.bajdcc.OP.syntax.token;

/**
 * 算符优先关系类型
 *
 * @author bajdcc
 */
public enum PrecedenceType {
	LT("<"), GT(">"), EQ("="), NULL("-");

	private String name;

	PrecedenceType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}