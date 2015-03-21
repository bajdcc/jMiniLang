package priv.bajdcc.lexer.token;

/**
 * ¹Ø¼ü´Ê
 * 
 * @author bajdcc
 */
public enum KeywordType {
	AUTO("auto"), BOOL("bool"), BREAK("break"), CASE("case"), CHAR("char"), CLASS(
			"class"), CONST("const"), CONTINUE("continue"), DEFAULT("default"), DELETE(
			"delete"), DO("do"), DOUBLE("double"), ELSE("else"), ENUM("enum"), EXTERN(
			"extern"), FALSE("false"), FLOAT("float"), FOR("for"), GOTO("goto"), IF(
			"if"), INT("int"), LONG("long"), MUTABLE("mutable"), NAMESPACE(
			"namespace"), NEW("new"), PRIVATE("private"), PROTECTED("protected"), PUBLIC(
			"public"), REGISTER("register"), RETURN("return"), SIGNED("signed"), SIZEOF(
			"sizeof"), STATIC("static"), STRUCT("struct"), SWITCH("switch"), TRUE(
			"true"), TYPEDEF("typedef"), UNION("union"), UNSIGNED("unsigned"), USING(
			"using"), VAR("var"), VOID("void"), VOLATILE("volatile"), WHILE(
			"while");

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
