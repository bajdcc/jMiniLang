package priv.bajdcc.util.lexer.token;

/**
 * 单词类型
 * 
 * @author bajdcc
 */
public enum TokenType {
	KEYWORD("关键字"), ID("标识符"), WHITESPACE("空白字符"), CHARACTER("字符"), STRING(
			"字符串"), BOOL("布尔"), INTEGER("整数"), POINTER("指针"), DECIMAL("实数"), EOF("结束符"), COMMENT(
			"注释"), OPERATOR("操作符"), MACRO("宏"), ERROR("错误"), RESERVE("保留");

	private String name;

	TokenType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
