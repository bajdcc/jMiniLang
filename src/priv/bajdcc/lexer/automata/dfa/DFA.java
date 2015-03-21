package priv.bajdcc.lexer.automata.dfa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

import org.vibur.objectpool.ConcurrentLinkedPool;
import org.vibur.objectpool.PoolService;

import priv.bajdcc.lexer.automata.BreadthFirstSearch;
import priv.bajdcc.lexer.automata.EdgeType;
import priv.bajdcc.lexer.automata.dfa.DFAStatus;
import priv.bajdcc.lexer.automata.nfa.NFA;
import priv.bajdcc.lexer.automata.nfa.NFAEdge;
import priv.bajdcc.lexer.automata.nfa.NFAStatus;
import priv.bajdcc.lexer.regex.IRegexComponent;
import priv.bajdcc.lexer.utility.ObjectFactory;

/**
 * 确定性自动机（DFA）
 * 
 * @author bajdcc
 *
 */
public class DFA extends NFA {
	/**
	 * 边对象池
	 */
	private PoolService<DFAEdge> m_EdgesPool = new ConcurrentLinkedPool<DFAEdge>(
			new ObjectFactory<DFAEdge>() {
				public DFAEdge create() {
					return new DFAEdge();
				};
			}, 1024, 10240, false);

	/**
	 * 状态对象池
	 */
	private PoolService<DFAStatus> m_StatusPool = new ConcurrentLinkedPool<DFAStatus>(
			new ObjectFactory<DFAStatus>() {
				public DFAStatus create() {
					return new DFAStatus();
				};
			}, 1024, 10240, false);

	/**
	 * DFA状态集合
	 */
	private DFAStatus m_mainDFA = null;

	public DFA(IRegexComponent exp, boolean debug) {
		super(exp, debug);
		transfer();
	}

	/**
	 * 转换
	 */
	private void transfer() {
		deleteEpsilonEdges();
		if (m_bDebug) {
			System.out.println("#### 消除Epsilon边 ####");
			System.out.println(getNFAString());
		}
		determine();
		if (m_bDebug) {
			System.out.println("#### 确定化 ####");
			System.out.println(getDFAString());
			System.out.println("#### 状态转移矩阵 ####");
			System.out.println(getDFATableString());
		}
		minimization();
		if (m_bDebug) {
			System.out.println("#### 最小化 ####");
			System.out.println(getDFAString());
			System.out.println("#### 状态转移矩阵 ####");
			System.out.println(getDFATableString());
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
	protected DFAEdge connect(DFAStatus begin, DFAStatus end) {
		DFAEdge edge = m_EdgesPool.take();// 申请一条新边
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
	protected void disconnect(DFAStatus status, DFAEdge edge) {
		edge.m_Begin.m_OutEdges.remove(edge);
		edge.m_End.m_InEdges.remove(edge);// 当前边的结束状态的入边集合去除当前边
		m_EdgesPool.restore(edge);
	}

	/**
	 * 断开某个状态和所有边
	 * 
	 * @param begin
	 *            某状态
	 */
	protected void disconnect(DFAStatus status) {
		/* 清除所有入边 */
		for (Iterator<DFAEdge> it = status.m_InEdges.iterator(); it.hasNext();) {
			DFAEdge edge = it.next();
			it.remove();
			disconnect(edge.m_Begin, edge);
		}
		/* 清除所有出边 */
		for (Iterator<DFAEdge> it = status.m_OutEdges.iterator(); it.hasNext();) {
			DFAEdge edge = it.next();
			it.remove();
			disconnect(status, edge);
		}
		m_StatusPool.restore(status);
	}

	/**
	 * 获取DFA状态闭包
	 * 
	 * @param bfs
	 *            遍历算法
	 * @param status
	 *            初态
	 * @return 初态闭包
	 */
	protected static ArrayList<DFAStatus> getDFAStatusClosure(
			BreadthFirstSearch<DFAEdge, DFAStatus> bfs, DFAStatus status) {
		status.visit(bfs);
		return bfs.m_Path;
	}

	/**
	 * 获得DFA状态转换矩阵
	 */
	public ArrayList<DFAStatus> getDFATable() {
		return getDFAStatusClosure(
				new BreadthFirstSearch<DFAEdge, DFAStatus>(), m_mainDFA);
	}

	/**
	 * 去除Epsilon边
	 */
	private void deleteEpsilonEdges() {
		ArrayList<NFAStatus> NFAStatusList = getNFAStatusClosure(
				new BreadthFirstSearch<NFAEdge, NFAStatus>(), m_mainNFA.m_Begin);// 获取状态闭包
		ArrayList<NFAStatus> unaccessiableList = new ArrayList<NFAStatus>();// 不可到达状态集合
		for (NFAStatus status : NFAStatusList) {
			boolean epsilon = true;
			for (NFAEdge edge : status.m_InEdges) {
				if (edge.m_Data.m_Action != EdgeType.EPSILON) {// 不是Epsilon边
					epsilon = false;// 当前可到达
					break;
				}
			}
			if (epsilon) {
				unaccessiableList.add(status);// 如果所有入边为Epsilon边，则不可到达
			}
		}
		unaccessiableList.remove(m_mainNFA.m_Begin);// 初态设为有效
		BreadthFirstSearch<NFAEdge, NFAStatus> epsilonBFS = new BreadthFirstSearch<NFAEdge, NFAStatus>() {
			@Override
			public boolean testEdge(NFAEdge edge) {
				return edge.m_Data.m_Action == EdgeType.EPSILON;
			}
		};
		/* 遍历所有有效状态 */
		for (NFAStatus status : NFAStatusList) {
			if (!unaccessiableList.contains(status)) {// 若为有效状态
				/* 获取当前状态的Epsilon闭包 */
				ArrayList<NFAStatus> epsilonClosure = getNFAStatusClosure(
						epsilonBFS, status);
				/* 去除自身状态 */
				epsilonClosure.remove(status);
				/* 遍历Epsilon闭包的状态 */
				for (NFAStatus epsilonStatus : epsilonClosure) {
					if (epsilonStatus.m_Data.m_bFinal) {
						/* 如果闭包中有终态，则当前状态为终态 */
						status.m_Data.m_bFinal = true;
					}
					/* 遍历闭包中所有边 */
					for (NFAEdge edge : epsilonStatus.m_OutEdges) {
						if (edge.m_Data.m_Action != EdgeType.EPSILON) {
							/* 如果当前边不是Epsilon边，就将闭包中的有效边添加到当前状态 */
							connect(status, edge.m_End).m_Data = edge.m_Data;
						}
					}
				}
			}
		}
		/* 删除Epsilon边 */
		for (NFAStatus status : NFAStatusList) {
			for (Iterator<NFAEdge> it = status.m_OutEdges.iterator(); it
					.hasNext();) {
				NFAEdge edge = it.next();
				if (edge.m_Data.m_Action == EdgeType.EPSILON) {
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
			for (int i = 0; i < status.m_OutEdges.size() - 1; i++) {
				NFAEdge edge1 = status.m_OutEdges.get(i);
				for (ListIterator<NFAEdge> it2 = status.m_OutEdges
						.listIterator(i + 1); it2.hasNext();) {
					NFAEdge edge2 = it2.next();
					if (edge1.m_End == edge2.m_End
							&& edge1.m_Data.m_Action == edge2.m_Data.m_Action
							&& edge1.m_Data.m_Param == edge2.m_Data.m_Param) {
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
		ArrayList<NFAStatus> NFAStatusList = getNFAStatusClosure(
				new BreadthFirstSearch<NFAEdge, NFAStatus>(), m_mainNFA.m_Begin);
		ArrayList<DFAStatus> DFAStatusList = new ArrayList<DFAStatus>();
		/* 哈希表用来进行DFA状态表的查找 */
		HashMap<String, Integer> DFAStatusListMap = new HashMap<String, Integer>();
		DFAStatus initStatus = m_StatusPool.take();
		initStatus.m_Data.m_bFinal = m_mainNFA.m_Begin.m_Data.m_bFinal;// 是否终态
		initStatus.m_Data.m_NFAStatus.add(m_mainNFA.m_Begin);// DFA[0]=NFA初态集合
		DFAStatusList.add(initStatus);
		DFAStatusListMap.put(initStatus.m_Data.getStatusString(NFAStatusList),
				0);
		/* 构造DFA表 */
		for (int i = 0; i < DFAStatusList.size(); i++) {
			DFAStatus dfaStatus = DFAStatusList.get(i);
			ArrayList<DFAEdgeBag> bags = new ArrayList<DFAEdgeBag>();
			/* 遍历当前NFA状态集合的所有边 */
			for (NFAStatus nfaStatus : dfaStatus.m_Data.m_NFAStatus) {
				for (NFAEdge nfaEdge : nfaStatus.m_OutEdges) {
					DFAEdgeBag dfaBag = null;
					for (DFAEdgeBag bag : bags) {
						/* 检查是否在表中 */
						if (nfaEdge.m_Data.m_Action == bag.m_Action
								&& nfaEdge.m_Data.m_Param == bag.m_Param) {
							dfaBag = bag;
							break;
						}
					}
					/* 若不存在，则新建 */
					if (dfaBag == null) {
						dfaBag = new DFAEdgeBag();
						dfaBag.m_Action = nfaEdge.m_Data.m_Action;
						dfaBag.m_Param = nfaEdge.m_Data.m_Param;
						bags.add(dfaBag);
					}
					/* 添加当前边 */
					dfaBag.m_NFAEdges.add(nfaEdge);
					/* 添加当前状态 */
					dfaBag.m_NFAStatus.add(nfaEdge.m_End);
				}
			}
			/* 遍历当前的所有DFA边 */
			for (DFAEdgeBag bag : bags) {
				/* 检测DFA指向的状态是否存在 */
				DFAStatus status = null;
				/* 哈希字符串 */
				String hash = bag.getStatusString(NFAStatusList);
				if (DFAStatusListMap.containsKey(bag
						.getStatusString(NFAStatusList))) {
					status = DFAStatusList.get(DFAStatusListMap.get(hash));
				} else {// 不存在DFA
					status = m_StatusPool.take();
					status.m_Data.m_NFAStatus = new ArrayList<NFAStatus>(
							bag.m_NFAStatus);
					/* 检查终态 */
					for (NFAStatus nfaStatus : status.m_Data.m_NFAStatus) {
						if (nfaStatus.m_Data.m_bFinal) {
							status.m_Data.m_bFinal = true;
							break;
						}
					}
					DFAStatusList.add(status);
					DFAStatusListMap.put(hash, DFAStatusList.size() - 1);
				}
				/* 创建DFA边 */
				DFAEdge edge = connect(dfaStatus, status);
				edge.m_Data.m_Action = bag.m_Action;
				edge.m_Data.m_Param = bag.m_Param;
				edge.m_Data.m_NFAEdges = bag.m_NFAEdges;
			}
		}
		m_mainDFA = DFAStatusList.get(0);
	}

	/**
	 * DFA最小化
	 */
	private void minimization() {
		/* 终态集合 */
		ArrayList<Integer> finalStatus = new ArrayList<Integer>();
		/* 非终态集合 */
		ArrayList<Integer> nonFinalStatus = new ArrayList<Integer>();
		/* DFA状态转移表，填充终态集合 */
		int[][] transition = buildTransition(finalStatus);
		/* 填充非终态集合和状态集合的哈希表 */
		for (int i = 0; i < transition.length; i++) {
			if (!finalStatus.contains(i)) {
				nonFinalStatus.add(i);// 添加非终态序号
			}
		}
		/* DFA状态表 */
		ArrayList<DFAStatus> statusList = getDFATable();
		/* 处理终态 */
		mergeStatus(partition(finalStatus, transition), statusList);
		/* 处理非终态 */
		mergeStatus(partition(nonFinalStatus, transition), statusList);
	}

	/**
	 * 最小化划分
	 * 
	 * @param statusList
	 *            初始划分
	 * @param transition
	 *            状态转移表
	 * @return 划分
	 */
	private ArrayList<ArrayList<Integer>> partition(
			ArrayList<Integer> statusList, int[][] transition) {
		if (statusList.size() == 1) {
			/* 存放结果 */
			ArrayList<ArrayList<Integer>> pat = new ArrayList<ArrayList<Integer>>();
			pat.add(new ArrayList<Integer>(statusList.get(0)));
			return pat;
		} else {
			/* 用于查找相同状态 */
			HashMap<String, ArrayList<Integer>> map = new HashMap<String, ArrayList<Integer>>();
			for (int status : statusList) {
				/* 获得状态hash */
				String hash = getStatusLineString(transition[status]);
				/* 状态是否出现过 */
				if (map.containsKey(hash)) {
					/* 状态重复，加入上个相同状态的集合 */
					map.get(hash).add(status);
				} else {
					/* 前次出现，创建数组保存它 */
					ArrayList<Integer> set = new ArrayList<Integer>();
					set.add(status);
					map.put(hash, set);
				}
			}
			return new ArrayList<ArrayList<Integer>>(map.values());
		}
	}

	/**
	 * 合并相同状态
	 * 
	 * @param pat
	 *            状态划分
	 * @param statusList
	 *            状态转移表
	 */
	private void mergeStatus(ArrayList<ArrayList<Integer>> pat,
			ArrayList<DFAStatus> statusList) {
		/* 保存要处理的多状态合并的划分 */
		ArrayList<ArrayList<Integer>> dealWith = new ArrayList<ArrayList<Integer>>();
		for (ArrayList<Integer> collection : pat) {
			if (collection.size() > 1) {// 有多个状态
				dealWith.add(collection);
			}
		}
		/* 合并每一分组 */
		for (ArrayList<Integer> collection : dealWith) {
			/* 目标状态为集合中第一个状态，其余状态被合并 */
			int dstStatus = collection.get(0);
			/* 目标状态 */
			DFAStatus status = statusList.get(dstStatus);
			for (int i = 1; i < collection.size(); i++) {
				/* 重复的状态 */
				int srcStatus = collection.get(i);
				DFAStatus dupStatus = statusList.get(srcStatus);
				/* 备份重复状态的入边 */
				ArrayList<DFAEdge> edges = new ArrayList<DFAEdge>(
						dupStatus.m_InEdges);
				/* 将指向重复状态的边改为指向目标状态的边 */
				for (DFAEdge edge : edges) {
					/* 复制边 */
					connect(edge.m_Begin, status).m_Data = edge.m_Data;
				}
				/* 去除重复状态 */
				disconnect(dupStatus);
			}
		}
	}

	/**
	 * 获取DFA状态转移表某行的字符串
	 * 
	 * @param line
	 *            某行的索引矩阵
	 * @return 哈希字符串
	 */
	private String getStatusLineString(int[] line) {
		StringBuilder sb = new StringBuilder();
		for (int i : line) {
			sb.append(i + ",");
		}
		return sb.toString();
	}

	/**
	 * 建立状态
	 * 
	 * @param finalStatus
	 * @return 状态转换矩阵
	 */
	public int[][] buildTransition(Collection<Integer> finalStatus) {
		finalStatus.clear();
		/* DFA状态表 */
		ArrayList<DFAStatus> statusList = getDFATable();
		/* 建立状态转移矩阵 */
		int[][] transition = new int[statusList.size()][m_Map.getRanges()
				.size()];
		/* 填充状态转移表 */
		for (int i = 0; i < statusList.size(); i++) {
			DFAStatus status = statusList.get(i);
			if (status.m_Data.m_bFinal) {
				finalStatus.add(i);// 标记终态
			}
			for (int j = 0; j < transition[i].length; j++) {
				transition[i][j] = -1;// 置无效标记-1
			}
			for (DFAEdge edge : status.m_OutEdges) {
				if (edge.m_Data.m_Action == EdgeType.CHARSET) {
					transition[i][edge.m_Data.m_Param] = statusList
							.indexOf(edge.m_End);
				}
			}
		}
		return transition;
	}

	/**
	 * 提供DFA描述
	 */
	private String getDFAString() {
		/* 取得NFA所有状态 */
		ArrayList<NFAStatus> NFAStatusList = getNFAStatusClosure(
				new BreadthFirstSearch<NFAEdge, NFAStatus>(), m_mainNFA.m_Begin);
		/* 取得DFA所有状态 */
		ArrayList<DFAStatus> DFAStatusList = getDFAStatusClosure(
				new BreadthFirstSearch<DFAEdge, DFAStatus>(), m_mainDFA);
		StringBuilder sb = new StringBuilder();
		/* 生成DFA描述 */
		for (int i = 0; i < DFAStatusList.size(); i++) {
			DFAStatus status = DFAStatusList.get(i);
			/* 状态 */
			sb.append("状态[" + i + "]" + (status.m_Data.m_bFinal ? "[结束]" : "")
					+ " => " + status.m_Data.getStatusString(NFAStatusList)
					+ System.getProperty("line.separator"));
			/* 边 */
			for (DFAEdge edge : status.m_OutEdges) {
				sb.append("\t边 => [" + DFAStatusList.indexOf(edge.m_End) + "]"
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
	
	/**
	 * 获取状态转移矩阵描述
	 */
	public String getDFATableString() {
		int[][] transition = buildTransition(new ArrayList<Integer>());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < transition.length; i++) {
			for (int j = 0; j < transition[i].length; j++) {
				sb.append("\t" + transition[i][j]);
			}
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}
}