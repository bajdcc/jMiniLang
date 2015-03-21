package priv.bajdcc.lexer.automata.nfa;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.vibur.objectpool.ConcurrentLinkedPool;
import org.vibur.objectpool.PoolService;

import priv.bajdcc.lexer.automata.BreadthFirstSearch;
import priv.bajdcc.lexer.automata.EdgeType;
import priv.bajdcc.lexer.regex.CharacterMap;
import priv.bajdcc.lexer.regex.CharacterRange;
import priv.bajdcc.lexer.regex.Charset;
import priv.bajdcc.lexer.regex.Constructure;
import priv.bajdcc.lexer.regex.IRegexComponent;
import priv.bajdcc.lexer.regex.IRegexComponentVisitor;
import priv.bajdcc.lexer.regex.Repetition;
import priv.bajdcc.lexer.utility.ObjectFactory;

/**
 * NFA构成算法（AST->NFA）
 * 
 * @author bajdcc
 *
 */
public class NFA implements IRegexComponentVisitor {

	/**
	 * 是否为调试模式（打印信息）
	 */
	protected boolean m_bDebug = false;
	
	/**
	 * 边对象池
	 */
	private PoolService<NFAEdge> m_EdgesPool = new ConcurrentLinkedPool<NFAEdge>(
			new ObjectFactory<NFAEdge>() {
				public NFAEdge create() {
					return new NFAEdge();
				};
			}, 1024, 10240, false);

	/**
	 * 状态对象池
	 */
	private PoolService<NFAStatus> m_StatusPool = new ConcurrentLinkedPool<NFAStatus>(
			new ObjectFactory<NFAStatus>() {
				public NFAStatus create() {
					return new NFAStatus();
				};
			}, 1024, 10240, false);

	/**
	 * 深度
	 */
	private int m_iLevel = 0;

	/**
	 * NFA栈
	 */
	private Stack<ArrayList<ENFA>> m_stkNFA = new Stack<ArrayList<ENFA>>();

	/**
	 * NFA子表
	 */
	private ArrayList<ENFA> m_childNFA = new ArrayList<ENFA>();

	/**
	 * ENFA
	 */
	protected ENFA m_mainNFA = null;

	/**
	 * 表达式树根结点
	 */
	protected IRegexComponent m_Expression = null;

	/**
	 * Sigma状态集
	 */
	protected CharacterMap m_Map = new CharacterMap();

	public NFA(IRegexComponent exp, boolean debug) {
		m_bDebug = debug;
		m_Expression = exp;
		m_Expression.visit(m_Map);
		if (m_bDebug) {
			System.out.println("#### 状态集合 ####");
			System.out.println(getStatusString());
		}
		m_Expression.visit(this);
		if (m_bDebug) {
			System.out.println("#### EpsilonNFA ####");
			System.out.println(getNFAString());
		}
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
	protected NFAEdge connect(NFAStatus begin, NFAStatus end) {
		NFAEdge edge = m_EdgesPool.take();// 申请一条新边
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
	protected void disconnect(NFAStatus status, NFAEdge edge) {
		edge.m_End.m_InEdges.remove(edge);// 当前边的结束状态的入边集合去除当前边
		m_EdgesPool.restore(edge);
	}

	/**
	 * 断开某个状态和所有边
	 * 
	 * @param begin
	 *            某状态
	 */
	protected void disconnect(NFAStatus status) {
		/* 清除所有入边 */
		for (Iterator<NFAEdge> it = status.m_InEdges.iterator(); it.hasNext();) {
			NFAEdge edge = it.next();
			it.remove();
			disconnect(edge.m_Begin, edge);
		}
		/* 清除所有出边 */
		for (Iterator<NFAEdge> it = status.m_OutEdges.iterator(); it.hasNext();) {
			NFAEdge edge = it.next();
			it.remove();
			disconnect(status, edge);
		}
		m_StatusPool.restore(status);
	}

	@Override
	public void visitBegin(Charset node) {
		enter();
		ENFA enfa = new ENFA();
		enfa.m_Begin = m_StatusPool.take();
		enfa.m_End = m_StatusPool.take();
		for (CharacterRange range : m_Map.getRanges()) {// 遍历所有字符区间
			if (node.include(range.m_chLowerBound)) {// 若在当前结点范围内，则添加边
				NFAEdge edge = connect(enfa.m_Begin, enfa.m_End);// 连接两个状态
				edge.m_Data.m_Action = EdgeType.CHARSET;// 字符类型
				edge.m_Data.m_Param = m_Map.find(range.m_chLowerBound);
			}
		}
		storeENFA(enfa);
	}

	@Override
	public void visitBegin(Constructure node) {
		enter();
		enterChildren();
	}

	@Override
	public void visitBegin(Repetition node) {
		enter();
		enterChildren();
	}

	@Override
	public void visitEnd(Charset node) {
		leave();
	}

	@Override
	public void visitEnd(Constructure node) {
		leaveChildren();
		ENFA result = null;
		if (!node.m_bBranch) {
			/* 将当前NFA的两端同每个子结点的两端串联 */
			for (ENFA enfa : m_childNFA) {
				if (result == null) {
					result = m_childNFA.get(0);
				} else {
					connect(result.m_End, enfa.m_Begin);
					result.m_End = enfa.m_End;
				}
			}
		} else {
			result = new ENFA();
			result.m_Begin = m_StatusPool.take();
			result.m_End = m_StatusPool.take();
			/* 将当前NFA的两端同每个子结点的两端并联 */
			for (ENFA enfa : m_childNFA) {
				connect(result.m_Begin, enfa.m_Begin);
				connect(enfa.m_End, result.m_End);
			}
		}
		storeENFA(result);
		leave();
	}

	@Override
	public void visitEnd(Repetition node) {
		leaveChildren();
		/* 构造子图副本 */
		ArrayList<ENFA> subENFAList = new ArrayList<ENFA>();
		ENFA enfa = new ENFA();
		enfa.m_Begin = m_childNFA.get(0).m_Begin;
		enfa.m_End = m_childNFA.get(0).m_End;
		int count = Integer.max(node.m_iLowerBound, node.m_iUpperBound);
		subENFAList.add(enfa);
		/* 循环复制ENFA */
		for (int i = 1; i <= count; i++) {
			subENFAList.add(copyENFA(enfa));
		}
		enfa = new ENFA();
		/* 构造循环起始部分 */
		if (node.m_iLowerBound > 0) {
			enfa.m_Begin = m_childNFA.get(0).m_Begin;
			enfa.m_End = m_childNFA.get(0).m_End;
			for (int i = 1; i < node.m_iLowerBound; i++) {
				connect(enfa.m_End, subENFAList.get(i).m_Begin);// 连接首尾
				enfa.m_End = subENFAList.get(i).m_End;
			}
		}
		if (node.m_iUpperBound != -1) {// 有限循环，构造循环结束部分
			for (int i = node.m_iLowerBound; i < node.m_iUpperBound; i++) {
				if (enfa.m_End != null) {
					connect(enfa.m_End, subENFAList.get(i).m_Begin);// 连接首尾
				} else {
					enfa = subENFAList.get(i);
				}
				connect(subENFAList.get(i).m_Begin,
						subENFAList.get(node.m_iUpperBound - 1).m_End);
			}
		} else {// 无限循环
			NFAStatus tailBegin, tailEnd;
			if (enfa.m_End == null) {
				tailBegin = enfa.m_Begin = m_StatusPool.take();
				tailEnd = enfa.m_End = m_StatusPool.take();
			} else {
				tailBegin = enfa.m_End;
				tailEnd = enfa.m_End = m_StatusPool.take();
			}
			/* 构造无限循环的结束部分 */
			connect(tailBegin, subENFAList.get(node.m_iLowerBound).m_Begin);
			connect(subENFAList.get(node.m_iLowerBound).m_End, tailBegin);
			connect(tailBegin, tailEnd);
		}
		/* 构造循环的头尾部分 */
		NFAStatus begin = m_StatusPool.take();
		NFAStatus end = m_StatusPool.take();
		connect(begin, enfa.m_Begin);
		connect(enfa.m_End, end);
		enfa.m_Begin = begin;
		enfa.m_End = end;
		storeENFA(enfa);
		leave();
	}

	/**
	 * 返回栈顶NFA
	 * 
	 * @return 当前栈顶NFA
	 */
	private ArrayList<ENFA> currentNFA() {
		return m_stkNFA.peek();
	}

	/**
	 * 存储NFA
	 * 
	 * @param enfa
	 *            ENFA
	 */
	private void storeENFA(ENFA enfa) {
		currentNFA().add(enfa);
	}

	/**
	 * 进入结点
	 */
	private void enter() {
		if (m_iLevel == 0) {// 刚进入该结点时，访问其子结点
			enterChildren();
		}
		m_iLevel++;
	}

	/**
	 * 离开结点
	 */
	private void leave() {
		m_iLevel--;
		if (m_iLevel == 0) {// 刚进入该结点时，访问其子结点
			leaveChildren();
			store();
		}
	}

	/**
	 * 进入子结点
	 */
	private void enterChildren() {
		m_stkNFA.push(new ArrayList<ENFA>());// 新建ENFA表
		m_childNFA = null;
	}

	/**
	 * 离开子结点
	 */
	private void leaveChildren() {
		m_childNFA = m_stkNFA.pop();
	}

	/**
	 * 存储结果
	 */
	private void store() {
		ENFA enfa = m_childNFA.get(0);
		enfa.m_End.m_Data.m_bFinal = true;
		m_mainNFA = enfa;
	}

	/**
	 * 复制ENFA
	 * 
	 * @param enfa
	 *            ENFA
	 * @return 副本
	 */
	private ENFA copyENFA(ENFA enfa) {
		ArrayList<NFAStatus> srcStatusList = new ArrayList<NFAStatus>();// 初态表
		ArrayList<NFAStatus> dstStatusList = new ArrayList<NFAStatus>();// 终态表
		srcStatusList.addAll(getNFAStatusClosure(
				new BreadthFirstSearch<NFAEdge, NFAStatus>(), enfa.m_Begin)); // 获取状态闭包
		/* 复制状态 */
		for (NFAStatus status : srcStatusList) {
			NFAStatus newStatus = m_StatusPool.take();
			newStatus.m_Data = status.m_Data;
			dstStatusList.add(newStatus);
		}
		/* 复制边 */
		for (int i = 0; i < srcStatusList.size(); i++) {
			NFAStatus status = srcStatusList.get(i);
			for (NFAEdge edge : status.m_OutEdges) {
				NFAEdge newEdge = connect(dstStatusList.get(i),
						dstStatusList.get(srcStatusList.indexOf(edge.m_End)));
				newEdge.m_Data = edge.m_Data;
			}
		}
		/* 找到始态和终态 */
		ENFA result = new ENFA();
		result.m_Begin = dstStatusList.get(srcStatusList.indexOf(enfa.m_Begin));
		result.m_End = dstStatusList.get(srcStatusList.indexOf(enfa.m_End));
		return result;
	}
	
	/**
	 * 获取字符映射表
	 */
	public CharacterMap getCharacterMap() {
		return m_Map;
	}

	/**
	 * 获取NFA状态闭包
	 * 
	 * @param bfs
	 *            遍历算法
	 * @param status
	 *            初态
	 * @return 初态闭包
	 */
	protected static ArrayList<NFAStatus> getNFAStatusClosure(
			BreadthFirstSearch<NFAEdge, NFAStatus> bfs, NFAStatus status) {
		status.visit(bfs);
		return bfs.m_Path;
	}

	/**
	 * 字符区间描述
	 */
	public String getStatusString() {
		return m_Map.toString();
	}

	/**
	 * NFA描述
	 */
	public String getNFAString() {
		StringBuilder sb = new StringBuilder();
		ArrayList<NFAStatus> statusList = getNFAStatusClosure(
				new BreadthFirstSearch<NFAEdge, NFAStatus>(), m_mainNFA.m_Begin);// 获取状态闭包
		/* 生成NFA描述 */
		for (int i = 0; i < statusList.size(); i++) {
			NFAStatus status = statusList.get(i);
			/* 状态 */
			sb.append("状态[" + i + "]" + (status.m_Data.m_bFinal ? "[结束]" : "")
					+ System.getProperty("line.separator"));
			/* 边 */
			for (NFAEdge edge : status.m_OutEdges) {
				sb.append("\t边 => [" + statusList.indexOf(edge.m_End) + "]"
						+ System.getProperty("line.separator"));// 指向边
				sb.append("\t\t类型 => " + edge.m_Data.m_Action.getName());
				switch (edge.m_Data.m_Action)// 类型
				{
				case CHARSET:
					sb.append("\t" + m_Map.getRanges().get(edge.m_Data.m_Param));// 区间
					break;
				case EPSILON:
					break;
				default:
					break;
				}
				sb.append(System.getProperty("line.separator"));
			}
		}
		return sb.toString();
	}
}
