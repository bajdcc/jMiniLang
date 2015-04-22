package priv.bajdcc.LALR1.syntax.token;

/**
 * 操作符
 * 
 * @author bajdcc
 */
public enum OperatorType {
	INFER("->"), ALTERNATIVE("|"), LPARAN("("), RPARAN(")"), LBRACE("{"), RBRACE(
			"}"), LSQUARE("["), RSQUARE("]"), LT("<"), GT(">");

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
