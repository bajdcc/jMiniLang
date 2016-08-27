package priv.bajdcc.LL1.syntax.prediction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

import priv.bajdcc.LL1.grammar.error.GrammarException;
import priv.bajdcc.LL1.grammar.error.GrammarException.GrammarError;
import priv.bajdcc.LL1.syntax.exp.RuleExp;
import priv.bajdcc.LL1.syntax.exp.TokenExp;
import priv.bajdcc.LL1.syntax.rule.Rule;
import priv.bajdcc.LL1.syntax.rule.RuleItem;
import priv.bajdcc.LL1.syntax.solver.SelectSetSolver;
import priv.bajdcc.LL1.syntax.token.PredictType;
import priv.bajdcc.util.lexer.regex.IRegexStringIterator;
import priv.bajdcc.util.lexer.token.Token;

/**
 * 【LL1预测分析】预测分析表
 *
 * @author bajdcc
 */
public class PredictionTable extends SelectSetSolver {

	/**
	 * 非终结符集合
	 */
	protected ArrayList<RuleExp> arrNonTerminals = null;

	/**
	 * 终结符集合
	 */
	protected ArrayList<TokenExp> arrTerminals = null;

	/**
	 * 起始规则
	 */
	private Rule initRule = null;

	/**
	 * 空串规则
	 */
	private TokenExp epsilon = null;

	/**
	 * 预测分析表
	 */
	private int[][] table = null;

	/**
	 * 当前处理的指令包索引
	 */
	private int indexBag = -1;

	/**
	 * 当前处理的非终结符索引
	 */
	private int indexVn = -1;

	/**
	 * 当前处理的指令包
	 */
	private ArrayList<PredictionInstruction> instBag = null;

	/**
	 * 当前处理的产生式规则
	 */
	private RuleItem item = null;

	/**
	 * 字符串迭代器
	 */
	private IRegexStringIterator iter = null;

	/**
	 * 指令包
	 */
	private ArrayList<ArrayList<PredictionInstruction>> instList = new ArrayList<>();

	public PredictionTable(ArrayList<RuleExp> nonTerminals,
			ArrayList<TokenExp> terminals, Rule rule, TokenExp epsilon,
			IRegexStringIterator iter) {
		arrNonTerminals = nonTerminals;
		arrTerminals = terminals;
		initRule = rule;
		this.epsilon = epsilon;
		this.iter = iter;
		initialize();
	}

	/**
	 * 抛出异常
	 * 
	 * @param error
	 *            错误类型
	 * @throws GrammarException
	 */
	private void err(GrammarError error) throws GrammarException {
		throw new GrammarException(error, iter.position(), iter.ex().token());
	}

	/**
	 * 初始化
	 */
	private void initialize() {
		initTable();
		predict();
	}

	/**
	 * 初始化预测分析表
	 */
	private void initTable() {
		int Vn = arrNonTerminals.size();
		int Vt = arrTerminals.size();
		table = new int[Vn][Vt];
		for (int i = 0; i < Vn; i++) {
			for (int j = 0; j < Vt; j++) {
				table[i][j] = -1;// 置无效位
			}
		}
	}

	/**
	 * 构造预测分析表
	 */
	private void predict() {
		for (RuleExp exp : arrNonTerminals) {
			indexVn++;
			for (RuleItem item : exp.rule.arrRules) {
				this.item = item;
				item.expression.visit(this);
			}
		}
	}

	@Override
	protected boolean isEpsilon() {
		return item.epsilon;
	}

	@Override
	protected Collection<TokenExp> getFollow() {
		return arrNonTerminals.get(indexVn).rule.arrFollows;
	}

	@Override
	protected void setCellToRuleId(int token) {
		if (table[indexVn][token] == -1) {
			table[indexVn][token] = indexBag;
		} else if (table[indexVn][token] != indexBag) {
			System.err.println(String.format("存在二义性冲突：位置（%d，%d），[%d]->[%d]",
					indexVn, token, table[indexVn][token], indexBag));
		}
	}

	@Override
	protected void addRule() {
		indexBag++;
		instBag = new ArrayList<>();
		instList.add(instBag);
	}

	@Override
	protected void addInstToRule(PredictType type, int inst) {
		instBag.add(0, new PredictionInstruction(type, inst));
	}

	/**
	 * 得到当前终结符ID
	 * 
	 * @return 终结符ID
	 */
	private int getTokenId() {
		Token token = iter.ex().token();
		for (TokenExp exp : arrTerminals) {
			if (exp.kType == token.kToken
					&& (exp.object == null || exp.object.equals(token.object))) {
				return exp.id;
			}
		}
		return -1;
	}

	/**
	 * 进行分析
	 * 
	 * @throws GrammarException
	 * 
	 */
	public void run() throws GrammarException {
		/* 核心堆栈 */
		Stack<PredictionInstruction> spi = new Stack<>();
		/* 结束符号进栈 */
		spi.push(new PredictionInstruction(PredictType.TERMINAL, epsilon.id));
		/* 起始符号进栈 */
		spi.push(new PredictionInstruction(PredictType.NONTERMINAL,
				initRule.nonTerminal.id));
		/* 设置当前状态 */
		int status = initRule.nonTerminal.id;
		/* 执行步骤顺序 */
		int index = 0;
		while (!spi.isEmpty()) {
			index++;
			System.out.println("步骤[" + index + "]");
			System.out.println("\t----------------");
			/* 获得当前输入字符索引 */
			int idx = getTokenId();
			if (idx == -1) {// 没有找到，非法字符
				err(GrammarError.UNDECLARED);
			}
			System.out.println("\t输入：" + "[" + arrTerminals.get(idx).toString()
					+ "]");
			/* 弹出栈顶符号 */
			PredictionInstruction top = spi.pop();
			System.out.print("\t栈顶：");
			switch (top.type) {
			case NONTERMINAL:
				System.out.println("["
						+ arrNonTerminals.get(top.inst).toString() + "]");
				break;
			case TERMINAL:
				System.out.println("[" + arrTerminals.get(top.inst).toString()
						+ "]");
				break;
			default:
				break;
			}
			switch (top.type) {
			case NONTERMINAL:
				/* 查预测分析表 */
				int inst = table[status][idx];
				if (inst == -1) {
					err(GrammarError.SYNTAX);
				}
				/* 产生式进栈 */
				System.out.print("\t入栈：");
				for (PredictionInstruction pi : instList.get(inst)) {
					spi.push(pi);
					switch (pi.type) {
					case NONTERMINAL:
						System.out
								.print("["
										+ arrNonTerminals.get(pi.inst)
												.toString() + "]");
						break;
					case TERMINAL:
						System.out.print("["
								+ arrTerminals.get(pi.inst).toString() + "]");
						break;
					default:
						break;
					}
				}
				System.out.println();
				break;
			case TERMINAL:
				if (idx == top.inst) {// 终结符匹配，继续
					iter.ex().saveToken();// 保存单词
					iter.scan();
				} else {
					err(GrammarError.SYNTAX);
				}
				System.out.println("\t匹配：" + arrTerminals.get(idx).toString());
				break;
			default:
				break;
			}
			System.out.println("\t----------------");
			for (int i = spi.size() - 1; i >= 0; i--) {
				PredictionInstruction pi = spi.get(i);
				switch (pi.type) {
				case NONTERMINAL:
					System.out.println("\t" + i + ": ["
							+ arrNonTerminals.get(pi.inst).toString() + "]");
					break;
				case TERMINAL:
					System.out.println("\t" + i + ": ["
							+ arrTerminals.get(pi.inst).toString() + "]");
					break;
				default:
					break;
				}
			}
			System.out.println();
			/* 更新当前状态 */
			status = -1;
			for (int i = spi.size() - 1; i >= 0; i--) {
				if (spi.get(i).type == PredictType.NONTERMINAL) {
					status = spi.get(i).inst;
					break;
				}
			}
		}
		System.out.println();
	}

	/**
	 * 获得指令描述
	 * 
	 */
	public String getInstString() {
		StringBuilder sb = new StringBuilder();
		sb.append("#### 指令包 ####");
		sb.append(System.lineSeparator());
		for (int i = 0; i < instList.size(); i++) {
			sb.append(i).append(": ");
			ArrayList<PredictionInstruction> pis = instList.get(i);
			for (PredictionInstruction k : pis) {
				switch (k.type) {
				case NONTERMINAL:
					sb.append("[").append(arrNonTerminals.get(k.inst).toString()).append("]");
					break;
				case TERMINAL:
					sb.append("[").append(arrTerminals.get(k.inst).toString()).append("]");
					break;
				default:
					break;
				}
			}
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	/**
	 * 获得矩阵描述
	 * 
	 */
	public String getMatrixString() {
		StringBuilder sb = new StringBuilder();
		int Vn = arrNonTerminals.size();
		int Vt = arrTerminals.size();
		sb.append("#### 预测分析矩阵 ####");
		sb.append(System.lineSeparator());
		for (int i = 0; i < Vn; i++) {
			for (int j = 0; j < Vt; j++) {
				if (table[i][j] != -1) {
					sb.append(table[i][j]);
				} else {
					sb.append("-");
				}
				sb.append("\t");
			}
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	/**
	 * 获得详细描述
	 */
	public String getTableString() {
		StringBuilder sb = new StringBuilder();
		int Vn = arrNonTerminals.size();
		int Vt = arrTerminals.size();
		sb.append("#### 预测分析表 ####");
		sb.append(System.lineSeparator());
		for (int i = 0; i < Vn; i++) {
			sb.append("状态[").append(i).append("]： ");
			sb.append(System.lineSeparator());
			sb.append("\t非终结符 -> ").append(arrNonTerminals.get(i).name);
			sb.append(System.lineSeparator());
			for (int j = 0; j < Vt; j++) {
				int idx = table[i][j];
				if (idx != -1) {
					sb.append("\t\t----------------");
					sb.append(System.lineSeparator());
					sb.append("\t\t接受 -> ").append(arrTerminals.get(j).toString());
					sb.append(System.lineSeparator());
					sb.append("\t\t入栈 -> ");
					for (PredictionInstruction k : instList.get(table[i][j])) {
						switch (k.type) {
						case NONTERMINAL:
							sb.append("[").append(arrNonTerminals.get(k.inst).toString()).append("]");
							break;
						case TERMINAL:
							sb.append("[").append(arrTerminals.get(k.inst).toString()).append("]");
							break;
						default:
							break;
						}
					}
					sb.append(System.lineSeparator());
				}
			}
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return getTableString() +
				getInstString() +
				getMatrixString();
	}
}
