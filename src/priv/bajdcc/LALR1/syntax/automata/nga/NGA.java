package priv.bajdcc.LALR1.syntax.automata.nga;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import priv.bajdcc.LALR1.syntax.ISyntaxComponent;
import priv.bajdcc.LALR1.syntax.ISyntaxComponentVisitor;
import priv.bajdcc.LALR1.syntax.Syntax;
import priv.bajdcc.LALR1.syntax.exp.BranchExp;
import priv.bajdcc.LALR1.syntax.exp.OptionExp;
import priv.bajdcc.LALR1.syntax.exp.PropertyExp;
import priv.bajdcc.LALR1.syntax.exp.RuleExp;
import priv.bajdcc.LALR1.syntax.exp.SequenceExp;
import priv.bajdcc.LALR1.syntax.exp.TokenExp;
import priv.bajdcc.LALR1.syntax.rule.RuleItem;
import priv.bajdcc.LALR1.syntax.stringify.NGAToString;
import priv.bajdcc.util.VisitBag;
import priv.bajdcc.util.lexer.automata.BreadthFirstSearch;

/**
 * <p>
 * <strong>非确定性文法自动机</strong>（<b>NGA</b>）构成算法（<b>AST->NGA</b>）
 * </p>
 * <i>功能：进行LR项目集的计算</i>
 * 
 * @author bajdcc
 *
 */
public class NGA implements ISyntaxComponentVisitor {

	/**
	 * 非终结符集合
	 */
	protected ArrayList<RuleExp> arrNonTerminals = null;

	/**
	 * 终结符集合
	 */
	protected ArrayList<TokenExp> arrTerminals = null;

	/**
	 * 规则到文法自动机状态的映射
	 */
	protected HashMap<RuleItem, NGAStatus> mapNGA = new HashMap<RuleItem, NGAStatus>();

	/**
	 * 保存结果的数据包
	 */
	private NGABag bag = null;

	public NGA(ArrayList<RuleExp> nonterminals, ArrayList<TokenExp> terminals) {
		arrNonTerminals = nonterminals;
		arrTerminals = terminals;
		generateNGAMap();
	}

	/**
	 * 连接两个状态
	 * 
	 * @param begin
	 *            初态
	 * @param end
	 *            终态
	 * @return 新的边
	 */
	protected NGAEdge connect(NGAStatus begin, NGAStatus end) {
		NGAEdge edge = new NGAEdge();// 申请一条新边
		edge.begin = begin;
		edge.end = end;
		begin.outEdges.add(edge);// 添加进起始边的出边
		end.inEdges.add(edge);// 添加进结束边的入边
		return edge;
	}

	/**
	 * 断开某个状态和某条边
	 * 
	 * @param status
	 *            某状态
	 * @param edge
	 *            某条边
	 */
	protected void disconnect(NGAStatus status, NGAEdge edge) {
		edge.end.inEdges.remove(edge);// 当前边的结束状态的入边集合去除当前边
	}

	/**
	 * 断开某个状态和所有边
	 * 
	 * @param begin
	 *            某状态
	 */
	protected void disconnect(NGAStatus status) {
		/* 清除所有入边 */
		for (Iterator<NGAEdge> it = status.inEdges.iterator(); it.hasNext();) {
			NGAEdge edge = it.next();
			it.remove();
			disconnect(edge.begin, edge);
		}
		/* 清除所有出边 */
		for (Iterator<NGAEdge> it = status.outEdges.iterator(); it.hasNext();) {
			NGAEdge edge = it.next();
			it.remove();
			disconnect(status, edge);
		}
	}

	/**
	 * 产生NGA映射表
	 */
	private void generateNGAMap() {
		int j = 0;
		for (RuleExp exp : arrNonTerminals) {
			j++;
			int i = 0;
			j = j + i - i;
			for (RuleItem item : exp.rule.arrRules) {
				/* 表达式转换成NGA */
				bag = new NGABag();
				bag.expression = item.expression;
				bag.prefix = exp.name + "[" + i + "]";
				bag.expression.visit(this);
				ENGA enga = bag.nga;
				/* NGA去Epsilon边 */
				NGAStatus status = deleteEpsilon(enga);
				/* 保存 */
				mapNGA.put(item, status);
				i++;
			}
		}
	}

	/**
	 * NGA去Epsilon边（与DFA去E边算法相似）
	 * 
	 * @param enga
	 *            ENGA
	 * @return NGA状态
	 */
	private NGAStatus deleteEpsilon(ENGA enga) {
		/* 获取状态闭包 */
		ArrayList<NGAStatus> NGAStatusList = getNGAStatusClosure(
				new BreadthFirstSearch<NGAEdge, NGAStatus>(), enga.begin);
		/* 可到达状态集合 */
		ArrayList<NGAStatus> availableStatus = new ArrayList<NGAStatus>();
		/* 可到达标签集合 */
		ArrayList<String> availableLabels = new ArrayList<String>();
		/* 可到达标签集哈希表（用于查找） */
		HashSet<String> availableLabelsSet = new HashSet<String>();
		/* 搜索所有有效状态 */
		availableStatus.add(NGAStatusList.get(0));
		availableLabels.add(NGAStatusList.get(0).data.label);
		availableLabelsSet.add(NGAStatusList.get(0).data.label);
		for (NGAStatus status : NGAStatusList) {
			if (status == NGAStatusList.get(0)) {// 排除第一个
				continue;
			}
			boolean available = false;
			for (NGAEdge edge : status.inEdges) {
				if (edge.data.kAction != NGAEdgeType.EPSILON) {// 不是Epsilon边
					available = true;// 当前可到达
					break;
				}
			}
			if (available
					&& !availableLabelsSet.contains(status.data.label)) {
				availableStatus.add(status);
				availableLabels.add(status.data.label);
				availableLabelsSet.add(status.data.label);
			}
		}
		BreadthFirstSearch<NGAEdge, NGAStatus> epsilonBFS = new BreadthFirstSearch<NGAEdge, NGAStatus>() {
			@Override
			public boolean testEdge(NGAEdge edge) {
				return edge.data.kAction == NGAEdgeType.EPSILON;
			}
		};
		/* 遍历所有有效状态 */
		for (NGAStatus status : availableStatus) {
			/* 获取当前状态的Epsilon闭包 */
			ArrayList<NGAStatus> epsilonClosure = getNGAStatusClosure(
					epsilonBFS, status);
			/* 去除自身状态 */
			epsilonClosure.remove(status);
			/* 遍历Epsilon闭包的状态 */
			for (NGAStatus epsilonStatus : epsilonClosure) {
				if (epsilonStatus.data.bFinal) {
					/* 如果闭包中有终态，则当前状态为终态 */
					status.data.bFinal = true;
				}
				/* 遍历闭包中所有边 */
				for (NGAEdge edge : epsilonStatus.outEdges) {
					if (edge.data.kAction != NGAEdgeType.EPSILON) {
						/* 获得索引 */
						int idx = availableLabels
								.indexOf(edge.end.data.label);
						/* 如果当前边不是Epsilon边，就将闭包中的有效边添加到当前状态 */
						connect(status, availableStatus.get(idx)).data = edge.data;
					}
				}
			}
		}
		/* 删除Epsilon边 */
		for (NGAStatus status : NGAStatusList) {
			for (Iterator<NGAEdge> it = status.outEdges.iterator(); it
					.hasNext();) {
				NGAEdge edge = it.next();
				if (edge.data.kAction == NGAEdgeType.EPSILON) {
					it.remove();
					disconnect(status, edge);// 删除Epsilon边
				}
			}
		}
		/* 删除无效状态 */
		ArrayList<NGAStatus> unaccessiableStatus = new ArrayList<NGAStatus>();
		for (NGAStatus status : NGAStatusList) {
			if (!availableStatus.contains(status)) {
				unaccessiableStatus.add(status);
			}
		}
		for (NGAStatus status : unaccessiableStatus) {
			NGAStatusList.remove(status);// 删除无效状态
			disconnect(status);// 删除与状态有关的所有边
		}
		return enga.begin;
	}

	/**
	 * 获取NGA状态闭包
	 * 
	 * @param bfs
	 *            遍历算法
	 * @param status
	 *            初态
	 * @return 初态闭包
	 */
	protected static ArrayList<NGAStatus> getNGAStatusClosure(
			BreadthFirstSearch<NGAEdge, NGAStatus> bfs, NGAStatus status) {
		status.visit(bfs);
		return bfs.arrStatus;
	}

	/**
	 * 开始遍历子结点
	 */
	private void beginChilren() {
		bag.childNGA = null;
		bag.stkNGA.push(new ArrayList<ENGA>());
	}

	/**
	 * 结束遍历子结点
	 */
	private void endChilren() {
		bag.childNGA = bag.stkNGA.pop();
	}

	/**
	 * 保存结果
	 * 
	 * @param enpa
	 *            EpsilonNGA
	 */
	private void store(ENGA enga) {
		if (bag.stkNGA.isEmpty()) {
			enga.end.data.bFinal = true;
			bag.nga = enga;
		} else {
			bag.stkNGA.peek().add(enga);
		}
	}

	@Override
	public void visitBegin(TokenExp node, VisitBag bag) {

	}

	@Override
	public void visitBegin(RuleExp node, VisitBag bag) {

	}

	@Override
	public void visitBegin(SequenceExp node, VisitBag bag) {
		beginChilren();
	}

	@Override
	public void visitBegin(BranchExp node, VisitBag bag) {
		beginChilren();
	}

	@Override
	public void visitBegin(OptionExp node, VisitBag bag) {
		beginChilren();
	}

	@Override
	public void visitBegin(PropertyExp node, VisitBag bag) {
		beginChilren();
	}

	@Override
	public void visitEnd(TokenExp node) {
		/* 新建ENGA */
		ENGA enga = createENGA(node);
		/* 连接ENGA边并保存当前结点 */
		NGAEdge edge = connect(enga.begin, enga.end);
		edge.data.kAction = NGAEdgeType.TOKEN;
		edge.data.token = node;
		store(enga);
	}

	@Override
	public void visitEnd(RuleExp node) {
		/* 新建ENGA */
		ENGA enga = createENGA(node);
		/* 连接ENGA边并保存当前结点 */
		NGAEdge edge = connect(enga.begin, enga.end);
		edge.data.kAction = NGAEdgeType.RULE;
		edge.data.rule = node;
		store(enga);
	}

	@Override
	public void visitEnd(SequenceExp node) {
		endChilren();
		/* 串联 */
		ENGA enga = new ENGA();
		for (ENGA child : bag.childNGA) {
			if (enga.begin != null) {
				connect(enga.end, child.begin);// 首尾相连
				enga.end = child.end;
			} else {
				enga.begin = bag.childNGA.get(0).begin;
				enga.end = bag.childNGA.get(0).end;
			}
		}
		store(enga);
	}

	@Override
	public void visitEnd(BranchExp node) {
		endChilren();
		/* 新建ENGA */
		ENGA enga = createENGA(node);
		/* 并联 */
		for (ENGA child : bag.childNGA) {
			/* 复制标签 */
			child.begin.data.label = enga.begin.data.label;
			child.end.data.label = enga.end.data.label;
			/* 连接首尾 */
			connect(enga.begin, child.begin);
			connect(child.end, enga.end);
		}
		store(enga);
	}

	@Override
	public void visitEnd(OptionExp node) {
		endChilren();
		/* 获得唯一的一个子结点 */
		ENGA enga = bag.childNGA.get(0);
		enga.begin.data.label = Syntax.getSingleString(
				bag.prefix, bag.expression, node, true);
		enga.end.data.label = Syntax.getSingleString(
				bag.prefix, bag.expression, node, false);
		/* 添加可选边，即Epsilon边 */
		connect(enga.begin, enga.end);
		store(enga);
	}

	@Override
	public void visitEnd(PropertyExp node) {
		endChilren();
		/* 获得唯一的一个子结点 */
		ENGA enga = bag.childNGA.get(0);
		enga.begin.data.label = Syntax.getSingleString(
				bag.prefix, bag.expression, node, true);
		enga.end.data.label = Syntax.getSingleString(
				bag.prefix, bag.expression, node, false);
		/* 获得该结点的边 */
		NGAEdge edge = enga.begin.outEdges.get(0);
		edge.data.iStorage = node.iStorage;
		edge.data.handler = node.errorHandler;
		edge.data.action = node.actionHandler;
		store(enga);
	}

	/**
	 * 新建ENGA
	 * 
	 * @param node
	 *            结点
	 * @return ENGA边
	 */
	private ENGA createENGA(ISyntaxComponent node) {
		ENGA enga = new ENGA();
		enga.begin = new NGAStatus();
		enga.end = new NGAStatus();
		enga.begin.data.label = Syntax.getSingleString(
				bag.prefix, bag.expression, node, true);
		enga.end.data.label = Syntax.getSingleString(
				bag.prefix, bag.expression, node, false);
		return enga;
	}

	/**
	 * 非确定性文法自动机描述
	 */
	public String getNGAString() {
		StringBuffer sb = new StringBuffer();
		sb.append("#### 产生式 ####");
		sb.append(System.lineSeparator());
		for (NGAStatus status : mapNGA.values()) {
			sb.append(getNGAString(status, ""));
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	/**
	 * 非确定性文法自动机描述
	 * 
	 * @param status
	 *            NGA状态
	 * @param prefix
	 *            前缀
	 * @return 描述
	 */
	public String getNGAString(NGAStatus status, String prefix) {
		NGAToString alg = new NGAToString(prefix);
		status.visit(alg);
		return alg.toString();
	}

	@Override
	public String toString() {
		return getNGAString();
	}
}
