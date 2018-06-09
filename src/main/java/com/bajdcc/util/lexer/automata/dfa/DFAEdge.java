package com.bajdcc.util.lexer.automata.dfa;

/**
 * DFA边
 *
 * @author bajdcc
 */
public class DFAEdge {
	/**
	 * 初态
	 */
	public DFAStatus begin;

	/**
	 * 终态
	 */
	public DFAStatus end;

	/**
	 * 数据
	 */
	public DFAEdgeData data = new DFAEdgeData();
}