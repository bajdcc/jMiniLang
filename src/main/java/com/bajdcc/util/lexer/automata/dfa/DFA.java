package com.bajdcc.util.lexer.automata.dfa;

import com.bajdcc.util.lexer.automata.BreadthFirstSearch;
import com.bajdcc.util.lexer.automata.EdgeType;
import com.bajdcc.util.lexer.automata.nfa.NFA;
import com.bajdcc.util.lexer.automata.nfa.NFAEdge;
import com.bajdcc.util.lexer.automata.nfa.NFAStatus;
import com.bajdcc.util.lexer.regex.IRegexComponent;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 确定性自动机（DFA）
 *
 * @author bajdcc
 */
public class DFA extends NFA {

	/**
	 * DFA状态集合
	 */
	private DFAStatus dfa = null;

	public DFA(IRegexComponent exp, boolean debug) {
		super(exp, debug);
		transfer();
	}

	/**
	 * 转换
	 */
	private void transfer() {
		deleteEpsilonEdges();
		if (bDebug) {
			System.out.println("#### 消除Epsilon边 ####");
			System.out.println(getNFAString());
		}
		determine();
		if (bDebug) {
			System.out.println("#### 确定化 ####");
			System.out.println(getDFAString());
			System.out.println("#### 状态转移矩阵 ####");
			System.out.println(getDFATableString());
		}
		minimization();
		if (bDebug) {
			System.out.println("#### 最小化 ####");
			System.out.println(getDFAString());
			System.out.println("#### 状态转移矩阵 ####");
			System.out.println(getDFATableString());
		}
	}

	/**
	 * 连接两个状态
	 *
	 * @param begin 初态
	 * @param end   终态
	 * @return 新的边
	 */
	protected DFAEdge connect(DFAStatus begin, DFAStatus end) {
		DFAEdge edge = new DFAEdge();// 申请一条新边
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
	protected void disconnect(DFAStatus status, DFAEdge edge) {
		edge.begin.outEdges.remove(edge);
		edge.end.inEdges.remove(edge);// 当前边的结束状态的入边集合去除当前边
	}

	/**
	 * 断开某个状态和所有边
	 *
	 * @param status 某状态
	 */
	protected void disconnect(DFAStatus status) {
		/* 清除所有入边 */
		for (Iterator<DFAEdge> it = status.inEdges.iterator(); it.hasNext(); ) {
			DFAEdge edge = it.next();
			it.remove();
			disconnect(edge.begin, edge);
		}
		/* 清除所有出边 */
		for (Iterator<DFAEdge> it = status.outEdges.iterator(); it.hasNext(); ) {
			DFAEdge edge = it.next();
			it.remove();
			disconnect(status, edge);
		}
	}

	/**
	 * 获取DFA状态闭包
	 *
	 * @param bfs    遍历算法
	 * @param status 初态
	 * @return 初态闭包
	 */
	protected static List<DFAStatus> getDFAStatusClosure(
			BreadthFirstSearch<DFAEdge, DFAStatus> bfs, DFAStatus status) {
		status.visit(bfs);
		return bfs.arrStatus;
	}

	/**
	 * 获得DFA状态转换矩阵
	 *
	 * @return DFA状态转换矩阵
	 */
	public List<DFAStatus> getDFATable() {
		return getDFAStatusClosure(
				new BreadthFirstSearch<>(), dfa);
	}

	/**
	 * 去除Epsilon边
	 */
	private void deleteEpsilonEdges() {
		List<NFAStatus> NFAStatusList = getNFAStatusClosure(
				new BreadthFirstSearch<>(), nfa.begin);// 获取状态闭包
		List<NFAStatus> unaccessiableList = new ArrayList<>();// 不可到达状态集合
		for (NFAStatus status : NFAStatusList) {
			boolean epsilon = true;
			for (NFAEdge edge : status.inEdges) {
				if (edge.data.kAction != EdgeType.EPSILON) {// 不是Epsilon边
					epsilon = false;// 当前可到达
					break;
				}
			}
			if (epsilon) {
				unaccessiableList.add(status);// 如果所有入边为Epsilon边，则不可到达
			}
		}
		unaccessiableList.remove(nfa.begin);// 初态设为有效
		BreadthFirstSearch<NFAEdge, NFAStatus> epsilonBFS = new BreadthFirstSearch<NFAEdge, NFAStatus>() {
			@Override
			public boolean testEdge(NFAEdge edge) {
				return edge.data.kAction == EdgeType.EPSILON;
			}
		};
		/* 遍历所有有效状态 */
		// 若为有效状态
		/* 获取当前状态的Epsilon闭包 *//* 去除自身状态 *//* 遍历Epsilon闭包的状态 *//* 如果闭包中有终态，则当前状态为终态 *//* 遍历闭包中所有边 *//* 如果当前边不是Epsilon边，就将闭包中的有效边添加到当前状态 *//* 如果当前边不是Epsilon边，就将闭包中的有效边添加到当前状态 */
		NFAStatusList.stream().filter(status -> !unaccessiableList.contains(status)).forEach(status -> {// 若为有效状态
			/* 获取当前状态的Epsilon闭包 */
			List<NFAStatus> epsilonClosure = getNFAStatusClosure(
					epsilonBFS, status);
			/* 去除自身状态 */
			epsilonClosure.remove(status);
			/* 遍历Epsilon闭包的状态 */
			for (NFAStatus epsilonStatus : epsilonClosure) {
				if (epsilonStatus.data.bFinal) {
					/* 如果闭包中有终态，则当前状态为终态 */
					status.data.bFinal = true;
				}
				/* 遍历闭包中所有边 */
				/* 如果当前边不是Epsilon边，就将闭包中的有效边添加到当前状态 */
				epsilonStatus.outEdges.stream().filter(edge -> edge.data.kAction != EdgeType.EPSILON).forEach(edge -> {
					/* 如果当前边不是Epsilon边，就将闭包中的有效边添加到当前状态 */
					connect(status, edge.end).data = edge.data;
				});
			}
		});
		/* 删除Epsilon边 */
		for (NFAStatus status : NFAStatusList) {
			for (Iterator<NFAEdge> it = status.outEdges.iterator(); it
					.hasNext(); ) {
				NFAEdge edge = it.next();
				if (edge.data.kAction == EdgeType.EPSILON) {
					it.remove();
					disconnect(status, edge);// 删除Epsilon边
				}
			}
		}
		/* 删除无效状态 */
		for (NFAStatus status : unaccessiableList) {
			NFAStatusList.remove(status);// 删除无效状态
			disconnect(status);// 删除与状态有关的所有边
		}
		unaccessiableList.clear();
		/* 删除重复边 */
		for (NFAStatus status : NFAStatusList) {
			for (int i = 0; i < status.outEdges.size() - 1; i++) {
				NFAEdge edge1 = status.outEdges.get(i);
				for (ListIterator<NFAEdge> it2 = status.outEdges
						.listIterator(i + 1); it2.hasNext(); ) {
					NFAEdge edge2 = it2.next();
					if (edge1.end == edge2.end
							&& edge1.data.kAction == edge2.data.kAction
							&& edge1.data.param == edge2.data.param) {
						it2.remove();
						disconnect(status, edge2);
					}
				}
			}
		}
	}

	/**
	 * NFA确定化，转为DFA
	 */
	private void determine() {
		/* 取得NFA所有状态 */
		List<NFAStatus> NFAStatusList = getNFAStatusClosure(
				new BreadthFirstSearch<>(), nfa.begin);
		List<DFAStatus> DFAStatusList = new ArrayList<>();
		/* 哈希表用来进行DFA状态表的查找 */
		Map<String, Integer> DFAStatusListMap = new HashMap<>();
		DFAStatus initStatus = new DFAStatus();
		initStatus.data.bFinal = nfa.begin.data.bFinal;// 是否终态
		initStatus.data.nfaStatus.add(nfa.begin);// DFA[0]=NFA初态集合
		DFAStatusList.add(initStatus);
		DFAStatusListMap.put(initStatus.data.getStatusString(NFAStatusList), 0);
		/* 构造DFA表 */
		for (int i = 0; i < DFAStatusList.size(); i++) {
			DFAStatus dfaStatus = DFAStatusList.get(i);
			List<DFAEdgeBag> bags = new ArrayList<>();
			/* 遍历当前NFA状态集合的所有边 */
			for (NFAStatus nfaStatus : dfaStatus.data.nfaStatus) {
				for (NFAEdge nfaEdge : nfaStatus.outEdges) {
					DFAEdgeBag dfaBag = null;
					for (DFAEdgeBag bag : bags) {
						/* 检查是否在表中 */
						if (nfaEdge.data.kAction == bag.kAction
								&& nfaEdge.data.param == bag.param) {
							dfaBag = bag;
							break;
						}
					}
					/* 若不存在，则新建 */
					if (dfaBag == null) {
						dfaBag = new DFAEdgeBag();
						dfaBag.kAction = nfaEdge.data.kAction;
						dfaBag.param = nfaEdge.data.param;
						bags.add(dfaBag);
					}
					/* 添加当前边 */
					dfaBag.nfaEdges.add(nfaEdge);
					/* 添加当前状态 */
					dfaBag.nfaStatus.add(nfaEdge.end);
				}
			}
			/* 遍历当前的所有DFA边 */
			for (DFAEdgeBag bag : bags) {
				/* 检测DFA指向的状态是否存在 */
				DFAStatus status;
				/* 哈希字符串 */
				String hash = bag.getStatusString(NFAStatusList);
				if (DFAStatusListMap.containsKey(bag
						.getStatusString(NFAStatusList))) {
					status = DFAStatusList.get(DFAStatusListMap.get(hash));
				} else {// 不存在DFA
					status = new DFAStatus();
					status.data.nfaStatus = new ArrayList<>(
							bag.nfaStatus);
					/* 检查终态 */
					for (NFAStatus nfaStatus : status.data.nfaStatus) {
						if (nfaStatus.data.bFinal) {
							status.data.bFinal = true;
							break;
						}
					}
					DFAStatusList.add(status);
					DFAStatusListMap.put(hash, DFAStatusList.size() - 1);
				}
				/* 创建DFA边 */
				DFAEdge edge = connect(dfaStatus, status);
				edge.data.kAction = bag.kAction;
				edge.data.param = bag.param;
				edge.data.nfaEdges = bag.nfaEdges;
			}
		}
		dfa = DFAStatusList.get(0);
	}

	/**
	 * DFA最小化
	 */
	private void minimization() {
		/* 是否存在等价类的flag */
		boolean bExistequivalentClass = true;
		while (bExistequivalentClass) {
			/* 终态集合 */
			List<Integer> finalStatus = new ArrayList<>();
			/* 非终态集合 */
			List<Integer> nonFinalStatus = new ArrayList<>();
			/* DFA状态转移表，填充终态集合 */
			int[][] transition = buildTransition(finalStatus);
			/* 填充非终态集合和状态集合的哈希表 */
			for (int i = 0; i < transition.length; i++) {
				if (!finalStatus.contains(i)) {
					nonFinalStatus.add(i);// 添加非终态序号
				}
			}
			/* DFA状态表 */
			List<DFAStatus> statusList = getDFATable();
			/* 处理终态 */
			bExistequivalentClass = mergeStatus(
					partition(finalStatus, transition), statusList);
			/* 处理非终态 */
			bExistequivalentClass |= mergeStatus(
					partition(nonFinalStatus, transition), statusList);
		}
	}

	/**
	 * 最小化划分
	 *
	 * @param statusList 初始划分
	 * @param transition 状态转移表
	 * @return 划分
	 */
	private List<List<Integer>> partition(
			List<Integer> statusList, int[][] transition) {
		if (statusList.size() == 1) {
			/* 存放结果 */
			List<List<Integer>> pat = new ArrayList<>();
			pat.add(new ArrayList<>(statusList.get(0)));
			return pat;
		} else {
			/* 用于查找相同状态 */
			Map<String, List<Integer>> map = new HashMap<>();
			for (int status : statusList) {
				/* 获得状态hash */
				String hash = getStatusLineString(transition[status]);
				/* 状态是否出现过 */
				if (map.containsKey(hash)) {
					/* 状态重复，加入上个相同状态的集合 */
					map.get(hash).add(status);
				} else {
					/* 前次出现，创建数组保存它 */
					List<Integer> set = new ArrayList<>();
					set.add(status);
					map.put(hash, set);
				}
			}
			return new ArrayList<>(map.values());
		}
	}

	/**
	 * 合并相同状态
	 *
	 * @param pat        状态划分
	 * @param statusList 状态转移表
	 */
	private boolean mergeStatus(List<List<Integer>> pat,
	                            List<DFAStatus> statusList) {
		/* 保存要处理的多状态合并的划分 */
		List<List<Integer>> dealWith = pat.stream().filter(collection -> collection.size() > 1).collect(Collectors.toList());
		// 有多个状态
		/* 是否已经没有等价类，若没有，就返回false，这样算法就结束（收敛 ） */
		if (dealWith.isEmpty()) {
			return false;
		}
		/* 合并每一分组 */
		for (List<Integer> collection : dealWith) {
			/* 目标状态为集合中第一个状态，其余状态被合并 */
			int dstStatus = collection.get(0);
			/* 目标状态 */
			DFAStatus status = statusList.get(dstStatus);
			for (int i = 1; i < collection.size(); i++) {
				/* 重复的状态 */
				int srcStatus = collection.get(i);
				DFAStatus dupStatus = statusList.get(srcStatus);
				/* 备份重复状态的入边 */
				ArrayList<DFAEdge> edges = new ArrayList<>(
						dupStatus.inEdges);
				/* 将指向重复状态的边改为指向目标状态的边 */
				for (DFAEdge edge : edges) {
					/* 复制边 */
					connect(edge.begin, status).data = edge.data;
				}
				/* 去除重复状态 */
				disconnect(dupStatus);
			}
		}
		return true;
	}

	/**
	 * 获取DFA状态转移表某行的字符串
	 *
	 * @param line 某行的索引矩阵
	 * @return 哈希字符串
	 */
	private String getStatusLineString(int[] line) {
		StringBuilder sb = new StringBuilder();
		for (int i : line) {
			sb.append(i).append(",");
		}
		return sb.toString();
	}

	/**
	 * 建立状态
	 *
	 * @param finalStatus 终态
	 * @return 状态转换矩阵
	 */
	public int[][] buildTransition(Collection<Integer> finalStatus) {
		finalStatus.clear();
		/* DFA状态表 */
		List<DFAStatus> statusList = getDFATable();
		/* 建立状态转移矩阵 */
		int[][] transition = new int[statusList.size()][chMap.getRanges()
				.size()];
		/* 填充状态转移表 */
		for (int i = 0; i < statusList.size(); i++) {
			DFAStatus status = statusList.get(i);
			if (status.data.bFinal) {
				finalStatus.add(i);// 标记终态
			}
			for (int j = 0; j < transition[i].length; j++) {
				transition[i][j] = -1;// 置无效标记-1
			}
			for (DFAEdge edge : status.outEdges) {
				if (edge.data.kAction == EdgeType.CHARSET) {
					transition[i][edge.data.param] = statusList
							.indexOf(edge.end);
				}
			}
		}
		return transition;
	}

	/**
	 * 提供DFA描述
	 *
	 * @return DFA描述
	 */
	public String getDFAString() {
		/* 取得NFA所有状态 */
		List<NFAStatus> NFAStatusList = getNFAStatusClosure(
				new BreadthFirstSearch<>(), nfa.begin);
		/* 取得DFA所有状态 */
		List<DFAStatus> DFAStatusList = getDFAStatusClosure(
				new BreadthFirstSearch<>(), dfa);
		StringBuilder sb = new StringBuilder();
		/* 生成DFA描述 */
		for (int i = 0; i < DFAStatusList.size(); i++) {
			DFAStatus status = DFAStatusList.get(i);
			/* 状态 */
			sb.append("状态[").append(i).append("]").append(status.data.bFinal ? "[结束]" : "").append(" => ").append(status.data.getStatusString(NFAStatusList)).append(System.lineSeparator());
			/* 边 */
			for (DFAEdge edge : status.outEdges) {
				sb.append("\t边 => [").append(DFAStatusList.indexOf(edge.end)).append("]").append(System.lineSeparator());// 指向边
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

	/**
	 * 获取状态转移矩阵描述
	 *
	 * @return 状态转移矩阵描述
	 */
	public String getDFATableString() {
		int[][] transition = buildTransition(new ArrayList<>());
		StringBuilder sb = new StringBuilder();
		for (int[] aTransition : transition) {
			for (int anATransition : aTransition) {
				sb.append("\t").append(anATransition);
			}
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
}