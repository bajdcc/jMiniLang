package priv.bajdcc.lexer.automata.dfa;

/**
 * DFA±ß
 * 
 * @author bajdcc
 *
 */
public class DFAEdge {
	/**
	 * ³õÌ¬
	 */
	public DFAStatus m_Begin;

	/**
	 * ÖÕÌ¬
	 */
	public DFAStatus m_End;

	/**
	 * Êý¾Ý
	 */
	public DFAEdgeData m_Data = new DFAEdgeData();
}