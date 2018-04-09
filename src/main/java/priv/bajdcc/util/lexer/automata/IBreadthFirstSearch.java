package priv.bajdcc.util.lexer.automata;

import priv.bajdcc.util.VisitBag;

/**
 * BFS宽度优先遍历接口
 *
 * @param <Edge>   边类型
 * @param <Status> 状态类型
 * @author bajdcc
 */
public interface IBreadthFirstSearch<Edge, Status> {
	/**
	 * 边测试
	 *
	 * @param edge 边
	 * @return 测试结果
	 */
	boolean testEdge(Edge edge);

	/**
	 * 遍历开始
	 *
	 * @param status 状态
	 * @param bag    遍历参数
	 */
	void visitBegin(Status status, VisitBag bag);

	/**
	 * 遍历结束
	 *
	 * @param status 状态
	 */
	void visitEnd(Status status);
}
