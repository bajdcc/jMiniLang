package priv.bajdcc.LALR1.grammar.runtime;

/**
 * 【运行时】运行时对象类型
 *
 * @author bajdcc
 */
public enum RuntimeObjectType {

	kNull("空"), kPtr("指针"), kObject("对象"), kInt("整数"), kReal("实数"), kChar("字符"), kString(
			"字符串"), kBool("布尔"), kFunc("函数"), kArray("数组"), kMap("字典"), kNan("非数");

	private String name;

	RuntimeObjectType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
