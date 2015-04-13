package priv.bajdcc.lexer.automata.dfa;

import java.util.ArrayList;
import java.util.HashSet;

import priv.bajdcc.lexer.automata.BreadthFirstSearch;
import priv.bajdcc.utility.VisitBag;

/**
 * DFA状态
 * 
 * @author bajdcc
 *
 */
public class DFAStatus {
	/**
	 * 出边集合
	 */
	public HashSet<DFAEdge> outEdges = new HashSet<DFAEdge>();

	/**
	 * 入边集合
	 */
	public HashSet<DFAEdge> inEdges = new HashSet<DFAEdge>();

	/**
	 * 数据
	 */
	public DFAStatusData data = new DFAStatusData();

	/**
	 * 用于遍历包括该状态在内的所有状态（连通），结果存放在PATH中
	 * 
	 * @param bfs
	 *            遍历算法
	 */
	public void visit(BreadthFirstSearch<DFAEdge, DFAStatus> bfs) {
		ArrayList<DFAStatus> stack = bfs.arrStatus;
		HashSet<DFAStatus> set = new HashSet<DFAStatus>();
		stack.clear();
		set.add(this);
		stack.add(this);
		for (int i = 0; i < stack.size(); i++) {// 遍历每个状态
			DFAStatus status = stack.get(i);
			VisitBag bag = new VisitBag();
			bfs.visitBegin(status, bag);
			if (bag.bVisitChildren) {
				for (DFAEdge edge : status.outEdges) {// 遍历状态的出边
					if (!set.contains(edge.end) && bfs.testEdge(edge)) {// 边未被访问，且边类型符合要求
						stack.add(edge.end);
						set.add(edge.end);
					}
				}
			}
			if (bag.bVisitEnd) {
				bfs.visitEnd(status);
			}
		}
	}
}
