package priv.bajdcc.lexer.token;

/**
 * 操作符
 * 
 * @author bajdcc
 */
public enum OperatorType {
	ASSIGN("="), PLUS("+"), MINUS("-"), TIMES("*"), DIVIDE("/"), MOD("%"), BIT_AND(
			"&"), BIT_OR("|"), BIT_NOT("~"), LOG_NOT("!"), LESS_THAN("<"), GREATER_THAN(
			">"),

	LPARAN("("), RPARAN(")"), LBRACE("{"), RBRACE("}"), LSQUARE("["), RSQUARE(
			"]"), COMMA(","), DOT("."), SEMI(";"), COLON(":"),

	EQUAL("=="), NOT_EQUAL("!="), PLUS_PLUS("++"), MINUS_MINUS("--"), PLUS_ASSIGN(
			"+="), MINUS_ASSIGN("-="), TIMES_ASSIGN("*="), DIV_ASSIGN("/="), LESS_THAN_OR_EQUAL(
			"<="), GREATER_THAN_OR_EQUAL(">="), LOGICAL_AND("&&"), LOGICAL_OR(
			"||");

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
