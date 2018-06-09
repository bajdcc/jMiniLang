package com.bajdcc.LALR1.syntax.automata.nga;

/**
 * 非确定性文法自动机边类型
 *
 * @author bajdcc
 */
public enum NGAEdgeType {
	EPSILON("Epsilon边"), TOKEN("终结符"), RULE("非终结符");

	private String name;

	NGAEdgeType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
