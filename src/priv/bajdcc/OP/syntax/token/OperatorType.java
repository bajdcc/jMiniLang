package priv.bajdcc.OP.syntax.token;

/**
 * 操作符
 * 
 * @author bajdcc
 */
public enum OperatorType {
	INFER("->"), ALTERNATIVE("|"), LT("<"), GT(">");

	private String name;

	OperatorType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
