package priv.bajdcc.syntax.automata.nga;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.vibur.objectpool.ConcurrentLinkedPool;
import org.vibur.objectpool.PoolService;

import priv.bajdcc.lexer.automata.BreadthFirstSearch;
import priv.bajdcc.syntax.ISyntaxComponent;
import priv.bajdcc.syntax.ISyntaxComponentVisitor;
import priv.bajdcc.syntax.RuleItem;
import priv.bajdcc.syntax.Syntax;
import priv.bajdcc.syntax.exp.BranchExp;
import priv.bajdcc.syntax.exp.OptionExp;
import priv.bajdcc.syntax.exp.PropertyExp;
import priv.bajdcc.syntax.exp.RuleExp;
import priv.bajdcc.syntax.exp.SequenceExp;
import priv.bajdcc.syntax.exp.TokenExp;
import priv.bajdcc.syntax.stringify.NGAToString;
import priv.bajdcc.utility.ObjectFactory;
import priv.bajdcc.utility.VisitBag;

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
	protected ArrayList<RuleExp> m_arrNonTerminals = null;

	/**
	 * 非终结符集合
	 */
	protected HashMap<RuleItem, NGAStatus> m_mapNGA = new HashMap<RuleItem, NGAStatus>();

	/**
	 * 边对象池
	 */
	private PoolService<NGAEdge> m_EdgesPool = new ConcurrentLinkedPool<NGAEdge>(
			new ObjectFactory<NGAEdge>() {
				public NGAEdge create() {
					return new NGAEdge();
				};
			}, 1024, 10240, false);

	/**
	 * 状态对象池
	 */
	private PoolService<NGAStatus> m_StatusPool = new ConcurrentLinkedPool<NGAStatus>(
			new ObjectFactory<NGAStatus>() {
				public NGAStatus create() {
					return new NGAStatus();
				};
			}, 1024, 10240, false);

	/**
	 * 保存结果的数据包
	 */
	private NGABag m_Bag = null;

	public NGA(ArrayList<RuleExp> nonterminals) {
		m_arrNonTerminals = nonterminals;
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
		NGAEdge edge = m_EdgesPool.take();// 申请一条新边
		edge.m_Begin = begin;
		edge.m_End = end;
		begin.m_OutEdges.add(edge);// 添加进起始边的出边
		end.m_InEdges.add(edge);// 添加进结束边的入边
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
		edge.m_End.m_InEdges.remove(edge);// 当前边的结束状态的入边集合去除当前边
		m_EdgesPool.restore(edge);
	}

	/**
	 * 断开某个状态和所有边
	 * 
	 * @param begin
	 *            某状态
	 */
	protected void disconnect(NGAStatus status) {
		/* 清除所有入边 */
		for (Iterator<NGAEdge> it = status.m_InEdges.iterator(); it.hasNext();) {
			NGAEdge edge = it.next();
			it.remove();
			disconnect(edge.m_Begin, edge);
		}
		/* 清除所有出边 */
		for (Iterator<NGAEdge> it = status.m_OutEdges.iterator(); it.hasNext();) {
			NGAEdge edge = it.next();
			it.remove();
			disconnect(status, edge);
		}
		m_StatusPool.restore(status);
	}

	/**
	 * 产生NGA映射表
	 */
	private void generateNGAMap() {
		for (RuleExp exp : m_arrNonTerminals) {
			int i = 0;
			for (RuleItem item : exp.m_Rule.m_arrRules) {
				/* 表达式转换成NGA */
				m_Bag = new NGABag();
				m_Bag.m_Expression = item.m_Expression;
				m_Bag.m_strPrefix = exp.m_strName + "[" + i + "]";
				m_Bag.m_Expression.visit(this);
				ENGA enga = m_Bag.m_outputNGA;
				/* NGA去Epsilon边 */
				NGAStatus status = deleteEpsilon(enga);
				/* 保存 */
				m_mapNGA.put(item, status);
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
				new BreadthFirstSearch<NGAEdge, NGAStatus>(), enga.m_Begin);
		/* 可到达状态集合 */
		ArrayList<NGAStatus> availableStatus = new ArrayList<NGAStatus>();
		/* 可到达标签集合 */
		ArrayList<String> availableLabels = new ArrayList<String>();
		/* 可到达标签集哈希表（用于查找） */
		HashSet<String> availableLabelsSet = new HashSet<String>();
		/* 搜索所有有效状态 */
		availableStatus.add(NGAStatusList.get(0));
		availableLabels.add(NGAStatusList.get(0).m_Data.m_strLabel);
		availableLabelsSet.add(NGAStatusList.get(0).m_Data.m_strLabel);
		for (NGAStatus status : NGAStatusList) {
			if (status == NGAStatusList.get(0)) {// 排除第一个
				continue;
			}
			boolean available = false;
			for (NGAEdge edge : status.m_InEdges) {
				if (edge.m_Data.m_Action != NGAEdgeType.EPSILON) {// 不是Epsilon边
					available = true;// 当前可到达
					break;
				}
			}
			if (available
					&& !availableLabelsSet.contains(status.m_Data.m_strLabel)) {
				availableStatus.add(status);
				availableLabels.add(status.m_Data.m_strLabel);
				availableLabelsSet.add(status.m_Data.m_strLabel);
			}
		}
		BreadthFirstSearch<NGAEdge, NGAStatus> epsilonBFS = new BreadthFirstSearch<NGAEdge, NGAStatus>() {
			@Override
			public boolean testEdge(NGAEdge edge) {
				return edge.m_Data.m_Action == NGAEdgeType.EPSILON;
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
				if (epsilonStatus.m_Data.m_bFinal) {
					/* 如果闭包中有终态，则当前状态为终态 */
					status.m_Data.m_bFinal = true;
				}
				/* 遍历闭包中所有边 */
				for (NGAEdge edge : epsilonStatus.m_OutEdges) {
					if (edge.m_Data.m_Action != NGAEdgeType.EPSILON) {
						/* 获得索引 */
						int idx = availableLabels
								.indexOf(edge.m_End.m_Data.m_strLabel);
						/* 如果当前边不是Epsilon边，就将闭包中的有效边添加到当前状态 */
						connect(status, availableStatus.get(idx)).m_Data = edge.m_Data;
					}
				}
			}
		}
		/* 删除Epsilon边 */
		for (NGAStatus status : NGAStatusList) {
			for (Iterator<NGAEdge> it = status.m_OutEdges.iterator(); it
					.hasNext();) {
				NGAEdge edge = it.next();
				if (edge.m_Data.m_Action == NGAEdgeType.EPSILON) {
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
		return enga.m_Begin;
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
		return bfs.m_arrStatus;
	}

	/**
	 * 开始遍历子结点
	 */
	private void beginChilren() {
		m_Bag.m_childNGA = null;
		m_Bag.m_stkNGA.push(new ArrayList<ENGA>());
	}

	/**
	 * 结束遍历子结点
	 */
	private void endChilren() {
		m_Bag.m_childNGA = m_Bag.m_stkNGA.pop();
	}

	/**
	 * 保存结果
	 * 
	 * @param enpa
	 *            EpsilonNGA
	 */
	private void store(ENGA enga) {
		if (m_Bag.m_stkNGA.isEmpty()) {
			enga.m_End.m_Data.m_bFinal = true;
			m_Bag.m_outputNGA = enga;
		} else {
			m_Bag.m_stkNGA.peek().add(enga);
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
		NGAEdge edge = connect(enga.m_Begin, enga.m_End);
		edge.m_Data.m_Action = NGAEdgeType.TOKEN;
		edge.m_Data.m_Token = node;
		store(enga);
	}

	@Override
	public void visitEnd(RuleExp node) {
		/* 新建ENGA */
		ENGA enga = createENGA(node);
		/* 连接ENGA边并保存当前结点 */
		NGAEdge edge = connect(enga.m_Begin, enga.m_End);
		edge.m_Data.m_Action = NGAEdgeType.RULE;
		edge.m_Data.m_Rule = node;
		store(enga);
	}

	@Override
	public void visitEnd(SequenceExp node) {
		endChilren();
		/* 串联 */
		ENGA enga = null;
		for (ENGA child : m_Bag.m_childNGA) {
			if (enga != null) {
				connect(enga.m_End, child.m_Begin);// 首尾相连
				enga.m_End = child.m_End;
			} else {
				enga = m_Bag.m_childNGA.get(0);
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
		for (ENGA child : m_Bag.m_childNGA) {
			/* 复制标签 */
			child.m_Begin.m_Data.m_strLabel = enga.m_Begin.m_Data.m_strLabel;
			child.m_End.m_Data.m_strLabel = enga.m_End.m_Data.m_strLabel;
			/* 连接首尾 */
			connect(enga.m_Begin, child.m_Begin);
			connect(child.m_Begin, enga.m_End);
		}
		store(enga);
	}

	@Override
	public void visitEnd(OptionExp node) {
		endChilren();
		/* 获得唯一的一个子结点 */
		ENGA enga = m_Bag.m_childNGA.get(0);
		enga.m_Begin.m_Data.m_strLabel = Syntax.getSingleString(
				m_Bag.m_strPrefix, m_Bag.m_Expression, node, true);
		enga.m_End.m_Data.m_strLabel = Syntax.getSingleString(
				m_Bag.m_strPrefix, m_Bag.m_Expression, node, false);
		/* 添加可选边，即Epsilon边 */
		connect(enga.m_Begin, enga.m_End);
		store(enga);
	}

	@Override
	public void visitEnd(PropertyExp node) {
		endChilren();
		/* 获得唯一的一个子结点 */
		ENGA enga = m_Bag.m_childNGA.get(0);
		enga.m_Begin.m_Data.m_strLabel = Syntax.getSingleString(
				m_Bag.m_strPrefix, m_Bag.m_Expression, node, true);
		enga.m_End.m_Data.m_strLabel = Syntax.getSingleString(
				m_Bag.m_strPrefix, m_Bag.m_Expression, node, false);
		/* 获得该结点的边 */
		NGAEdge edge = enga.m_Begin.m_OutEdges.get(0);
		edge.m_Data.m_iStorage = node.m_iStorage;
		edge.m_Data.m_Handler = node.m_ErrorHandler;
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
		enga.m_Begin = m_StatusPool.take();
		enga.m_End = m_StatusPool.take();
		enga.m_Begin.m_Data.m_strLabel = Syntax.getSingleString(
				m_Bag.m_strPrefix, m_Bag.m_Expression, node, true);
		enga.m_End.m_Data.m_strLabel = Syntax.getSingleString(
				m_Bag.m_strPrefix, m_Bag.m_Expression, node, false);
		return enga;
	}

	/**
	 * 非确定性文法自动机描述
	 */
	public String getNGAString() {
		StringBuffer sb = new StringBuffer();
		for (NGAStatus status : m_mapNGA.values()) {
			sb.append(getNGAString(status, ""));
			sb.append(System.getProperty("line.separator"));
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
