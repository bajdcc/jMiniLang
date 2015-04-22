package priv.bajdcc.LALR1.syntax.automata.npa;

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
	public NPAStatus begin;

	/**
	 * 终态
	 */
	public NPAStatus end;

	/**
	 * 数据
	 */
	public NPAEdgeData data = new NPAEdgeData();
}
