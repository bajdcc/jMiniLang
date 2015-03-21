package priv.bajdcc.lexer.token;

/**
 * ²Ù×÷·û
 * 
 * @author bajdcc
 */
public enum OperatorType {
	ASSIGN("="), PLUS("+"), MINUS("-"), TIMES("*"), DIV("/"), MOD("%"), BIT_AND(
			"&"), BIT_OR("|"), BIT_NOT("~"), LOG_NOT("!"), LT("<"), GT(">"),

	LPARAN("("), RPARAN(")"), LBRACE("{"), RBRACE("}"), LSQUARE("["), RSQUARE(
			"]"), COMMA(","), DOT("."), SEMI(";"), COLON(":"),

	EQ("=="), NEQ("!="), PLUS_PLUS("++"), MINUS_MINUS("--"), PLUS_ASSIGN("+="), MINUS_ASSIGN(
			"-="), TIMES_ASSIGN("*="), DIV_ASSIGN("/="), NGT("<="), NLT(">="), LOGICAL_AND(
			"&&"), LOGICAL_OR("||");

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
