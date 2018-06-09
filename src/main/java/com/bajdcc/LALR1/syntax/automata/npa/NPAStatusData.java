package com.bajdcc.LALR1.syntax.automata.npa;

/**
 * 非确定性下推自动机状态数据
 *
 * @author bajdcc
 */
public class NPAStatusData {
	/**
	 * 标签
	 */
	public String label = "";

	/**
	 * 状态所属规则的索引
	 */
	public int iRuleItem = -1;
}
