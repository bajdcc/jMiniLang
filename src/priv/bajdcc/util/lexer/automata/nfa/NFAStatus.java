package priv.bajdcc.util.lexer.automata.nfa;

import java.util.ArrayList;
import java.util.HashSet;

import priv.bajdcc.util.VisitBag;
import priv.bajdcc.util.lexer.automata.BreadthFirstSearch;

/**
 * NFA状态
 * 
 * @author bajdcc
 *
 */
public class NFAStatus {
	/**
	 * 出边集合
	 */
	public ArrayList<NFAEdge> outEdges = new ArrayList<NFAEdge>();

	/**
	 * 入边集合
	 */
	public ArrayList<NFAEdge> inEdges = new ArrayList<NFAEdge>();

	/**
	 * 数据
	 */
	public NFAStatusData data = new NFAStatusData();

	/**
	 * 用于遍历包括该状态在内的所有状态（连通），结果存放在PATH中
	 * 
	 * @param bfs
	 *            遍历算法
	 */
	public void visit(BreadthFirstSearch<NFAEdge, NFAStatus> bfs) {
		ArrayList<NFAStatus> stack = bfs.arrStatus;
		HashSet<NFAStatus> set = new HashSet<NFAStatus>();
		stack.clear();
		set.add(this);
		stack.add(this);
		for (int i = 0; i < stack.size(); i++) {// 遍历每个状态
			NFAStatus status = stack.get(i);
			VisitBag bag = new VisitBag();
			bfs.visitBegin(status, bag);
			if (bag.bVisitChildren) {
				for (NFAEdge edge : status.outEdges) {// 遍历状态的出边
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
