package priv.bajdcc.lexer.automata;

import java.util.ArrayList;

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
	public ArrayList<Status> m_Path = new ArrayList<Status>();

	@Override
	public boolean testEdge(Edge edge) {
		return true;
	}

	@Override
	public void visitBegin(Status status) {

	}

	@Override
	public void visitEnd(Status status) {

	}

}
