package com.bajdcc.LALR1.syntax.automata.nga;

import com.bajdcc.util.VisitBag;
import com.bajdcc.util.lexer.automata.BreadthFirstSearch;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * 非确定性文法自动机状态
 *
 * @author bajdcc
 */
public class NGAStatus {
	/**
	 * 出边集合
	 */
	public ArrayList<NGAEdge> outEdges = new ArrayList<>();

	/**
	 * 入边集合
	 */
	public ArrayList<NGAEdge> inEdges = new ArrayList<>();

	/**
	 * 数据
	 */
	public NGAStatusData data = new NGAStatusData();

	/**
	 * 用于遍历包括该状态在内的所有状态（连通），结果存放在PATH中
	 *
	 * @param bfs 遍历算法
	 */
	public void visit(BreadthFirstSearch<NGAEdge, NGAStatus> bfs) {
		ArrayList<NGAStatus> stack = bfs.arrStatus;
		HashSet<NGAStatus> set = new HashSet<>();
		stack.clear();
		set.add(this);
		stack.add(this);
		for (int i = 0; i < stack.size(); i++) {// 遍历每个状态
			NGAStatus status = stack.get(i);
			VisitBag bag = new VisitBag();
			bfs.visitBegin(status, bag);
			if (bag.getVisitChildren()) {
				// 遍历状态的出边
// 边未被访问，且边类型符合要求
				status.outEdges.stream().filter(edge -> !set.contains(edge.end) && bfs.testEdge(edge)).forEach(edge -> {// 边未被访问，且边类型符合要求
					stack.add(edge.end);
					set.add(edge.end);
				});
			}
			if (bag.getVisitEnd()) {
				bfs.visitEnd(status);
			}
		}
	}
}
