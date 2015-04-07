package priv.bajdcc.semantic;

import java.util.ArrayList;

import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.regex.IRegexStringIterator;
import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.lexer.token.TokenType;
import priv.bajdcc.semantic.lexer.TokenFactory;
import priv.bajdcc.semantic.token.ISemanticHandler;
import priv.bajdcc.semantic.token.ParsingStack;
import priv.bajdcc.semantic.tracker.ErrorRecord;
import priv.bajdcc.semantic.tracker.Instruction;
import priv.bajdcc.semantic.tracker.InstructionRecord;
import priv.bajdcc.semantic.tracker.Tracker;
import priv.bajdcc.semantic.tracker.TrackerError;
import priv.bajdcc.semantic.tracker.TrackerResource;
import priv.bajdcc.syntax.RuleItem;
import priv.bajdcc.syntax.Syntax;
import priv.bajdcc.syntax.automata.npa.NPAEdge;
import priv.bajdcc.syntax.automata.npa.NPAStatus;
import priv.bajdcc.syntax.error.IErrorHandler;
import priv.bajdcc.syntax.error.SyntaxException;
import priv.bajdcc.syntax.exp.TokenExp;
import priv.bajdcc.utility.TrackerErrorBag;

/**
 * 语义分析
 *
 * @author bajdcc
 */
/**
 *
 *
 * @author bajdcc
 */
public class Semantic extends Syntax implements IErrorHandler {

	/**
	 * 单词流工厂
	 */
	private TokenFactory m_TokenFactory = null;

	/**
	 * 错误处理器
	 */
	private IErrorHandler m_defErrorHandler = this;

	/**
	 * 存放生成的指令
	 */
	private ArrayList<Instruction> m_arrInsts = new ArrayList<Instruction>();

	/**
	 * 存放生成过程中的错误
	 */
	private ArrayList<TrackerError> m_arrErrors = new ArrayList<TrackerError>();

	/**
	 * 单词流
	 */
	private ArrayList<Token> m_arrTokens = new ArrayList<Token>();

	/**
	 * 当前的语义接口
	 */
	private ISemanticHandler m_Handler = null;

	/**
	 * 语义分析结果
	 */
	private Object m_Object = null;

	/**
	 * 设置错误处理器
	 * 
	 * @param handler
	 *            错误处理器接口
	 */
	public void setErrorHandler(IErrorHandler handler) {
		m_defErrorHandler = handler;
	}

	public Semantic(String context) throws RegexException {
		super(true);
		m_TokenFactory = new TokenFactory(context);// 用于分析的文本
		m_TokenFactory.discard(TokenType.COMMENT);
		m_TokenFactory.discard(TokenType.WHITESPACE);
		m_TokenFactory.discard(TokenType.ERROR);
		m_TokenFactory.scan();
	}

	/**
	 * @param handler
	 *            语义分析接口
	 * @param inferString
	 *            文法推导式
	 * @throws SyntaxException
	 */
	public void infer(ISemanticHandler handler, String inferString)
			throws SyntaxException {
		if (handler == null) {
			throw new NullPointerException("handler");
		}
		m_Handler = handler;
		super.infer(inferString);
		m_Handler = null;
	}

	@Override
	protected void onAddRuleItem(RuleItem item) {
		item.m_Handler = m_Handler;
	}

	/**
	 * 初始化
	 * 
	 * @param startSymbol
	 *            开始符号
	 * @throws SyntaxException
	 */
	public void initialize(String startSymbol) throws SyntaxException {
		super.initialize(startSymbol);
		analysis();
		run();
	}

	/**
	 * 开始分析工作
	 */
	private void analysis() {
		/* 跟踪器资源 */
		TrackerResource trackerResource = new TrackerResource();
		/* 可用NPA边表 */
		ArrayList<NPAEdge> aliveEdgeList = new ArrayList<NPAEdge>();
		/* 结束边 */
		NPAEdge finalEdge = null;
		/* 没有错误的跟踪器数量 */
		int trackerWithoutErrorCount = 0;
		/* 初始PDA状态集合 */
		ArrayList<NPAStatus> initStatusList = m_NPA.getInitStatusList();
		/* 清空结果 */
		m_arrInsts.clear();
		m_arrErrors.clear();
		/* 初始化跟踪器 */
		for (NPAStatus npaStatus : initStatusList) {
			Tracker tracker = trackerResource.addTracker();
			tracker.m_rcdInst = trackerResource.addInstRecord(null);
			tracker.m_rcdError = trackerResource.addErrorRecord(null);
			tracker.m_npaStatus = npaStatus;
			tracker.m_iterToken = m_TokenFactory;
			trackerWithoutErrorCount++;
		}
		/* 是否分析成功 */
		boolean success = false;
		/* 进行分析 */
		while (trackerResource.m_headTracker != null && !success) {
			Tracker tracker = trackerResource.m_headTracker;
			while (tracker != null) {
				Tracker nextTracker = tracker.m_nextTracker;				
				/* 对每一个跟踪器进行计算，构造level记录可用边优先级，可以防止冲突 */
				/* 匹配=0 移进=0 左递归=1 归约=2 */
				/* 对终结符则移进，对非终结符则匹配，互不冲突，故两者优先级相同 */
				/* 左递归属于特殊的归约，区别是归约后不出栈 */
				int level = 2;
				/* 筛选边 */
				aliveEdgeList.clear();
				finalEdge = null;
				/* 遍历出边 */
				for (NPAEdge npaEdge : tracker.m_npaStatus.m_OutEdges) {
					int sublevel = -1;
					/* 若当前边使用了LA表，则输入不满足LA表时会被丢弃 */
					if (npaEdge.m_Data.m_arrLookAhead != null) {
						if (!npaEdge.m_Data.m_arrLookAhead
								.contains(getTokenId(tracker))) {
							continue;
						}
					}
					switch (npaEdge.m_Data.m_Action) {
					case FINISH:
						/* 检查状态堆栈是否为空 */
						if (tracker.m_stkStatus.isEmpty()) {
							finalEdge = npaEdge;
						}
						continue;
					case LEFT_RECURSION:
						sublevel = 1;
						break;
					case MOVE:
						/* 检查Move所需要的记号跟输入是否一致（匹配） */
						if (npaEdge.m_Data.m_iToken != getTokenId(tracker)) {
							continue;// 失败
						} else {
							sublevel = 0;
						}
						break;
					case REDUCE:
						/* 检查Reduce所需要的栈状态跟状态堆栈的栈顶元素是否一致 */
						if (tracker.m_stkStatus.isEmpty()
								|| npaEdge.m_Data.m_Status != tracker.m_stkStatus
										.peek()) {
							continue;// 失败
						} else {
							sublevel = 2;
						}
						break;
					case SHIFT:
						sublevel = 0;
						break;
					default:
						break;
					}
					if (sublevel >= 0) {// 0为优先级最高
						if (sublevel < level) {
							aliveEdgeList.clear();
							level = sublevel;
						}
						if (sublevel == level) {
							aliveEdgeList.add(npaEdge);// 添加可用边（可用解）
						}
					}
				}
				/* 检查是否有可用边 */
				if (!aliveEdgeList.isEmpty()) {
					/* 如果当前跟踪器没发生过错误，则调整trackerWithoutErrorCount的数值 */
					if (!tracker.m_bRaiseError) {
						trackerWithoutErrorCount += aliveEdgeList.size() - 1;
					}
					/* 如果存在可供转移的边，则进行跳转，必要的时候复制跟踪器（用来回溯） */
					int rev_i = aliveEdgeList.size();
					for (NPAEdge edge : aliveEdgeList) {
						rev_i--;
						Tracker newTracker;
						if (rev_i != 0)// 未遍历到末尾
						{
							/* 复制新的跟踪器 */
							newTracker = trackerResource.addTracker();
							newTracker.m_bRaiseError = tracker.m_bRaiseError;
							newTracker.m_stkStatus.clear();
							newTracker.m_stkStatus.addAll(tracker.m_stkStatus);
							newTracker.m_npaStatus = tracker.m_npaStatus;
							newTracker.m_iterToken = tracker.m_iterToken.copy();
							/* 复制错误记录集 */
							if (!tracker.m_rcdError.m_arrErrors.isEmpty()) {
								newTracker.m_rcdError = trackerResource
										.addErrorRecord(tracker.m_rcdError);
							} else {
								newTracker.m_rcdError = trackerResource
										.addErrorRecord(tracker.m_rcdError.m_prevErrorRecord);
							}
							/* 复制指令记录集 */
							if (!tracker.m_rcdInst.m_arrInsts.isEmpty()) {
								newTracker.m_rcdInst = trackerResource
										.addInstRecord(tracker.m_rcdInst);
							} else {
								newTracker.m_rcdInst = trackerResource
										.addInstRecord(tracker.m_rcdInst.m_prevInstRecord);
							}
						} else {
							newTracker = tracker;
							/* 如果跟踪器需要分化，则在适当的时候改变自身的资源 */
							if (aliveEdgeList.size() > 1) {
								if (!tracker.m_rcdError.m_arrErrors.isEmpty()) {
									newTracker.m_rcdInst = trackerResource
											.addInstRecord(tracker.m_rcdInst);
								}
								if (!tracker.m_rcdInst.m_arrInsts.isEmpty()) {
									newTracker.m_rcdInst = trackerResource
											.addInstRecord(tracker.m_rcdInst);
								}
							}
						}
						switch (edge.m_Data.m_Action) {
						case FINISH:
							break;
						case LEFT_RECURSION:
							break;
						case MOVE:
							newTracker.m_iterToken.ex().saveToken();
							newTracker.m_iterToken.scan();// 匹配，前进一步
							break;
						case REDUCE:
							newTracker.m_stkStatus.pop();// 栈顶弹出
							break;
						case SHIFT:
							newTracker.m_stkStatus.push(newTracker.m_npaStatus);// 移入新状态
							break;
						default:
							break;
						}
						newTracker.m_rcdInst.m_arrInsts.add(new Instruction(
								edge.m_Data.m_Inst, edge.m_Data.m_iIndex,
								edge.m_Data.m_iHandler));// 添加指令和参数
						newTracker.m_npaStatus = edge.m_End;// 通过这条边
					}
				} else {// 无可用边
					/* 检查当前输入时候否到结尾 */
					if (!tracker.m_iterToken.ex().isEOF()) {
						/* 无可用边，且单词流未结束，则报错 */
						if (!tracker.m_bRaiseError) {
							/* 如果是第一次发生错误则调整状态 */
							tracker.m_bRaiseError = true;
							trackerWithoutErrorCount--;
							/* 如果存在没有发生错误的跟踪器，则删除当前跟踪器 */
							if (trackerWithoutErrorCount > 0) {
								tracker.m_iterToken = null;
								trackerResource.freeTracker(tracker);
								tracker = null;
							}
						}
						/* 不存在没有错误的跟踪器，此时判断当前跟踪器是否有继续分析的价值 */
						if (tracker != null) {
							TrackerError error = new TrackerError(
									tracker.m_iterToken.ex().lastPosition());
							/* 寻找合适的错误处理器并处理 */
							boolean processed = findCorrectHandler(tracker,
									error);
							/* 若没有错误处理器接受这个错误，则调用缺省的错误处理器 */
							if (!processed) {
								processed = handleError(tracker, error);
							}
							/* 如果没有处理器接受这个错误则输出缺省的错误消息 */
							if (!processed) {
								error.m_strMessage = String
										.format("位置[%s]，类型[%s]，状态[%s]",
												tracker.m_iterToken.ex()
														.lastPosition(),
												getTokenId(tracker),
												tracker.m_npaStatus.m_Data.m_strLabel);
								tracker.m_iterToken.scan();// 跳过
							}
							/* 提交错误 */
							tracker.m_bInStepError = true;
							tracker.m_rcdError.m_arrErrors.add(error);
						}
					} else {// 单词流到达末尾，同时PDA没有可用边
						tracker.m_bFinished = true;// 跟踪器标记为结束
						/* 如果记号已经读完，则判断是否走到了终结状态 */
						if (finalEdge != null)// 最终边
						{
							tracker.m_rcdInst.m_arrInsts.add(new Instruction(
									finalEdge.m_Data.m_Inst,
									finalEdge.m_Data.m_iIndex,
									finalEdge.m_Data.m_iHandler));
							/* 判断是否分析成功 */
							if (!tracker.m_bRaiseError) {
								/* 记录一个分析结果 */
								ArrayList<Instruction> instList = new ArrayList<Instruction>();
								InstructionRecord instRecord = tracker.m_rcdInst;
								/* 记录语法树指令 */
								while (instRecord != null) {
									instList.addAll(0, instRecord.m_arrInsts);
									instRecord = instRecord.m_prevInstRecord;
								}
								/* 保存结果 */
								m_arrInsts.addAll(instList);
								m_arrTokens.addAll(tracker.m_iterToken.ex()
										.tokenList());
								/* 删除当前跟踪器 */
								tracker.m_iterToken = null;
								trackerResource.freeTracker(tracker);
								tracker = null;
								/* 归约成功 */
								success = true;
							}
						} else {
							/* 无可用边，且单词流未结束，则报错 */
							if (!tracker.m_bRaiseError) {
								/* 如果是第一次发生错误则调整状态 */
								tracker.m_bRaiseError = true;
								trackerWithoutErrorCount--;
								/* 如果存在没有发生错误的跟踪器，则删除当前跟踪器 */
								if (trackerWithoutErrorCount > 0) {
									tracker.m_iterToken = null;
									trackerResource.freeTracker(tracker);
									tracker = null;
								}
							}
							/* 判断该跟踪器是否有继续分析的价值 */
							if (tracker != null) {
								TrackerError error = new TrackerError(
										tracker.m_iterToken.position());
								/* 寻找合适的错误处理器并处理 */
								boolean processed = findCorrectHandler(tracker,
										error);
								/* 如果没有错误处理程序或者错误处理程序放弃处理，则产生缺省的错误消息 */
								if (!processed) {
									error.m_strMessage = String
											.format("位置[%s]，状态 %s",
													tracker.m_iterToken.ex()
															.lastPosition(),
													tracker.m_npaStatus.m_Data.m_strLabel);
									tracker.m_iterToken.scan();// 跳过
								}
								/* 提交错误 */
								tracker.m_bInStepError = true;
								tracker.m_rcdError.m_arrErrors.add(error);
							}
						}
					}
				}
				/* 取出下一个跟踪器 */
				tracker = nextTracker;
			}
			if (trackerWithoutErrorCount == 0)// 没有不存在错误的跟踪器
			{
				/* 根据当前发生错误的情况剔除跟踪器 */

				/* 当前步骤没有错误的跟踪器数量 */
				int trackerWithoutErrorInStepCount = 0;
				/* 当前步骤发生错误的跟踪器数量 */
				int trackerWithErrorInStepCount = 0;
				/* 检查发生错误的跟踪器数量 */
				Tracker tmpTracker = trackerResource.m_headTracker;
				while (tmpTracker != null) {
					if (tmpTracker.m_bInStepError) {
						trackerWithErrorInStepCount++;
					} else {
						trackerWithoutErrorInStepCount++;
					}
					tmpTracker = tmpTracker.m_nextTracker;
				}
				/* 如果同时存在发生错误和不发生错误的跟踪器，则剔除发生错误的跟踪器 */
				if (trackerWithoutErrorInStepCount > 0
						&& trackerWithErrorInStepCount > 0) {
					tmpTracker = trackerResource.m_headTracker;
					while (tmpTracker != null) {
						if (tmpTracker.m_bInStepError) {
							trackerResource.freeTracker(tmpTracker);
						}
						tmpTracker = tmpTracker.m_nextTracker;
					}
				}
				/* 检查是否有到达终点的带有错误的跟踪器 */
				tmpTracker = trackerResource.m_headTracker;
				while (tmpTracker != null) {
					tmpTracker.m_bInStepError = false;
					if (tmpTracker.m_bFinished) {
						/* 提交一份错误报告 */
						ErrorRecord error = tmpTracker.m_rcdError;
						while (error != null) {
							m_arrErrors.addAll(error.m_arrErrors);
							error = error.m_prevErrorRecord;
						}
						/* 终止分析，删除跟踪器 */
						while (trackerResource.m_headTracker != null) {
							trackerResource.m_headTracker.m_iterToken = null;
							trackerResource
									.freeTracker(trackerResource.m_headTracker);
						}
						break;
					}
					tmpTracker = tmpTracker.m_nextTracker;
				}
			}
		}
	}

	/**
	 * 查找合适的错误处理器
	 * 
	 * @param tracker
	 *            跟踪器
	 * @param error
	 *            错误
	 * @return 是否找到
	 */
	private boolean findCorrectHandler(Tracker tracker, TrackerError error) {
		/* 遍历当前状态的所有出边 */
		for (NPAEdge edge : tracker.m_npaStatus.m_OutEdges) {
			IErrorHandler handler = edge.m_Data.m_Handler;
			/* 如果找到了一个错误处理器则进行处理 */
			if (handler != null) {
				TrackerErrorBag bag = new TrackerErrorBag();
				error.m_strMessage = handler.handle(tracker.m_iterToken, bag);
				/* 如果没有放弃则进行处理 */
				if (!bag.m_bGiveUp) {
					if (bag.m_bPass) {// 通过
						if (edge.m_Data.m_ErrorJump != null) {// 有自定义错误处理器
							tracker.m_npaStatus = edge.m_Data.m_ErrorJump;
						} else {
							tracker.m_npaStatus = edge.m_End;// 通过
						}
					}
					if (bag.m_bRead) {// 跳过当前记号
					}
					if (bag.m_bHalt) {// 中止
						tracker.m_bFinished = true;
					}
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 调用缺省的错误处理器处理错误
	 * 
	 * @param tracker
	 *            跟踪器
	 * @param error
	 *            错误
	 * @return 是否成功处理错误
	 */
	private boolean handleError(Tracker tracker, TrackerError error) {
		TrackerErrorBag bag = new TrackerErrorBag();
		error.m_strMessage = m_defErrorHandler.handle(tracker.m_iterToken, bag);
		/* 如果没有放弃则进行处理 */
		if (!bag.m_bGiveUp) {
			if (bag.m_bRead) {// 跳过当前记号
				tracker.m_iterToken.scan();
			}
			if (bag.m_bHalt) {// 中止
				tracker.m_bFinished = true;
			}
			return true;
		}
		return false;
	}

	/**
	 * 得到当前终结符ID
	 * 
	 * @param tracker
	 *            跟踪器
	 * 
	 * @return 终结符ID
	 */
	private int getTokenId(Tracker tracker) {
		Token token = tracker.m_iterToken.ex().token();
		for (TokenExp exp : m_arrTerminals) {
			if (exp.m_kType == token.m_kToken
					&& (exp.m_Object == null || exp.m_Object
							.equals(token.m_Object))) {
				return exp.m_iID;
			}
		}
		return -1;
	}

	/**
	 * 进行语义处理
	 */
	private void run() {
		/* 单词索引 */
		int idxToken = 0;
		/* 规则集合 */
		ArrayList<RuleItem> items = m_NPA.getRuleItems();
		/* 数据处理堆栈 */
		ParsingStack ps = new ParsingStack();
		/* 遍历所有指令 */
		for (Instruction inst : m_arrInsts) {
			/* 语义处理接口 */
			ISemanticHandler handler = null;
			/* 处理前 */
			switch (inst.m_Inst) {
			case PASS:
				idxToken++;
				break;
			case READ:
				ps.set(inst.m_iIndex, m_arrTokens.get(idxToken));
				idxToken++;
				break;
			case SHIFT:
				ps.push();
				break;
			case LEFT_RECURSION:
			case LEFT_RECURSION_DISCARD:
			case TRANSLATE:
			case TRANSLATE_DISCARD:
			case TRANSLATE_FINISH:
				if (inst.m_iHandler != -1) {
					handler = items.get(inst.m_iHandler).m_Handler;
				}
				break;
			default:
				break;
			}
			/* 处理时 */
			if (handler != null) {
				m_Object = handler.handle(ps, null, null);
			}
			/* 处理后 */
			switch (inst.m_Inst) {
			case LEFT_RECURSION:
				ps.pop();// 先pop再push为了让栈层成为current的引用
				ps.push();
				ps.set(inst.m_iIndex, m_Object);
				break;
			case LEFT_RECURSION_DISCARD:
				ps.pop();
				ps.push();
				break;
			case PASS:
				break;
			case READ:
				break;
			case SHIFT:
				break;
			case TRANSLATE:
				ps.pop();
				ps.set(inst.m_iIndex, m_Object);
				break;
			case TRANSLATE_DISCARD:
				ps.pop();
				break;
			case TRANSLATE_FINISH:
				ps.pop();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 缺省的错误处理器
	 * 
	 * @param iterator
	 *            迭代器
	 * @param bag
	 *            参数包
	 * @return 处理信息
	 */
	@Override
	public String handle(IRegexStringIterator token, TrackerErrorBag bag) {
		bag.m_bHalt = true;
		bag.m_bGiveUp = false;
		return "Error";
	}

	/**
	 * 获取单词流描述
	 */
	public String getTokenList() {
		StringBuffer sb = new StringBuffer();
		sb.append("#### 单词流 ####");
		sb.append(System.getProperty("line.separator"));
		for (Token token : m_arrTokens) {
			sb.append(token.toString());
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	/**
	 * 获取分析结果描述
	 */
	public String getObject() {
		StringBuffer sb = new StringBuffer();
		sb.append("#### 分析结果 ####");
		sb.append(System.getProperty("line.separator"));
		if (m_Object != null) {
			sb.append(m_Object.toString());
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	/**
	 * 获得指令集描述
	 */
	public String getInst() {
		ArrayList<RuleItem> items = m_NPA.getRuleItems();
		StringBuffer sb = new StringBuffer();
		sb.append("#### 指令集 ####");
		sb.append(System.getProperty("line.separator"));
		for (Instruction inst : m_arrInsts) {
			sb.append(inst.toString());
			if (inst.m_iHandler != -1) {
				RuleItem item = items.get(inst.m_iHandler);
				sb.append("\t\t"
						+ getSingleString(
								item.m_Parent.m_nonTerminal.m_strName,
								item.m_Expression));
			}
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	/**
	 * 获得错误描述
	 */
	public String getError() {
		StringBuffer sb = new StringBuffer();
		sb.append("#### 错误列表 ####");
		sb.append(System.getProperty("line.separator"));
		for (TrackerError error : m_arrErrors) {
			sb.append(error.toString());
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}
}
