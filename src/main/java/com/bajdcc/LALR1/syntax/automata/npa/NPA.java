package com.bajdcc.LALR1.syntax.automata.npa;

import com.bajdcc.LALR1.semantic.token.ISemanticAction;
import com.bajdcc.LALR1.syntax.automata.nga.NGA;
import com.bajdcc.LALR1.syntax.automata.nga.NGAEdge;
import com.bajdcc.LALR1.syntax.automata.nga.NGAEdgeType;
import com.bajdcc.LALR1.syntax.automata.nga.NGAStatus;
import com.bajdcc.LALR1.syntax.exp.RuleExp;
import com.bajdcc.LALR1.syntax.exp.TokenExp;
import com.bajdcc.LALR1.syntax.rule.Rule;
import com.bajdcc.LALR1.syntax.rule.RuleItem;
import com.bajdcc.util.lexer.automata.BreadthFirstSearch;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * <p>
 * <strong>非确定性下推自动机</strong>（<b>NPA</b>）构成算法
 * </p>
 *
 * @author bajdcc
 */
public class NPA extends NGA {

	/**
	 * 规则集合
	 */
	private List<RuleItem> arrRuleItems = new ArrayList<>();

	/**
	 * 起始状态集合
	 */
	private List<NPAStatus> arrInitStatusList = new ArrayList<>();

	/**
	 * 语义动作集合
	 */
	private List<ISemanticAction> arrActions;

	/**
	 * 起始规则
	 */
	private Rule initRule;

	public NPA(List<RuleExp> nonterminals, List<TokenExp> terminals,
	           Rule initNonterminal, List<ISemanticAction> actions) {
		super(nonterminals, terminals);
		initRule = initNonterminal;
		arrActions = actions;
		generateNPA();
	}

	/**
	 * 连接两个状态
	 *
	 * @param begin 初态
	 * @param end   终态
	 * @return 新的边
	 */
	protected NPAEdge connect(NPAStatus begin, NPAStatus end) {
		NPAEdge edge = new NPAEdge();// 申请一条新边
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
	protected void disconnect(NPAStatus status, NPAEdge edge) {
		edge.end.inEdges.remove(edge);// 当前边的结束状态的入边集合去除当前边
	}

	/**
	 * 断开某个状态和所有边
	 *
	 * @param status 某状态
	 */
	protected void disconnect(NPAStatus status) {
		/* 清除所有入边 */
		for (Iterator<NPAEdge> it = status.inEdges.iterator(); it.hasNext(); ) {
			NPAEdge edge = it.next();
			it.remove();
			disconnect(edge.begin, edge);
		}
		/* 清除所有出边 */
		for (Iterator<NPAEdge> it = status.outEdges.iterator(); it.hasNext(); ) {
			NPAEdge edge = it.next();
			it.remove();
			disconnect(status, edge);
		}
	}

	/**
	 * 产生下推自动机
	 */
	private void generateNPA() {
		/* 下推自动机状态 */
		List<NPAStatus> NPAStatusList = new ArrayList<>();
		/* 文法自动机状态 */
		List<NGAStatus> NGAStatusList = new ArrayList<>();
		/* 下推自动机边（规则映射到NGA边） */
		Map<Rule, ArrayList<NGAEdge>> ruleEdgeMap = new HashMap<>();
		/* 遍历每条规则 */
		for (Entry<RuleItem, NGAStatus> entry : mapNGA.entrySet()) {
			RuleItem key = entry.getKey();
			NGAStatus value = entry.getValue();
			/* 保存规则 */
			arrRuleItems.add(key);
			/* 搜索当前规则中的所有状态 */
			List<NGAStatus> CurrentNGAStatusList = getNGAStatusClosure(
					new BreadthFirstSearch<>(), value);
			/* 搜索所有的边 */
			for (NGAStatus status : CurrentNGAStatusList) {
				/* 若边为非终结符边，则加入邻接表，终结符->带终结符的所有边 */
				status.outEdges.stream().filter(edge -> edge.data.kAction == NGAEdgeType.RULE).forEach(edge -> {
					Rule rule = edge.data.rule.rule;
					if (!ruleEdgeMap.containsKey(rule)) {
						ruleEdgeMap.put(rule, new ArrayList<>());
					}
					ruleEdgeMap.get(rule).add(edge);
				});
			}
			/* 为所有的NGA状态构造对应的NPA状态，为一一对应 */
			for (NGAStatus status : CurrentNGAStatusList) {
				/* 保存NGA状态 */
				NGAStatusList.add(status);
				/* 新建NPA状态 */
				NPAStatus NPAStatus = new NPAStatus();
				NPAStatus.data.label = status.data.label;
				NPAStatus.data.iRuleItem = arrRuleItems.indexOf(key);
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
			RuleItem ruleItem = arrRuleItems.get(npaStatus.data.iRuleItem);
			/* 检查是否为纯左递归，类似[A::=Aa]此类，无法直接添加纯左递归边，需要LA及归约 */
			if (isNoLeftRecursiveStatus(ngaStatus, ruleItem.parent)) {
				/* 当前状态是否为初始状态且推导规则是否属于起始规则（无NGA入边） */
				boolean isInitRuleStatus = initRule == ruleItem.parent;
				/* 若是，则将当前状态对应的NPA状态加入初始状态表中 */
				if (ngaStatus.inEdges.isEmpty() && isInitRuleStatus) {
					arrInitStatusList.add(npaStatus);
				}
				/* 建立计算优先级使用的记号表，其中元素为从当前状态出发的Rule或Token边的First集（LA预查优先） */
				HashSet<Integer> tokenSet = new HashSet<>();
				/* 遍历文法自动机的所有边 */
				for (NGAEdge edge : ngaStatus.outEdges) {
					switch (edge.data.kAction) {
						case EPSILON:
							break;
						case RULE:
							/* 判断边是否为纯左递归 */
							if (!isLeftRecursiveEdge(edge, ruleItem.parent)) {
								for (RuleItem item : edge.data.rule.rule.arrRules) {
									/* 起始状态 */
									NGAStatus initItemStatus = mapNGA.get(item);
									/* 判断状态是否为纯左递归 */
									if (isNoLeftRecursiveStatus(initItemStatus,
											item.parent)) {
										/* 添加Shift边，功能为将一条状态序号放入堆栈顶 */
										NPAEdge npaEdge = connect(npaStatus,
												NPAStatusList.get(NGAStatusList
														.indexOf(initItemStatus)));
										npaEdge.data.handler = edge.data.handler;
										npaEdge.data.action = edge.data.action;
										npaEdge.data.kAction = NPAEdgeType.SHIFT;
										npaEdge.data.inst = NPAInstruction.SHIFT;
										npaEdge.data.errorJump = NPAStatusList
												.get(NGAStatusList
														.indexOf(edge.end));
										/* 为移进项目构造LookAhead表，LA不吃字符，只是单纯压入新的状态（用于规约） */
										npaEdge.data.arrLookAhead = new HashSet<>();
										npaEdge.data.arrLookAhead.addAll(item.setFirstSetTokens.stream().filter(exp -> !tokenSet.contains(exp.id)).map(exp -> exp.id).collect(Collectors.toList()));
									}
								}
								// 将当前非终结符的所有终结符First集加入tokenSet，以便非终结符的Move的LA操作（优先级）
								tokenSet.addAll(edge.data.rule.rule.arrTokens.stream().map(exp -> exp.id).collect(Collectors.toList()));
							}
							break;
						case TOKEN:
							/* 添加Move边，功能为吃掉（匹配）一个终结符，若终结符不匹配，则报错（即不符合文法） */
							NPAEdge npaEdge = connect(npaStatus,
									NPAStatusList.get(NGAStatusList
											.indexOf(edge.end)));
							npaEdge.data.handler = edge.data.handler;
							npaEdge.data.action = edge.data.action;
							npaEdge.data.kAction = NPAEdgeType.MOVE;
							npaEdge.data.iToken = edge.data.token.id;
							npaEdge.data.iHandler = arrActions
									.indexOf(edge.data.action);
							npaEdge.data.errorJump = npaEdge.end;
							/* 根据StorageID配置指令 */
							if (edge.data.iStorage != -1) {
								npaEdge.data.inst = NPAInstruction.READ;
								npaEdge.data.iIndex = edge.data.iStorage;// 参数
							} else {
								npaEdge.data.inst = NPAInstruction.PASS;
							}
							/* 修改TokenSet */
							if (tokenSet.contains(edge.data.token.id)) {
								/* 使用LookAhead表 */
								npaEdge.data.arrLookAhead = new HashSet<>();
							} else {
								tokenSet.add(edge.data.token.id);
							}
							break;
						default:
							break;
					}
				}
				/* 如果当前NGA状态是结束状态（此时要进行规约），则检查是否需要添加其他边 */
				if (ngaStatus.data.bFinal) {
					if (ruleEdgeMap.containsKey(ruleItem.parent)) {
						/* 遍历文法自动机中附带了当前推导规则所属规则的边 */
						List<NGAEdge> ruleEdges = ruleEdgeMap
								.get(ruleItem.parent);// 当前规约的文法的非终结符为A，获得包含A的所有边
						for (NGAEdge ngaEdge : ruleEdges) {
							/* 判断纯左递归，冗长的表达式是为了获得当前边的所在推导式的起始非终结符 */
							if (isLeftRecursiveEdge(
									ngaEdge,
									arrRuleItems.get(NPAStatusList
											.get(NGAStatusList
													.indexOf(ngaEdge.begin)).data.iRuleItem).parent)) {
								/* 添加Left Recursion边（特殊的Reduce边） */
								NPAEdge npaEdge = connect(npaStatus,
										NPAStatusList.get(NGAStatusList
												.indexOf(ngaEdge.end)));
								npaEdge.data.kAction = NPAEdgeType.LEFT_RECURSION;
								if (ngaEdge.data.iStorage != -1) {
									npaEdge.data.inst = NPAInstruction.LEFT_RECURSION;
									npaEdge.data.iIndex = ngaEdge.data.iStorage;
								} else {
									npaEdge.data.inst = NPAInstruction.LEFT_RECURSION_DISCARD;
								}
								npaEdge.data.iHandler = npaStatus.data.iRuleItem;// 规约的规则
								/* 为左递归构造Lookahead表（Follow集），若LA成功则进入左递归 */
								npaEdge.data.arrLookAhead = new HashSet<>();
								for (NGAEdge edge : ngaEdge.end.outEdges) {
									/* 若出边为终结符，则直接加入（终结符First集仍是本身） */
									if (edge.data.kAction == NGAEdgeType.TOKEN) {
										npaEdge.data.arrLookAhead
												.add(edge.data.token.id);
									} else {
										/* 若出边为非终结符，则加入非终结符的First集 */
										npaEdge.data.arrLookAhead.addAll(edge.data.rule.rule.arrTokens.stream().map(exp -> exp.id).collect(Collectors.toList()));
									}
								}
							} else {
								/* 添加Reduce边 */
								NPAEdge npaEdge = connect(npaStatus,
										NPAStatusList.get(NGAStatusList
												.indexOf(ngaEdge.end)));
								npaEdge.data.kAction = NPAEdgeType.REDUCE;
								npaEdge.data.status = NPAStatusList
										.get(NGAStatusList
												.indexOf(ngaEdge.begin));
								if (ngaEdge.data.iStorage != -1) {
									npaEdge.data.inst = NPAInstruction.TRANSLATE;
									npaEdge.data.iIndex = ngaEdge.data.iStorage;
								} else {
									npaEdge.data.inst = NPAInstruction.TRANSLATE_DISCARD;
								}
								npaEdge.data.iHandler = npaStatus.data.iRuleItem;// 规约的规则
							}
						}
					}
					if (isInitRuleStatus) {
						/* 添加Finish边 */
						NPAEdge npaEdge = connect(npaStatus, npaStatus);
						npaEdge.data.kAction = NPAEdgeType.FINISH;
						npaEdge.data.inst = NPAInstruction.TRANSLATE_FINISH;
						npaEdge.data.iHandler = npaStatus.data.iRuleItem;
					}
				}
			}
		}
	}

	/**
	 * 判断状态是否不是纯左递归
	 *
	 * @param status NGA状态
	 * @param rule   规则
	 * @return 状态是否不是左递归
	 */
	private static boolean isNoLeftRecursiveStatus(NGAStatus status, Rule rule) {
		if (!status.inEdges.isEmpty())// 有入边，则无纯左递归
		{
			return true;
		}
		for (NGAEdge edge : status.outEdges) {
			if (edge.data.kAction != NGAEdgeType.RULE
					|| edge.data.rule.rule != rule) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断边是否为纯左递归
	 *
	 * @param edge NGA边
	 * @param rule 规则
	 * @return 状态是否为左递归
	 */
	private static boolean isLeftRecursiveEdge(NGAEdge edge, Rule rule) {
		// 没有入边
		return edge.begin.inEdges.isEmpty() && edge.data.kAction == NGAEdgeType.RULE && edge.data.rule.rule == rule;
	}

	/**
	 * 获取NPA状态闭包
	 *
	 * @param bfs    遍历算法
	 * @param status 初态
	 * @return 初态闭包
	 */
	protected static List<NPAStatus> getNGAStatusClosure(
			BreadthFirstSearch<NPAEdge, NPAStatus> bfs, NPAStatus status) {
		status.visit(bfs);
		return bfs.getArrStatus();
	}

	/**
	 * 获得NPA初态
	 *
	 * @return NPA初态表
	 */
	public List<NPAStatus> getInitStatusList() {
		return arrInitStatusList;
	}

	/**
	 * 获得NPA所有状态
	 *
	 * @return NPA状态表
	 */
	public List<NPAStatus> getNPAStatusList() {
		List<NPAStatus> NPAStatusList = new ArrayList<>();
		for (NPAStatus status : arrInitStatusList) {
			NPAStatusList.addAll(getNGAStatusClosure(
					new BreadthFirstSearch<>(), status));
		}
		return NPAStatusList;
	}

	/**
	 * 获得所有推导式
	 *
	 * @return NPA状态表
	 */
	public List<RuleItem> getRuleItems() {
		return arrRuleItems;
	}

	/**
	 * 获得NPA描述
	 *
	 * @return NPA描述
	 */
	public String getNPAString() {
		StringBuilder sb = new StringBuilder();
		/* 构造状态路径 */
		List<NPAStatus> statusList = getNPAStatusList();
		/* 输出初始状态 */
		sb.append("#### 初始状态 ####");
		sb.append(System.lineSeparator());
		for (int i = 0; i < arrInitStatusList.size(); i++) {
			sb.append("状态[").append(i).append("]： ").append(arrInitStatusList.get(i).data.label);
			sb.append(System.lineSeparator());
		}
		sb.append(System.lineSeparator());
		/* 遍历状态 */
		sb.append("#### 状态转换图 ####");
		sb.append(System.lineSeparator());
		for (int i = 0; i < statusList.size(); i++) {
			NPAStatus status = statusList.get(i);
			/* 输出状态标签 */
			sb.append("状态[").append(i).append("]： ");
			sb.append(System.lineSeparator());
			sb.append("\t项目：").append(status.data.label);
			sb.append(System.lineSeparator());
			sb.append("\t规则：").append(arrRuleItems.get(status.data.iRuleItem).parent.nonTerminal.name);
			sb.append(System.lineSeparator());
			/* 输出边 */
			for (NPAEdge edge : status.outEdges) {
				sb.append("\t\t----------------");
				sb.append(System.lineSeparator());
				/* 输出边的目标 */
				sb.append("\t\t到达状态[").append(statusList.indexOf(edge.end)).append("]: ").append(edge.end.data.label);
				sb.append(System.lineSeparator());
				/* 输出边的类型 */
				sb.append("\t\t类型：").append(edge.data.kAction.getName());
				switch (edge.data.kAction) {
					case FINISH:
						break;
					case LEFT_RECURSION:
						break;
					case MOVE:
						sb.append("\t=> ").append(edge.data.iToken).append("(").append(arrTerminals.get(edge.data.iToken)).append(")");
						break;
					case REDUCE:
						sb.append("\t=> ").append(edge.data.status.data.label);
						break;
					case SHIFT:
						break;
					default:
						break;
				}
				sb.append(System.lineSeparator());
				/* 输出边的指令 */
				sb.append("\t\t指令：").append(edge.data.inst.getName());
				switch (edge.data.inst) {
					case PASS:
						break;
					case READ:
						sb.append("\t=> ").append(edge.data.iIndex);
						break;
					case SHIFT:
						break;
					case LEFT_RECURSION:
					case TRANSLATE:
						sb.append("\t=> ").append(arrRuleItems.get(edge.data.iHandler).parent.nonTerminal.name).append(" ").append(edge.data.iIndex);
						break;
					case LEFT_RECURSION_DISCARD:
					case TRANSLATE_DISCARD:
					case TRANSLATE_FINISH:
						sb.append("\t=> ").append(arrRuleItems.get(edge.data.iHandler).parent.nonTerminal.name);
						break;
					default:
						break;
				}
				if (edge.data.action != null)
					sb.append("  ").append("[Action]");
				sb.append(System.lineSeparator());
				/* 输出Lookahead表 */
				if (edge.data.arrLookAhead != null) {
					sb.append("\t\t预查：");
					for (int id : edge.data.arrLookAhead) {
						sb.append("[").append(arrTerminals.get(id)).append("]");
					}
					sb.append(System.lineSeparator());
				}
			}
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return getNPAString();
	}
}
