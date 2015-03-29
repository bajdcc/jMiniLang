package priv.bajdcc.syntax.automata.npa;

import java.util.ArrayList;
import java.util.HashSet;

import priv.bajdcc.lexer.automata.BreadthFirstSearch;
import priv.bajdcc.utility.VisitBag;

/**
 * 非确定性下推自动机状态
 * 
 * @author bajdcc
 *
 */
public class NPAStatus {
	/**
	 * 出边集合
	 */
	public ArrayList<NPAEdge> m_OutEdges = new ArrayList<NPAEdge>();

	/**
	 * 入边集合
	 */
	public ArrayList<NPAEdge> m_InEdges = new ArrayList<NPAEdge>();

	/**
	 * 数据
	 */
	public NPAStatusData m_Data = new NPAStatusData();

	/**
	 * 用于遍历包括该状态在内的所有状态（连通），结果存放在PATH中
	 * 
	 * @param bfs
	 *            遍历算法
	 */
	public void visit(BreadthFirstSearch<NPAEdge, NPAStatus> bfs) {
		ArrayList<NPAStatus> stack = bfs.m_arrStatus;
		HashSet<NPAStatus> set = new HashSet<NPAStatus>();
		stack.clear();
		set.add(this);
		stack.add(this);
		for (int i = 0; i < stack.size(); i++) {// 遍历每个状态
			NPAStatus status = stack.get(i);
			VisitBag bag = new VisitBag();
			bfs.visitBegin(status, bag);
			if (bag.m_bVisitChildren) {
				for (NPAEdge edge : status.m_OutEdges) {// 遍历状态的出边
					if (!set.contains(edge.m_End) && bfs.testEdge(edge)) {// 边未被访问，且边类型符合要求
						stack.add(edge.m_End);
						set.add(edge.m_End);
					}
				}
			}
			if (bag.m_bVisitEnd) {
				bfs.visitEnd(status);
			}
		}
	}
}
