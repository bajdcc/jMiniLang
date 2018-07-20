package com.bajdcc.util.lexer.automata.nfa;

import com.bajdcc.util.lexer.automata.BreadthFirstSearch;
import com.bajdcc.util.lexer.automata.EdgeType;
import com.bajdcc.util.lexer.regex.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * NFA构成算法（AST-&gt;NFA）
 *
 * @author bajdcc
 */
public class NFA implements IRegexComponentVisitor {

	/**
	 * 是否为调试模式（打印信息）
	 */
	protected boolean bDebug;

	/**
	 * 深度
	 */
	private int iLevel = 0;

	/**
	 * NFA栈
	 */
	private Stack<ArrayList<ENFA>> stkNFA = new Stack<>();

	/**
	 * NFA子表
	 */
	private ArrayList<ENFA> childNFA = new ArrayList<>();

	/**
	 * ENFA
	 */
	protected ENFA nfa = null;

	/**
	 * 表达式树根结点
	 */
	protected IRegexComponent expression;

	/**
	 * Sigma状态集
	 */
	protected CharacterMap chMap = new CharacterMap();

	public NFA(IRegexComponent exp, boolean debug) {
		bDebug = debug;
		expression = exp;
		expression.visit(chMap);
		if (bDebug) {
			System.out.println("#### 状态集合 ####");
			System.out.println(getStatusString());
		}
		expression.visit(this);
		if (bDebug) {
			System.out.println("#### EpsilonNFA ####");
			System.out.println(getNFAString());
		}
	}

	/**
	 * 连接两个状态
	 *
	 * @param begin 初态
	 * @param end   终态
	 * @return 新的边
	 */
	protected NFAEdge connect(NFAStatus begin, NFAStatus end) {
		NFAEdge edge = new NFAEdge();// 申请一条新边
		edge.begin = begin;
		edge.end = end;
		begin.outEdges.add(edge);// 添加进起始边的出边
		end.inEdges.add(edge);// 添加进结束边的入边
		return edge;
	}

	/**
	 * 断开某个状态和某条边
	 *
	 * @param status 某状态
	 * @param edge   某条边
	 */
	protected void disconnect(NFAStatus status, NFAEdge edge) {
		edge.end.inEdges.remove(edge);// 当前边的结束状态的入边集合去除当前边
	}

	/**
	 * 断开某个状态和所有边
	 *
	 * @param status 某状态
	 */
	protected void disconnect(NFAStatus status) {
		/* 清除所有入边 */
		for (Iterator<NFAEdge> it = status.inEdges.iterator(); it.hasNext(); ) {
			NFAEdge edge = it.next();
			it.remove();
			disconnect(edge.begin, edge);
		}
		/* 清除所有出边 */
		for (Iterator<NFAEdge> it = status.outEdges.iterator(); it.hasNext(); ) {
			NFAEdge edge = it.next();
			it.remove();
			disconnect(status, edge);
		}
	}

	@Override
	public void visitBegin(Charset node) {
		enter();
		ENFA enfa = new ENFA();
		enfa.begin = new NFAStatus();
		enfa.end = new NFAStatus();
		// 遍历所有字符区间
// 若在当前结点范围内，则添加边
// 连接两个状态
// 字符类型
		chMap.getRanges().stream().filter(range -> node.include(range.getLowerBound())).forEach(range -> {// 若在当前结点范围内，则添加边
			NFAEdge edge = connect(enfa.begin, enfa.end);// 连接两个状态
			edge.data.kAction = EdgeType.CHARSET;// 字符类型
			edge.data.param = chMap.find(range.getLowerBound());
		});
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
		if (!node.getBranch()) {
			/* 将当前NFA的两端同每个子结点的两端串联 */
			for (ENFA enfa : childNFA) {
				if (result == null) {
					result = childNFA.get(0);
				} else {
					connect(result.end, enfa.begin);
					result.end = enfa.end;
				}
			}
		} else {
			result = new ENFA();
			result.begin = new NFAStatus();
			result.end = new NFAStatus();
			/* 将当前NFA的两端同每个子结点的两端并联 */
			for (ENFA enfa : childNFA) {
				connect(result.begin, enfa.begin);
				connect(enfa.end, result.end);
			}
		}
		storeENFA(result);
		leave();
	}

	@Override
	public void visitEnd(Repetition node) {
		leaveChildren();
		// #### 注意 ####
		// 由于正则表达式的语法树结构使然，循环语句的子结点必定只有一个，
		// 为字符集、并联或串联。
		/* 构造子图副本 */
		ArrayList<ENFA> subENFAList = new ArrayList<>();
		ENFA enfa = new ENFA();
		enfa.begin = childNFA.get(0).begin;
		enfa.end = childNFA.get(0).end;
		int count = Math.max(node.getLowerBound(), node.getUpperBound());
		subENFAList.add(enfa);
		/* 循环复制ENFA */
		for (int i = 1; i <= count; i++) {
			subENFAList.add(copyENFA(enfa));
		}
		enfa = new ENFA();
		/* 构造循环起始部分 */
		if (node.getLowerBound() > 0) {
			enfa.begin = childNFA.get(0).begin;
			enfa.end = childNFA.get(0).end;
			for (int i = 1; i < node.getLowerBound(); i++) {
				connect(enfa.end, subENFAList.get(i).begin);// 连接首尾
				enfa.end = subENFAList.get(i).end;
			}
		}
		if (node.getUpperBound() != -1) {// 有限循环，构造循环结束部分
			for (int i = node.getLowerBound(); i < node.getUpperBound(); i++) {
				if (enfa.end != null) {
					connect(enfa.end, subENFAList.get(i).begin);// 连接首尾
					enfa.end = subENFAList.get(i).end;
				} else {
					enfa = subENFAList.get(i);
				}
				connect(subENFAList.get(i).begin,
						subENFAList.get(node.getUpperBound() - 1).end);
			}
		} else {// 无限循环
			NFAStatus tailBegin, tailEnd;
			if (enfa.end == null) {// 循环最低次数为0，即未构造起始部分，故需构造
				tailBegin = enfa.begin = new NFAStatus();
				tailEnd = enfa.end = new NFAStatus();
			} else {// 起始部分已构造完毕，故起始端无需再次构造
				tailBegin = enfa.end;
				tailEnd = enfa.end = new NFAStatus();
			}
			/* 构造无限循环的结束部分，连接起始端与循环端的双向e边 */
			connect(tailBegin, subENFAList.get(node.getLowerBound()).begin);
			connect(subENFAList.get(node.getLowerBound()).end, tailBegin);
			connect(tailBegin, tailEnd);
		}
		/* 构造循环的头尾部分 */
		NFAStatus begin = new NFAStatus();
		NFAStatus end = new NFAStatus();
		connect(begin, enfa.begin);
		connect(enfa.end, end);
		enfa.begin = begin;
		enfa.end = end;
		storeENFA(enfa);
		leave();
	}

	/**
	 * 返回栈顶NFA
	 *
	 * @return 当前栈顶NFA
	 */
	private ArrayList<ENFA> currentNFA() {
		return stkNFA.peek();
	}

	/**
	 * 存储NFA
	 *
	 * @param enfa ENFA
	 */
	private void storeENFA(ENFA enfa) {
		currentNFA().add(enfa);
	}

	/**
	 * 进入结点
	 */
	private void enter() {
		if (iLevel == 0) {// 首次访问AST时
			enterChildren();
		}
		iLevel++;
	}

	/**
	 * 离开结点
	 */
	private void leave() {
		iLevel--;
		if (iLevel == 0) {// 离开整个AST时，存储结果
			leaveChildren();
			store();
		}
	}

	/**
	 * 进入子结点
	 */
	private void enterChildren() {
		stkNFA.push(new ArrayList<>());// 新建ENFA表
		childNFA = null;
	}

	/**
	 * 离开子结点
	 */
	private void leaveChildren() {
		childNFA = stkNFA.pop();// 获得当前结点的子结点
	}

	/**
	 * 存储结果
	 */
	private void store() {
		// #### 注意 ####
		// 本程序由regex构造的AST形成的NFA有明确且唯一的初态和终态
		ENFA enfa = childNFA.get(0);// 此时位于顶层，故顶层首个ENFA为根ENFA
		enfa.end.data.bFinal = true;// 根ENFA的初态为begin，终态为end
		nfa = enfa;
	}

	/**
	 * 复制ENFA
	 *
	 * @param enfa ENFA
	 * @return 副本
	 */
	private ENFA copyENFA(ENFA enfa) {
		ArrayList<NFAStatus> dstStatusList = new ArrayList<>();// 终态表
		// 获取状态闭包
		ArrayList<NFAStatus> srcStatusList = new ArrayList<>(getNFAStatusClosure(
				new BreadthFirstSearch<>(), enfa.begin));
		/* 复制状态 */
		for (NFAStatus status : srcStatusList) {
			NFAStatus newStatus = new NFAStatus();
			newStatus.data = status.data;
			dstStatusList.add(newStatus);
		}
		/* 复制边 */
		for (int i = 0; i < srcStatusList.size(); i++) {
			NFAStatus status = srcStatusList.get(i);
			for (NFAEdge edge : status.outEdges) {
				NFAEdge newEdge = connect(dstStatusList.get(i),
						dstStatusList.get(srcStatusList.indexOf(edge.end)));
				newEdge.data = edge.data;
			}
		}
		/* 新建ENFA，连接初态和终态 */
		ENFA result = new ENFA();
		result.begin = dstStatusList.get(srcStatusList.indexOf(enfa.begin));
		result.end = dstStatusList.get(srcStatusList.indexOf(enfa.end));
		return result;
	}

	/**
	 * 获取字符映射表
	 *
	 * @return 字符映射表
	 */
	public CharacterMap getCharacterMap() {
		return chMap;
	}

	/**
	 * 获取NFA状态闭包
	 *
	 * @param bfs    遍历算法
	 * @param status 初态
	 * @return 初态闭包
	 */
	protected static ArrayList<NFAStatus> getNFAStatusClosure(
			BreadthFirstSearch<NFAEdge, NFAStatus> bfs, NFAStatus status) {
		status.visit(bfs);
		return bfs.arrStatus;
	}

	/**
	 * 字符区间描述
	 *
	 * @return 字符区间描述
	 */
	public String getStatusString() {
		return chMap.toString();
	}

	/**
	 * NFA描述
	 *
	 * @return NFA描述
	 */
	public String getNFAString() {
		StringBuilder sb = new StringBuilder();
		ArrayList<NFAStatus> statusList = getNFAStatusClosure(
				new BreadthFirstSearch<>(), nfa.begin);// 获取状态闭包
		/* 生成NFA描述 */
		for (int i = 0; i < statusList.size(); i++) {
			NFAStatus status = statusList.get(i);
			/* 状态 */
			sb.append("状态[").append(i).append("]").append(status.data.bFinal ? "[结束]" : "").append(System.lineSeparator());
			/* 边 */
			for (NFAEdge edge : status.outEdges) {
				sb.append("\t边 => [").append(statusList.indexOf(edge.end)).append("]").append(System.lineSeparator());// 指向边
				sb.append("\t\t类型 => ").append(edge.data.kAction.getName());
				switch (edge.data.kAction)// 类型
				{
					case CHARSET:
						sb.append("\t").append(chMap.getRanges().get(edge.data.param));// 区间
						break;
					case EPSILON:
						break;
					default:
						break;
				}
				sb.append(System.lineSeparator());
			}
		}
		return sb.toString();
	}
}
