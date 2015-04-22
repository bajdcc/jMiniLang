package priv.bajdcc.util.lexer.automata.nfa;

import priv.bajdcc.util.lexer.automata.EdgeType;

/**
 * NFA边数据
 * 
 * @author bajdcc
 *
 */
public class NFAEdgeData {
	/**
	 * 边类型
	 */
	public EdgeType kAction = EdgeType.EPSILON;
	
	/**
	 * 数据
	 */
	public int param = -1;
}
