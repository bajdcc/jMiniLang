package priv.bajdcc.syntax.automata.npa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import org.vibur.objectpool.ConcurrentLinkedPool;
import org.vibur.objectpool.PoolService;

import priv.bajdcc.lexer.automata.BreadthFirstSearch;
import priv.bajdcc.syntax.Rule;
import priv.bajdcc.syntax.RuleItem;
import priv.bajdcc.syntax.automata.nga.NGA;
import priv.bajdcc.syntax.automata.nga.NGAEdge;
import priv.bajdcc.syntax.automata.nga.NGAEdgeType;
import priv.bajdcc.syntax.automata.nga.NGAStatus;
import priv.bajdcc.syntax.exp.RuleExp;
import priv.bajdcc.syntax.exp.TokenExp;
import priv.bajdcc.utility.ObjectFactory;

/**
 * <p>
 * <strong>非确定性下推自动机</strong>（<b>NPA</b>）构成算法
 * </p>
 * 
 * @author bajdcc
 *
 */
public class NPA extends NGA {

	/**
	 * 规则集合
	 */
	ArrayList<RuleItem> m_arrRuleItems = new ArrayList<RuleItem>();

	/**
	 * 起始状态集合
	 */
	ArrayList<NPAStatus> m_arrInitStatusList = new ArrayList<NPAStatus>();

	/**
	 * 起始规则
	 */
	Rule m_initRule = null;

	/**
	 * 边对象池
	 */
	private PoolService<NPAEdge> m_EdgesPool = new ConcurrentLinkedPool<NPAEdge>(
			new ObjectFactory<NPAEdge>() {
				public NPAEdge create() {
					return new NPAEdge();
				};
			}, 1024, 10240, false);

	/**
	 * 状态对象池
	 */
	private PoolService<NPAStatus> m_StatusPool = new ConcurrentLinkedPool<NPAStatus>(
			new ObjectFactory<NPAStatus>() {
				public NPAStatus create() {
					return new NPAStatus();
				};
			}, 1024, 10240, false);

	public NPA(ArrayList<RuleExp> nonterminals, ArrayList<TokenExp> terminals,
			Rule initNonterminal) {
		super(nonterminals, terminals);
		m_initRule = initNonterminal;
		generateNPA();
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
	protected NPAEdge connect(NPAStatus begin, NPAStatus end) {
		NPAEdge edge = m_EdgesPool.take();// 申请一条新边
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
	protected void disconnect(NPAStatus status, NPAEdge edge) {
		edge.m_End.m_InEdges.remove(edge);// 当前边的结束状态的入边集合去除当前边
		m_EdgesPool.restore(edge);
	}

	/**
	 * 断开某个状态和所有边
	 * 
	 * @param begin
	 *            某状态
	 */
	protected void disconnect(NPAStatus status) {
		/* 清除所有入边 */
		for (Iterator<NPAEdge> it = status.m_InEdges.iterator(); it.hasNext();) {
			NPAEdge edge = it.next();
			it.remove();
			disconnect(edge.m_Begin, edge);
		}
		/* 清除所有出边 */
		for (Iterator<NPAEdge> it = status.m_OutEdges.iterator(); it.hasNext();) {
			NPAEdge edge = it.next();
			it.remove();
			disconnect(status, edge);
		}
		m_StatusPool.restore(status);
	}

	/**
	 * 产生下推自动机
	 */
	private void generateNPA() {
		/* 下推自动机状态 */
		ArrayList<NPAStatus> NPAStatusList = new ArrayList<NPAStatus>();
		/* 文法自动机状态 */
		ArrayList<NGAStatus> NGAStatusList = new ArrayList<NGAStatus>();
		/* 下推自动机边（规则映射到NGA边） */
		HashMap<Rule, ArrayList<NGAEdge>> ruleEdgeMap = new HashMap<Rule, ArrayList<NGAEdge>>();
		/* 遍历每条规则 */
		for (Entry<RuleItem, NGAStatus> entry : m_mapNGA.entrySet()) {
			/* 保存规则 */
			m_arrRuleItems.add(entry.getKey());
			/* 搜索当前规则中的所有状态 */
			ArrayList<NGAStatus> CurrentNGAStatusList = getNGAStatusClosure(
					new BreadthFirstSearch<NGAEdge, NGAStatus>(),
					entry.getValue());
			/* 搜索所有的边 */
			for (NGAStatus status : CurrentNGAStatusList) {
				for (NGAEdge edge : status.m_OutEdges) {
					/* 将边为非终结符边，则加入邻接表 */
					if (edge.m_Data.m_Action == NGAEdgeType.RULE) {
						Rule rule = edge.m_Data.m_Rule.m_Rule;
						if (!ruleEdgeMap.containsKey(rule)) {
							ruleEdgeMap.put(rule, new ArrayList<NGAEdge>());
						}
						ruleEdgeMap.get(rule).add(edge);
					}
				}
			}
			/* 为所有的NGA状态构造对应的NPA状态 */
			for (NGAStatus status : CurrentNGAStatusList) {
				/* 保存NGA状态 */
				NGAStatusList.add(status);
				/* 新建NPA状态 */
				NPAStatus NPAStatus = m_StatusPool.take();
				NPAStatus.m_Data.m_strLabel = status.m_Data.m_strLabel;
				NPAStatus.m_Data.m_iRuleItem = m_arrRuleItems.indexOf(entry
						.getKey());
				NPAStatusList.add(NPAStatus);
			}
		}
		/* 遍历所有NPA状态 */
		for (int i = 0; i < NPAStatusList.size(); i++) {
			/* 获得NGA状态 */
			NGAStatus ngaStatus = NGAStatusList.get(i);
			/* 获得NPA状态 */
			NPAStatus npaStatus = NPAStatusList.get(i);
			/* 获得规则 */
			RuleItem ruleItem = m_arrRuleItems
					.get(npaStatus.m_Data.m_iRuleItem);
			/* 检查是否为纯左递归 */
			if (!isLeftRecursiveStatus(ngaStatus, ruleItem.m_Parent)) {
				/* 当前状态是否为初始状态且推导规则是否属于起始规则 */
				boolean isInitRuleStatus = m_initRule == ruleItem.m_Parent;
				/* 若是，则将当前状态对应的下推自动机状态加入初始状态表中 */
				if (npaStatus.m_InEdges.isEmpty() && isInitRuleStatus) {
					m_arrInitStatusList.add(npaStatus);
				}
				/* 建立计算优先级使用的记号表 */
				HashSet<Integer> tokenSet = new HashSet<Integer>();
				/* 遍历文法自动机的所有边 */
				for (NGAEdge edge : ngaStatus.m_OutEdges) {
					switch (edge.m_Data.m_Action) {
					case EPSILON:
						break;
					case RULE:
						/* 判断边是否为纯左递归 */
						if (!isLeftRecursiveEdge(edge, ruleItem.m_Parent)) {
							/* 添加Shift边 */
							for (RuleItem item : edge.m_Data.m_Rule.m_Rule.m_arrRules) {
								/* 起始状态 */
								NGAStatus initItemStatus = m_mapNGA.get(item);
								/* 判断状态是否为纯左递归 */
								if (!isLeftRecursiveStatus(initItemStatus,
										item.m_Parent)) {
									NPAEdge npaEdge = connect(npaStatus,
											NPAStatusList.get(NGAStatusList
													.indexOf(initItemStatus)));
									npaEdge.m_Data.m_Handler = edge.m_Data.m_Handler;
									npaEdge.m_Data.m_Action = NPAEdgeType.SHIFT;
									npaEdge.m_Data.m_ErrorJump = NPAStatusList
											.get(NGAStatusList
													.indexOf(edge.m_End));

									/* 构造LookAhead表 */
									npaEdge.m_Data.m_arrLookAhead = new HashSet<Integer>();
									for (TokenExp exp : item.m_arrFirstSetTokens) {
										int id = exp.m_iID;
										if (!tokenSet.contains(id)) {
											npaEdge.m_Data.m_arrLookAhead
													.add(id);
										}
									}
								}
							}
							for (TokenExp exp : edge.m_Data.m_Rule.m_Rule.m_arrTokens) {
								tokenSet.add(exp.m_iID);
							}
						}
						break;
					case TOKEN:
						/* 添加Move边 */
						NPAEdge npaEdge = connect(npaStatus,
								NPAStatusList.get(NGAStatusList
										.indexOf(edge.m_End)));
						npaEdge.m_Data.m_Handler = edge.m_Data.m_Handler;
						npaEdge.m_Data.m_Action = NPAEdgeType.MOVE;
						npaEdge.m_Data.m_iToken = edge.m_Data.m_Token.m_iID;
						npaEdge.m_Data.m_ErrorJump = npaEdge.m_End;
						/* 根据StorageID配置指令 */
						if (edge.m_Data.m_iStorage != -1) {
							npaEdge.m_Data.m_Inst = NPAInstruction.READ;
							npaEdge.m_Data.m_iIndex = edge.m_Data.m_iStorage;// 参数
						} else {
							npaEdge.m_Data.m_Inst = NPAInstruction.ACCEPT;
						}
						/* 修改TokenSet */
						if (tokenSet.contains(edge.m_Data.m_Token.m_iID)) {
							/* 使用LookAhead表 */
							npaEdge.m_Data.m_arrLookAhead = new HashSet<Integer>();
						} else {
							npaEdge.m_Data.m_Inst = NPAInstruction.ACCEPT;
						}
						break;
					default:
						break;
					}
				}
				/* 如果当前NGA状态是结束状态，则检查是否需要添加其他边 */
				if (ngaStatus.m_Data.m_bFinal) {
					if (ruleEdgeMap.containsKey(ruleItem.m_Parent)) {
						/* 遍历文法自动机中附带了当前推导规则所属规则的边 */
						ArrayList<NGAEdge> ruleEdges = ruleEdgeMap
								.get(ruleItem.m_Parent);
						for (NGAEdge ngaEdge : ruleEdges) {
							if (isLeftRecursiveEdge(
									ngaEdge,
									m_arrRuleItems.get(NPAStatusList
											.get(NGAStatusList
													.indexOf(ngaEdge.m_Begin)).m_Data.m_iRuleItem).m_Parent)) {
								/* 添加Left Recursion边 */
								NPAEdge npaEdge = connect(npaStatus,
										NPAStatusList.get(NGAStatusList
												.indexOf(ngaEdge.m_End)));
								npaEdge.m_Data.m_Action = NPAEdgeType.LEFT_RECURSION;
								if (npaEdge.m_Data.m_arrLookAhead != null) {
									npaEdge.m_Data.m_Inst = NPAInstruction.LEFT_RECURSION;
									npaEdge.m_Data.m_iIndex = ngaEdge.m_Data.m_iStorage;
								} else {
									npaEdge.m_Data.m_Inst = NPAInstruction.LEFT_RECURSION_DISCARD;
								}
								npaEdge.m_Data.m_iHandler = npaStatus.m_Data.m_iRuleItem;
								/* 构造Lookahead表 */
								npaEdge.m_Data.m_arrLookAhead = new HashSet<Integer>();
								for (NGAEdge edge : ngaEdge.m_End.m_OutEdges) {
									if (edge.m_Data.m_Action == NGAEdgeType.TOKEN) {
										npaEdge.m_Data.m_arrLookAhead
												.add(edge.m_Data.m_Token.m_iID);
									} else {
										for (TokenExp exp : edge.m_Data.m_Rule.m_Rule.m_arrTokens) {
											npaEdge.m_Data.m_arrLookAhead
													.add(exp.m_iID);
										}
									}
								}
							} else {
								/* 添加Reduce边 */
								NPAEdge npaEdge = connect(npaStatus,
										NPAStatusList.get(NGAStatusList
												.indexOf(ngaEdge.m_End)));
								npaEdge.m_Data.m_Action = NPAEdgeType.REDUCE;
								npaEdge.m_Data.m_Status = NPAStatusList
										.get(NGAStatusList
												.indexOf(ngaEdge.m_Begin));
								if (npaEdge.m_Data.m_arrLookAhead != null) {
									npaEdge.m_Data.m_Inst = NPAInstruction.TRANSLATE;
									npaEdge.m_Data.m_iIndex = ngaEdge.m_Data.m_iStorage;
								} else {
									npaEdge.m_Data.m_Inst = NPAInstruction.TRANSLATE_DISCARD;
								}
								npaEdge.m_Data.m_iHandler = npaStatus.m_Data.m_iRuleItem;
							}
						}
					}
					if (isInitRuleStatus) {
						/* 添加Finish边 */
						NPAEdge npaEdge = connect(npaStatus, npaStatus);
						npaEdge.m_Data.m_Action = NPAEdgeType.FINISH;
						npaEdge.m_Data.m_Inst = NPAInstruction.TRANSLATE_FINISH;
						npaEdge.m_Data.m_iHandler = npaStatus.m_Data.m_iRuleItem;
					}
				}
			}
		}
	}

	/**
	 * 判断状态是否为纯左递归
	 * 
	 * @param status
	 *            NGA状态
	 * @param rule
	 *            规则
	 * @return 状态是否为左递归
	 */
	private static boolean isLeftRecursiveStatus(NGAStatus status, Rule rule) {
		if (status.m_InEdges.isEmpty())// 没有入边，则无纯左递归
		{
			return false;
		}
		for (NGAEdge edge : status.m_OutEdges) {
			if (edge.m_Data.m_Action != NGAEdgeType.RULE
					|| edge.m_Data.m_Rule.m_Rule != rule) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断边是否为纯左递归
	 * 
	 * @param status
	 *            NGA状态
	 * @param rule
	 *            规则
	 * @return 状态是否为左递归
	 */
	private static boolean isLeftRecursiveEdge(NGAEdge edge, Rule rule) {
		if (edge.m_Begin.m_InEdges.isEmpty())// 没有入边
		{
			return edge.m_Data.m_Action != NGAEdgeType.RULE
					&& edge.m_Data.m_Rule.m_Rule == rule;
		}
		return false;
	}

	/**
	 * 获取NPA状态闭包
	 * 
	 * @param bfs
	 *            遍历算法
	 * @param status
	 *            初态
	 * @return 初态闭包
	 */
	protected static ArrayList<NPAStatus> getNGAStatusClosure(
			BreadthFirstSearch<NPAEdge, NPAStatus> bfs, NPAStatus status) {
		status.visit(bfs);
		return bfs.m_arrStatus;
	}

	/**
	 * 获得NPA描述
	 */
	public String getNPAString() {
		StringBuilder sb = new StringBuilder();
		HashSet<NPAStatus> statusSet = new HashSet<NPAStatus>(
				m_arrInitStatusList);
		ArrayList<NPAStatus> statusList = new ArrayList<NPAStatus>();
		/* 构造状态路径 */
		for (NPAStatus status : m_arrInitStatusList) {
			statusSet.addAll(getNGAStatusClosure(
					new BreadthFirstSearch<NPAEdge, NPAStatus>(), status));
		}
		statusList.addAll(statusSet);
		/* 输出初始状态 */
		sb.append("#### 初始状态 ####");
		sb.append(System.getProperty("line.separator"));
		for (int i = 0; i < m_arrInitStatusList.size(); i++) {
			sb.append("状态[" + i + "]： "
					+ m_arrInitStatusList.get(i).m_Data.m_strLabel);
			sb.append(System.getProperty("line.separator"));
		}
		sb.append(System.getProperty("line.separator"));
		/* 遍历状态 */
		sb.append("#### 状态转换图 ####");
		sb.append(System.getProperty("line.separator"));
		for (int i = 0; i < statusList.size(); i++) {
			NPAStatus status = statusList.get(i);
			/* 输出状态标签 */
			sb.append("状态[" + i + "]： ");
			sb.append(System.getProperty("line.separator"));
			sb.append("\t标签[" + i + "]： " + status.m_Data.m_strLabel);
			sb.append(System.getProperty("line.separator"));
			sb.append("\t规则["
					+ i
					+ "]： "
					+ m_arrRuleItems.get(status.m_Data.m_iRuleItem).m_Parent.m_nonTerminal.m_strName);
			sb.append(System.getProperty("line.separator"));
			/* 输出边 */
			for (NPAEdge edge : status.m_OutEdges) {
				sb.append("\t\t----------------");
				sb.append(System.getProperty("line.separator"));
				/* 输出边的目标 */
				sb.append("\t\t到达 " + statusList.indexOf(edge.m_End) + " : "
						+ edge.m_End.m_Data.m_strLabel);
				sb.append(System.getProperty("line.separator"));
				/* 输出边的类型 */
				sb.append("\t\t类型：" + edge.m_Data.m_Action.getName());
				switch (edge.m_Data.m_Action) {
				case FINISH:
					break;
				case LEFT_RECURSION:
					break;
				case MOVE:
					sb.append("\t=> " + edge.m_Data.m_iToken + "("
							+ m_arrTerminals.get(edge.m_Data.m_iToken) + ")");
					break;
				case REDUCE:
					sb.append("\t=> " + edge.m_Data.m_Status.m_Data.m_strLabel);
					break;
				case SHIFT:
					break;
				default:
					break;
				}
				sb.append(System.getProperty("line.separator"));
				/* 输出边的指令 */
				sb.append("\t\t指令：" + edge.m_Data.m_Inst.getName());
				switch (edge.m_Data.m_Inst) {
				case ACCEPT:
					break;
				case READ:
					sb.append("\t=> " + edge.m_Data.m_iIndex);
					break;
				case SHIFT:
					break;
				case LEFT_RECURSION:
				case TRANSLATE:
					sb.append("\t=> "
							+ m_arrRuleItems.get(edge.m_Data.m_iHandler).m_Parent.m_nonTerminal.m_strName
							+ " " + edge.m_Data.m_iIndex);
					break;
				case LEFT_RECURSION_DISCARD:
				case TRANSLATE_DISCARD:
				case TRANSLATE_FINISH:
					sb.append("\t=> "
							+ m_arrRuleItems.get(edge.m_Data.m_iHandler).m_Parent.m_nonTerminal.m_strName);
					break;
				default:
					break;
				}
				sb.append(System.getProperty("line.separator"));
				/* 输出Lookahead表 */
				if (edge.m_Data.m_arrLookAhead != null) {
					sb.append("\t\t预查：");
					for (int id : edge.m_Data.m_arrLookAhead) {
						sb.append("[" + id + "(" + m_arrTerminals.get(id)
								+ ")]");
					}
					sb.append(System.getProperty("line.separator"));
				}
			}
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return getNPAString();
	}
}
