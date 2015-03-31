package priv.bajdcc.lexer.automata.dfa;

/**
 * DFA边
 * 
 * @author bajdcc
 *
 */
public class DFAEdge {
	/**
	 * 初态
	 */
	public DFAStatus m_Begin;

	/**
	 * 终态
	 */
	public DFAStatus m_End;

	/**
	 * 数据
	 */
	public DFAEdgeData m_Data = new DFAEdgeData();
}