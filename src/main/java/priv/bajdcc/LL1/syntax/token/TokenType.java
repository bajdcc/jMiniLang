package priv.bajdcc.LL1.syntax.token;

/**
 * 单词类型
 *
 * @author bajdcc
 */
public enum TokenType {
	TERMINAL("终结符"), NONTERMINAL("非终结符"), EOF("全文末尾"), COMMENT(
			"注释"), OPERATOR("操作符"), WHITSPACE("空白字符"), ERROR(
			"错误");

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
