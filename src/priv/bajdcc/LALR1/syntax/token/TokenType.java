package priv.bajdcc.LALR1.syntax.token;

/**
 * 单词类型
 * 
 * @author bajdcc
 */
public enum TokenType {
	TERMINAL("终结符"), NONTERMINAL("非终结符"), STORAGE("存储序号"), EOF("全文末尾"), COMMENT(
			"注释"), OPERATOR("操作符"), WHITSPACE("空白字符"), HANDLER("错误处理器"), ACTION("语义动作"), ERROR(
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
};
