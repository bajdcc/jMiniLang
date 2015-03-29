package priv.bajdcc.syntax.automata.npa;

/**
 * 非确定性下推自动机边
 * 
 * @author bajdcc
 *
 */
public class NPAEdge {
	/**
	 * 初态
	 */
	public NPAStatus m_Begin;

	/**
	 * 终态
	 */
	public NPAStatus m_End;

	/**
	 * 数据
	 */
	public NPAEdgeData m_Data = new NPAEdgeData();
}
