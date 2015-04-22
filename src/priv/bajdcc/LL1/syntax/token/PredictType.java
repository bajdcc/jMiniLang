package priv.bajdcc.LL1.syntax.token;

/**
 * 预测分析指令类型
 * 
 * @author bajdcc
 */
public enum PredictType {
	TERMINAL("终结符"), NONTERMINAL("非终结符");

	private String name;

	PredictType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}