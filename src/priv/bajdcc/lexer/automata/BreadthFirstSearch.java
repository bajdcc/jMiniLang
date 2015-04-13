package priv.bajdcc.lexer.automata;

import java.util.ArrayList;

import priv.bajdcc.utility.VisitBag;

/**
 * 宽度优先搜索
 * 
 * @author bajdcc
 * @param T
 *            状态类型
 */
public class BreadthFirstSearch<Edge, Status> implements
		IBreadthFirstSearch<Edge, Status> {

	/**
	 * 存放状态的集合
	 */
	public ArrayList<Status> arrStatus = new ArrayList<Status>();

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
