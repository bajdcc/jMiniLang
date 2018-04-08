package priv.bajdcc.LALR1.semantic;

import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.symbol.IManageSymbol;
import priv.bajdcc.LALR1.grammar.symbol.IQuerySymbol;
import priv.bajdcc.LALR1.grammar.tree.Function;
import priv.bajdcc.LALR1.semantic.lexer.TokenFactory;
import priv.bajdcc.LALR1.semantic.token.ISemanticAnalyzer;
import priv.bajdcc.LALR1.semantic.tracker.*;
import priv.bajdcc.LALR1.syntax.Syntax;
import priv.bajdcc.LALR1.syntax.automata.npa.NPAEdge;
import priv.bajdcc.LALR1.syntax.automata.npa.NPAStatus;
import priv.bajdcc.LALR1.syntax.exp.TokenExp;
import priv.bajdcc.LALR1.syntax.handler.IErrorHandler;
import priv.bajdcc.LALR1.syntax.handler.SyntaxException;
import priv.bajdcc.LALR1.syntax.handler.SyntaxException.SyntaxError;
import priv.bajdcc.LALR1.syntax.rule.RuleItem;
import priv.bajdcc.util.Position;
import priv.bajdcc.util.TrackerErrorBag;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.regex.IRegexStringIterator;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * 【语义分析】语义分析
 *
 * @author bajdcc
 */
public class Semantic extends Syntax implements IErrorHandler {

	/**
	 * 单词流工厂
	 */
	protected TokenFactory tokenFactory = null;

	/**
	 * 错误处理器
	 */
	private IErrorHandler errorHandler = this;

	/**
	 * 存放生成的指令
	 */
	private ArrayList<Instruction> arrInsts = new ArrayList<>();

	/**
	 * 存放生成过程中的错误
	 */
	private ArrayList<TrackerError> arrErrors = new ArrayList<>();

	/**
	 * 单词流
	 */
	private ArrayList<Token> arrTokens = new ArrayList<>();

	/**
	 * 当前的语义接口
	 */
	private ISemanticAnalyzer semanticHandler = null;

	/**
	 * 语义分析结果
	 */
	private Object object = null;

	/**
	 * 跟踪器资源
	 */
	private TrackerResource trackerResource = new TrackerResource();

	/**
	 * 没有错误的跟踪器数量
	 */
	private int iTrackerWithoutErrorCount = 0;

	/**
	 * 当前跟踪器
	 */
	private Tracker tracker = null;

	/**
	 * 是否打印调试信息
	 */
	private boolean debug = false;

	/**
	 * 设置错误处理器
	 * 
	 * @param handler
	 *            错误处理器接口
	 */
	public void setErrorHandler(IErrorHandler handler) {
		errorHandler = handler;
	}

	public Semantic(String context) throws RegexException {
		super(true);
		tokenFactory = new TokenFactory(context);// 用于分析的文本
		tokenFactory.discard(TokenType.COMMENT);
		tokenFactory.discard(TokenType.WHITESPACE);
		tokenFactory.discard(TokenType.ERROR);
		tokenFactory.discard(TokenType.MACRO);
		tokenFactory.scan();
	}

	/**
	 * @param handler
	 *            语义分析接口
	 * @param inferString
	 *            文法推导式
	 * @throws SyntaxException 词法错误
	 */
	public void infer(ISemanticAnalyzer handler, String inferString)
			throws SyntaxException {
		if (handler == null) {
			throw new NullPointerException("handler");
		}
		semanticHandler = handler;
		super.infer(inferString);
		semanticHandler = null;
	}

	@Override
	protected void onAddRuleItem(RuleItem item) {
		item.handler = semanticHandler;
	}

	/**
	 * 开始解析
	 * @throws SyntaxException 语法错误
	 */
	public void parse() throws SyntaxException {
		analysis();
		run();
	}

	/**
	 * 开始分析工作
	 */
	private void analysis() {
		/* 可用NPA边表 */
		ArrayList<NPAEdge> aliveEdgeList = new ArrayList<>();
		/* 结束边 */
		NPAEdge finalEdge;
		/* 初始PDA状态集合 */
		ArrayList<NPAStatus> initStatusList = npa.getInitStatusList();
		/* 清空结果 */
		arrInsts.clear();
		arrErrors.clear();
		/* 初始化跟踪器 */
		for (NPAStatus npaStatus : initStatusList) {
			Tracker tracker = trackerResource.addTracker();
			tracker.rcdInst = trackerResource.addInstRecord(null);
			tracker.rcdError = trackerResource.addErrorRecord(null);
			tracker.npaStatus = npaStatus;
			tracker.iter = tokenFactory;
			iTrackerWithoutErrorCount++;
		}
		/* 是否分析成功 */
		boolean success = false;
		/* 进行分析 */
		while (trackerResource.head != null && !success) {
			tracker = trackerResource.head;
			while (tracker != null && !success) {
				Tracker nextTracker = tracker.next;
				if (debug) {
					System.err.println(getTrackerStatus());
				}
				/* 对每一个跟踪器进行计算，构造level记录可用边优先级，可以防止冲突 */
				/* 匹配=0 移进=0 左递归=1 归约=2 */
				/* 对终结符则移进，对非终结符则匹配，互不冲突，故两者优先级相同 */
				/* 左递归属于特殊的归约，区别是归约后不出栈 */
				/* 注意：已除去二义性文法，跟踪器是不带回溯的 */
				int level = 2;
				/* 筛选边 */
				aliveEdgeList.clear();
				finalEdge = null;
				/* 遍历出边 */
				for (NPAEdge npaEdge : tracker.npaStatus.outEdges) {
					int sublevel = -1;
					/* 若当前边使用了LA表，则输入不满足LA表时会被丢弃 */
					if (npaEdge.data.arrLookAhead != null) {
						if (!npaEdge.data.arrLookAhead
								.contains(getTokenId(tracker))) {
							continue;
						}
					}
					switch (npaEdge.data.kAction) {
					case FINISH:
						/* 检查状态堆栈是否为空 */
						if (tracker.stkStatus.isEmpty()) {
							finalEdge = npaEdge;
						}
						continue;
					case LEFT_RECURSION:
						sublevel = 1;
						break;
					case MOVE:
						/* 检查Move所需要的记号跟输入是否一致（匹配） */
						if (npaEdge.data.iToken != getTokenId(tracker)) {
							continue;// 失败
						} else {
							sublevel = 0;
						}
						break;
					case REDUCE:
						/* 检查Reduce所需要的栈状态跟状态堆栈的栈顶元素是否一致 */
						if (tracker.stkStatus.isEmpty()
								|| npaEdge.data.status != tracker.stkStatus
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
					// 0为优先级最高
					if (sublevel < level) {
                        aliveEdgeList.clear();
                        level = sublevel;
                    }
					if (sublevel == level) {
                        aliveEdgeList.add(npaEdge);// 添加可用边（可用解）
                    }
				}
				/* 检查是否有可用边 */
				if (!aliveEdgeList.isEmpty()) {
					/* 如果当前跟踪器没发生过错误，则调整trackerWithoutErrorCount的数值 */
					if (!tracker.bRaiseError) {
						iTrackerWithoutErrorCount += aliveEdgeList.size() - 1;
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
							newTracker.bRaiseError = tracker.bRaiseError;
							newTracker.stkStatus.clear();
							newTracker.stkStatus.addAll(tracker.stkStatus);
							newTracker.npaStatus = tracker.npaStatus;
							newTracker.iter = tracker.iter.copy();
							/* 复制错误记录集 */
							if (!tracker.rcdError.arrErrors.isEmpty()) {
								newTracker.rcdError = trackerResource
										.addErrorRecord(tracker.rcdError);
							} else {
								newTracker.rcdError = trackerResource
										.addErrorRecord(tracker.rcdError.prev);
							}
							/* 复制指令记录集 */
							if (!tracker.rcdInst.arrInsts.isEmpty()) {
								newTracker.rcdInst = trackerResource
										.addInstRecord(tracker.rcdInst);
							} else {
								newTracker.rcdInst = trackerResource
										.addInstRecord(tracker.rcdInst.prev);
							}
						} else {
							newTracker = tracker;
							/* 如果跟踪器需要分化，则在适当的时候改变自身的资源 */
							if (aliveEdgeList.size() > 1) {
								if (!tracker.rcdError.arrErrors.isEmpty()) {
									newTracker.rcdInst = trackerResource
											.addInstRecord(tracker.rcdInst);
								}
								if (!tracker.rcdInst.arrInsts.isEmpty()) {
									newTracker.rcdInst = trackerResource
											.addInstRecord(tracker.rcdInst);
								}
							}
						}
						switch (edge.data.kAction) {
						case FINISH:
							break;
						case LEFT_RECURSION:
							break;
						case MOVE:
							newTracker.iter.ex().saveToken();
							newTracker.iter.scan();// 匹配，前进一步
							break;
						case REDUCE:
							newTracker.stkStatus.pop();// 栈顶弹出
							break;
						case SHIFT:
							newTracker.stkStatus.push(newTracker.npaStatus);// 移入新状态
							break;
						default:
							break;
						}
						newTracker.rcdInst.arrInsts.add(new Instruction(
								edge.data.inst, edge.data.iIndex,
								edge.data.iHandler));// 添加指令和参数
						newTracker.npaStatus = edge.end;// 通过这条边
					}
				} else {// 无可用边
					/* 检查当前输入时候否到结尾 */
					if (!tracker.iter.ex().isEOF()) {
						/* 无可用边，且单词流未结束，则报错 */
						if (!tracker.bRaiseError) {
							/* 如果是第一次发生错误则调整状态 */
							tracker.bRaiseError = true;
							iTrackerWithoutErrorCount--;
							/* 如果存在没有发生错误的跟踪器，则删除当前跟踪器 */
							if (iTrackerWithoutErrorCount > 0) {
								tracker.iter = null;
								trackerResource.freeTracker(tracker);
								tracker = null;
							}
						}
						/* 不存在没有错误的跟踪器，此时判断当前跟踪器是否有继续分析的价值 */
						if (tracker != null) {
							validateTracker(false);
						}
					} else {// 单词流到达末尾，同时PDA没有可用边
						tracker.bFinished = true;// 跟踪器标记为结束
						/* 如果记号已经读完，则判断是否走到了终结状态 */
						if (finalEdge != null)// 最终边
						{
							tracker.rcdInst.arrInsts.add(new Instruction(
									finalEdge.data.inst, finalEdge.data.iIndex,
									finalEdge.data.iHandler));
							/* 判断是否分析成功 */
							if (!tracker.bRaiseError) {
								/* 记录一个分析结果 */
								ArrayList<Instruction> instList = new ArrayList<>();
								InstructionRecord instRecord = tracker.rcdInst;
								/* 记录语法树指令 */
								while (instRecord != null) {
									instList.addAll(0, instRecord.arrInsts);
									instRecord = instRecord.prev;
								}
								/* 保存结果 */
								arrInsts.addAll(instList);
								arrTokens.addAll(tracker.iter.ex().tokenList());
								/* 删除当前跟踪器 */
								tracker.iter = null;
								trackerResource.freeTracker(tracker);
								tracker = null;
								/* 归约成功 */
								success = true;
							}
						} else {
							validateTracker(true);
						}
					}
				}
				/* 取出下一个跟踪器 */
				tracker = nextTracker;
			}
			if (iTrackerWithoutErrorCount == 0)// 没有不存在错误的跟踪器
			{
				/* 根据当前发生错误的情况剔除跟踪器 */

				/* 当前步骤没有错误的跟踪器数量 */
				int trackerWithoutErrorInStepCount = 0;
				/* 当前步骤发生错误的跟踪器数量 */
				int trackerWithErrorInStepCount = 0;
				/* 检查发生错误的跟踪器数量 */
				Tracker tmpTracker = trackerResource.head;
				while (tmpTracker != null) {
					if (tmpTracker.bInStepError) {
						trackerWithErrorInStepCount++;
					} else {
						trackerWithoutErrorInStepCount++;
					}
					tmpTracker = tmpTracker.next;
				}
				/* 如果同时存在发生错误和不发生错误的跟踪器，则剔除发生错误的跟踪器 */
				if (trackerWithoutErrorInStepCount > 0
						&& trackerWithErrorInStepCount > 0) {
					tmpTracker = trackerResource.head;
					while (tmpTracker != null) {
						if (tmpTracker.bInStepError) {
							trackerResource.freeTracker(tmpTracker);
						}
						tmpTracker = tmpTracker.next;
					}
				}
				/* 检查是否有到达终点的带有错误的跟踪器 */
				tmpTracker = trackerResource.head;
				while (tmpTracker != null) {
					tmpTracker.bInStepError = false;
					if (tmpTracker.bFinished) {
						/* 提交一份错误报告 */
						ErrorRecord error = tmpTracker.rcdError;
						while (error != null) {
							arrErrors.addAll(error.arrErrors);
							error = error.prev;
						}
						/* 终止分析，删除跟踪器 */
						while (trackerResource.head != null) {
							trackerResource.head.iter = null;
							trackerResource.freeTracker(trackerResource.head);
						}
						break;
					}
					tmpTracker = tmpTracker.next;
				}
			}
		}
	}

	/**
	 * 分析跟踪器是否有继续分析的价值
	 * 
	 * @param fatal
	 *            跟踪器是否遇到严重且不可恢复的错误
	 */
	private void validateTracker(boolean fatal) {
		/* 无可用边，且单词流未结束，则报错 */
		if (!tracker.bRaiseError) {
			/* 如果是第一次发生错误则调整状态 */
			tracker.bRaiseError = true;
			iTrackerWithoutErrorCount--;
			/* 如果存在没有发生错误的跟踪器，则删除当前跟踪器 */
			if (iTrackerWithoutErrorCount > 0) {
				tracker.iter = null;
				trackerResource.freeTracker(tracker);
				tracker = null;
			}
		}
		/* 判断该跟踪器是否有继续分析的价值 */
		if (tracker != null) {
			Position position = new Position(fatal ? tracker.iter.position() : tracker.iter
					.ex().lastPosition());
			TrackerError error = new TrackerError(position);
			/* 寻找合适的错误处理器并处理 */
			boolean processed = findCorrectHandler(tracker, error, position);
			/* 若没有错误处理器接受这个错误，则调用缺省的错误处理器 */
			if (!processed && !fatal) {
				processed = handleError(tracker, error, position);
			}
			/* 如果没有错误处理程序或者错误处理程序放弃处理，则产生缺省的错误消息 */
			if (!processed) {
				if (!fatal) {
					error.message = String.format("类型[%s]，状态[%s]", tracker.iter
							.ex().token(), tracker.npaStatus.data.label);
				} else {
					error.message = String.format("状态[%s]",
							tracker.npaStatus.data.label);
				}
				tracker.iter.scan();// 跳过
			}
			/* 提交错误 */
			tracker.bInStepError = true;
			tracker.rcdError.arrErrors.add(error);
		}
	}

	/**
	 * 查找合适的错误处理器
	 * 
	 * @param tracker
	 *            跟踪器
	 * @param error
	 *            错误
	 * @param position
	 *            错误位置
	 * @return 是否找到
	 */
	private boolean findCorrectHandler(Tracker tracker, TrackerError error, Position position) {
		/* 遍历当前状态的所有出边 */
		for (NPAEdge edge : tracker.npaStatus.outEdges) {
			IErrorHandler handler = edge.data.handler;
			/* 如果找到了一个错误处理器则进行处理 */
			if (handler != null) {
				TrackerErrorBag bag = new TrackerErrorBag(position);
				error.message = handler.handle(tracker.iter, bag);
				/* 如果没有放弃则进行处理 */
				if (!bag.bGiveUp) {
					if (bag.bPass) {// 通过
						if (edge.data.errorJump != null) {// 有自定义错误处理器
							tracker.npaStatus = edge.data.errorJump;
						} else {
							tracker.npaStatus = edge.end;// 通过
						}
					}
					if (bag.bRead) {// 跳过当前记号
						tracker.iter.scan();
					}
					if (bag.bHalt) {// 中止
						tracker.bFinished = true;
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
	 * @param position
	 *            错误位置
	 * @return 是否成功处理错误
	 */
	private boolean handleError(Tracker tracker, TrackerError error, Position position) {
		TrackerErrorBag bag = new TrackerErrorBag(position);
		error.message = errorHandler.handle(tracker.iter, bag);
		/* 如果没有放弃则进行处理 */
		if (!bag.bGiveUp) {
			if (bag.bRead) {// 跳过当前记号
				tracker.iter.scan();
			}
			if (bag.bHalt) {// 中止
				tracker.bFinished = true;
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
		Token token = tracker.iter.ex().token();
		for (TokenExp exp : arrTerminals) {
			if (exp.kType == token.kToken
					&& (exp.object == null || exp.object.equals(token.object))) {
				return exp.id;
			}
		}
		return -1;
	}

	/**
	 * 进行语义处理
	 */
	private void run() throws SyntaxException {
		if (!arrErrors.isEmpty()) {
			System.err.println(getTrackerError());
			throw new SyntaxException(SyntaxError.COMPILE_ERROR,
					arrErrors.get(0).position, "出现语法错误");
		}
		/* 规则集合 */
		ArrayList<RuleItem> items = npa.getRuleItems();
		/* 符号表查询接口 */
		IQuerySymbol query = getQuerySymbolService();
		/* 符号表管理接口 */
		IManageSymbol manage = getManageSymbolService();
		/* 语义错误处理接口 */
		ISemanticRecorder recorder = getSemanticRecorderService();
		/* 复制单词流 */
		ArrayList<Token> tokens = arrTokens.stream().map(Token::copy).collect(Collectors.toCollection(ArrayList::new));
		/* 运行时自动机 */
		SemanticMachine machine = new SemanticMachine(items, arrActions,
				tokens, query, manage, recorder, debug);
		/* 遍历所有指令 */
		arrInsts.forEach(machine::run);
		object = machine.getObject();
		if (object != null) {
			Function entry = (Function) object;
			manage.getManageScopeService().registerFunc(entry);
		}
	}

	/**
	 * 获取符号表查询接口
	 * 
	 * @return 符号表查询接口
	 */
	protected IQuerySymbol getQuerySymbolService() {
		return null;
	}

	/**
	 * 获取符号表管理接口
	 * 
	 * @return 符号表管理接口
	 */
	protected IManageSymbol getManageSymbolService() {
		return null;
	}

	/**
	 * 获取语义错误处理接口
	 * 
	 * @return 语义错误处理接口
	 */
	protected ISemanticRecorder getSemanticRecorderService() {
		return null;
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
	public String handle(IRegexStringIterator iterator, TrackerErrorBag bag) {
		bag.bHalt = true;
		bag.bGiveUp = false;
		return "Error";
	}

	/**
	 * 获取跟踪器信息
	 */
	private String getTrackerStatus() {
		/* 用于调试 */
		System.err.println("#### #### ####");
		System.err.println(tracker.iter.index());
		System.err.println(tracker.iter.position());
		System.err.println(tracker.npaStatus.data.label);
		ArrayList<RuleItem> items = npa.getRuleItems();
		StringBuilder sb = new StringBuilder();
		sb.append(System.lineSeparator());
		InstructionRecord rcd = tracker.rcdInst;
		while (rcd != null) {
			for (Instruction inst : rcd.arrInsts) {
				sb.append(inst.toString());
				if (inst.iHandler != -1) {
					RuleItem item = items.get(inst.iHandler);
					sb.append("\t").append(getSingleString(item.parent.nonTerminal.name,
							item.expression));
				}
				sb.append(System.lineSeparator());
			}
			rcd = rcd.prev;
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	/**
	 * 获取单词流描述
	 * @return 单词流描述
	 */
	public String getTokenList() {
		StringBuilder sb = new StringBuilder();
		sb.append("#### 单词流 ####");
		sb.append(System.lineSeparator());
		for (Token token : arrTokens) {
			sb.append(token.toString());
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	/**
	 * 获取分析结果描述
	 * @return 分析结果描述
	 */
	public String getObject() {
		StringBuilder sb = new StringBuilder();
		sb.append("#### 分析结果 ####");
		sb.append(System.lineSeparator());
		if (object != null) {
			sb.append(object.toString());
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	/**
	 * 获得指令集描述
	 * @return 指令集描述
	 */
	public String getInst() {
		ArrayList<RuleItem> items = npa.getRuleItems();
		StringBuilder sb = new StringBuilder();
		sb.append("#### 指令集 ####");
		sb.append(System.lineSeparator());
		for (Instruction inst : arrInsts) {
			sb.append(inst.toString());
			if (inst.iHandler != -1) {
				RuleItem item = items.get(inst.iHandler);
				sb.append("\t\t").append(getSingleString(item.parent.nonTerminal.name,
						item.expression));
			}
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	/**
	 * 获得语法错误描述
	 * @return 语法错误描述
	 */
	public String getTrackerError() {
		StringBuilder sb = new StringBuilder();
		sb.append("#### 语法错误列表 ####");
		sb.append(System.lineSeparator());
		for (TrackerError error : arrErrors) {
			sb.append(error.toString());
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
}
