package priv.bajdcc.syntax.stringify;

import priv.bajdcc.lexer.automata.BreadthFirstSearch;
import priv.bajdcc.syntax.automata.nga.NGAEdge;
import priv.bajdcc.syntax.automata.nga.NGAStatus;
import priv.bajdcc.utility.VisitBag;

/**
 * NGA序列化（宽度优先搜索）
 * 
 * @author bajdcc
 * @param T
 *            状态类型
 */
public class NGAToString extends
		BreadthFirstSearch<NGAEdge, NGAStatus> {

	/**
	 * 描述
	 */
	private StringBuilder m_Context = new StringBuilder();

	/**
	 * 前缀
	 */
	private String m_Prefix = "";

	public NGAToString() {

	}

	public NGAToString(String prefix) {
		m_Prefix = prefix;
	}

	@Override
	public void visitBegin(NGAStatus status, VisitBag bag) {
		/* 若首次访问节点则先构造状态表 */
		if (m_arrStatus.isEmpty()) {
			BreadthFirstSearch<NGAEdge, NGAStatus> bfs = new BreadthFirstSearch<NGAEdge, NGAStatus>();
			status.visit(bfs);
			m_arrStatus = bfs.m_arrStatus;
		}
		/* 输出状态标签 */
		appendLine();
		appendPrefix();
		m_Context.append("--== 状态[" + m_arrStatus.indexOf(status) + "]"
				+ (status.m_Data.m_bFinal ? "[结束]" : "") + " ==--");
		appendLine();
		appendPrefix();
		m_Context.append("标签： " + status.m_Data.m_strLabel);
		appendLine();
		/* 输出边 */
		for (NGAEdge edge : status.m_OutEdges) {
			appendPrefix();
			m_Context.append("\t到达 " + m_arrStatus.indexOf(edge.m_End)
					+ "  ：  ");
			m_Context.append(edge.m_Data.m_Action.getName());
			switch (edge.m_Data.m_Action) {
			case EPSILON:
				break;
			case RULE:
				m_Context.append(" = " + edge.m_Data.m_Rule);
				break;
			case TOKEN:
				m_Context.append(" = " + edge.m_Data.m_Token);
				break;
			default:
				break;
			}
			appendLine();
		}
	}

	/**
	 * 添加前缀
	 */
	private void appendPrefix() {
		m_Context.append(m_Prefix);
	}

	/**
	 * 添加行
	 */
	private void appendLine() {
		m_Context.append(System.getProperty("line.separator"));
	}

	@Override
	public String toString() {
		return m_Context.toString();
	}
}
