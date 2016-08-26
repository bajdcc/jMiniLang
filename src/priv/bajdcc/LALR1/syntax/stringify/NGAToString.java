package priv.bajdcc.LALR1.syntax.stringify;

import java.util.ArrayList;

import priv.bajdcc.LALR1.syntax.automata.nga.NGAEdge;
import priv.bajdcc.LALR1.syntax.automata.nga.NGAStatus;
import priv.bajdcc.util.VisitBag;
import priv.bajdcc.util.lexer.automata.BreadthFirstSearch;

/**
 * NGA序列化（宽度优先搜索）
 * 
 * @author bajdcc 状态类型
 */
public class NGAToString extends BreadthFirstSearch<NGAEdge, NGAStatus> {

	/**
	 * 描述
	 */
	private StringBuilder context = new StringBuilder();

	/**
	 * 前缀
	 */
	private String prefix = "";

	/**
	 * 存放状态的集合
	 */
	private ArrayList<NGAStatus> arrNGAStatus = new ArrayList<NGAStatus>();

	public NGAToString() {

	}

	public NGAToString(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public void visitBegin(NGAStatus status, VisitBag bag) {
		/* 若首次访问节点则先构造状态表 */
		if (arrNGAStatus.isEmpty()) {
			BreadthFirstSearch<NGAEdge, NGAStatus> bfs = new BreadthFirstSearch<NGAEdge, NGAStatus>();
			status.visit(bfs);
			arrNGAStatus = bfs.arrStatus;
		}
		/* 输出状态标签 */
		appendLine();
		appendPrefix();
		context.append("--== 状态[" + arrNGAStatus.indexOf(status) + "]"
				+ (status.data.bFinal ? "[结束]" : "") + " ==--");
		appendLine();
		appendPrefix();
		context.append("项目： " + status.data.label);
		appendLine();
		/* 输出边 */
		for (NGAEdge edge : status.outEdges) {
			appendPrefix();
			context.append("\t到达 " + arrNGAStatus.indexOf(edge.end) + "  ：  ");
			context.append(edge.data.kAction.getName());
			switch (edge.data.kAction) {
			case EPSILON:
				break;
			case RULE:
				context.append(" = " + edge.data.rule);
				break;
			case TOKEN:
				context.append(" = " + edge.data.token);
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
		context.append(prefix);
	}

	/**
	 * 添加行
	 */
	private void appendLine() {
		context.append(System.lineSeparator());
	}

	@Override
	public String toString() {
		return context.toString();
	}
}
