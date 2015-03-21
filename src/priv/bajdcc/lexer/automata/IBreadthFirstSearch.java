package priv.bajdcc.lexer.automata;

/**
 * 
 * BFS宽度优先遍历接口
 * 
 * @param <Edge>
 *            边类型
 * @param <Status>
 *            状态类型
 * @author bajdcc
 *
 */
public interface IBreadthFirstSearch<Edge, Status> {
	/**
	 * 边测试
	 * 
	 * @return 测试结果
	 */
	public boolean testEdge(Edge edge);

	/**
	 * 遍历开始
	 * 
	 * @param status
	 *            状态
	 */
	public void visitBegin(Status status);

	/**
	 * 遍历结束
	 * 
	 * @param status
	 *            状态
	 */
	public void visitEnd(Status status);
}
