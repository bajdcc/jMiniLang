package priv.bajdcc.util.lexer.automata.dfa;

import priv.bajdcc.util.VisitBag;
import priv.bajdcc.util.lexer.automata.BreadthFirstSearch;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * DFA状态
 *
 * @author bajdcc
 */
public class DFAStatus {
	/**
	 * 出边集合
	 */
	public HashSet<DFAEdge> outEdges = new HashSet<>();

	/**
	 * 入边集合
	 */
	public HashSet<DFAEdge> inEdges = new HashSet<>();

	/**
	 * 数据
	 */
	public DFAStatusData data = new DFAStatusData();

	/**
	 * 用于遍历包括该状态在内的所有状态（连通），结果存放在PATH中
	 *
	 * @param bfs 遍历算法
	 */
	public void visit(BreadthFirstSearch<DFAEdge, DFAStatus> bfs) {
		ArrayList<DFAStatus> stack = bfs.arrStatus;
		HashSet<DFAStatus> set = new HashSet<>();
		stack.clear();
		set.add(this);
		stack.add(this);
		for (int i = 0; i < stack.size(); i++) {// 遍历每个状态
			DFAStatus status = stack.get(i);
			VisitBag bag = new VisitBag();
			bfs.visitBegin(status, bag);
			if (bag.bVisitChildren) {
				// 遍历状态的出边
// 边未被访问，且边类型符合要求
				status.outEdges.stream().filter(edge -> !set.contains(edge.end) && bfs.testEdge(edge)).forEach(edge -> {// 边未被访问，且边类型符合要求
					stack.add(edge.end);
					set.add(edge.end);
				});
			}
			if (bag.bVisitEnd) {
				bfs.visitEnd(status);
			}
		}
	}
}
