package priv.bajdcc.util.lexer.automata;

import java.util.ArrayList;

import priv.bajdcc.util.VisitBag;

/**
 * 宽度优先搜索
 * 
 * @author bajdcc 状态类型
 */
public class BreadthFirstSearch<Edge, Status> implements
		IBreadthFirstSearch<Edge, Status> {

	/**
	 * 存放状态的集合
	 */
	public ArrayList<Status> arrStatus = new ArrayList<>();

	@Override
	public boolean testEdge(Edge edge) {
		return true;
	}

	@Override
	public void visitBegin(Status status, VisitBag bag) {

	}

	@Override
	public void visitEnd(Status status) {

	}
}
