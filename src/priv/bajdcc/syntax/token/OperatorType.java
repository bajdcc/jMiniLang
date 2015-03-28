package priv.bajdcc.syntax.token;

/**
 * ²Ù×÷·û
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
