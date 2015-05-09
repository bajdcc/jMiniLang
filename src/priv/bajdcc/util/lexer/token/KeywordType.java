package priv.bajdcc.util.lexer.token;

/**
 * 关键词
 * 
 * @author bajdcc
 */
public enum KeywordType {
	AUTO("auto"), BOOL("bool"), BREAK("break"), CASE("case"), CHAR("char"), CONST(
			"const"), CONTINUE("continue"), DEFAULT("default"), DO("do"), DOUBLE(
			"double"), ELSE("else"), ENUM("enum"), EXTERN("extern"), FALSE(
			"false"), FLOAT("float"), FOR("for"), GOTO("goto"), IF("if"), INT(
			"int"), LONG("long"), REGISTER("register"), RETURN("return"), SHORT(
			"short"), SIGNED("signed"), SIZEOF("sizeof"), STATIC("static"), STRUCT(
			"struct"), SWITCH("switch"), TRUE("true"), TYPEDEF("typedef"), UNION(
			"union"), UNSIGNED("unsigned"), VOID("void"), VOLATILE("volatile"), WHILE(
			"while"), VARIABLE("var"), FUNCTION("func"), LET("let"), CALL("call"), REF("ref");

	private String name;

	KeywordType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
};
