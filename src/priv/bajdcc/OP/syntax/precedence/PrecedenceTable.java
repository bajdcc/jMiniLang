package priv.bajdcc.OP.syntax.precedence;

import priv.bajdcc.LL1.syntax.prediction.PredictionInstruction;
import priv.bajdcc.LL1.syntax.token.PredictType;
import priv.bajdcc.OP.grammar.error.GrammarException;
import priv.bajdcc.OP.grammar.error.GrammarException.GrammarError;
import priv.bajdcc.OP.grammar.handler.IPatternHandler;
import priv.bajdcc.OP.syntax.exp.RuleExp;
import priv.bajdcc.OP.syntax.exp.TokenExp;
import priv.bajdcc.OP.syntax.handler.SyntaxException;
import priv.bajdcc.OP.syntax.handler.SyntaxException.SyntaxError;
import priv.bajdcc.OP.syntax.rule.RuleItem;
import priv.bajdcc.OP.syntax.solver.FirstVTSolver;
import priv.bajdcc.OP.syntax.solver.LastVTSolver;
import priv.bajdcc.OP.syntax.solver.OPTableSolver;
import priv.bajdcc.OP.syntax.token.PrecedenceType;
import priv.bajdcc.util.lexer.regex.IRegexStringIterator;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

import java.util.*;

/**
 * 【算法优先分析】算法优先关系表
 *
 * @author bajdcc
 */
public class PrecedenceTable extends OPTableSolver {

	/**
	 * 非终结符集合
	 */
	protected ArrayList<RuleExp> arrNonTerminals = null;

	/**
	 * 终结符集合
	 */
	protected ArrayList<TokenExp> arrTerminals = null;

	/**
	 * 非终结符映射
	 */
	protected HashMap<RuleExp, Integer> mapNonTerminals = new HashMap<>();

	/**
	 * 终结符映射
	 */
	protected HashMap<TokenExp, Integer> mapTerminals = new HashMap<>();

	/**
	 * 算符优先分析表
	 */
	private PrecedenceType[][] table = null;

	/**
	 * 归约模式映射
	 */
	private HashMap<String, IPatternHandler> mapPattern = null;

	/**
	 * 字符串迭代器
	 */
	private IRegexStringIterator iter = null;

	private static boolean debug = false;

	public PrecedenceTable(ArrayList<RuleExp> nonTerminals,
			ArrayList<TokenExp> terminals,
			HashMap<String, IPatternHandler> pattern, IRegexStringIterator iter) {
		arrNonTerminals = nonTerminals;
		arrTerminals = terminals;
		mapPattern = pattern;
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
	 * 抛出异常
	 * 
	 * @param error
	 *            错误类型
	 * @throws SyntaxException
	 */
	private void err(SyntaxError error) throws SyntaxException {
		throw new SyntaxException(error, iter.position(), iter.ex().token());
	}

	/**
	 * 添加归约模式
	 * 
	 * @param pattern
	 *            模式串（由0和1组成，0=Vn，1=Vt）
	 * @param handler
	 *            处理器
	 * @throws SyntaxException 词法错误
	 */
	public void addPatternHandler(String pattern, IPatternHandler handler)
			throws SyntaxException {
		if (mapPattern.put(pattern, handler) != null) {
			err(SyntaxError.REDECLARATION);
		}
	}

	/**
	 * 初始化
	 */
	private void initialize() {
		initMap();
		initTable();
		buildFirstVTAndLastVt();
		buildTable();
	}

	/**
	 * 初始化符号映射表
	 */
	private void initMap() {
		for (int i = 0; i < arrNonTerminals.size(); i++) {
			mapNonTerminals.put(arrNonTerminals.get(i), i);
		}
		for (int i = 0; i < arrTerminals.size(); i++) {
			mapTerminals.put(arrTerminals.get(i), i);
		}
	}

	/**
	 * 初始化算符优先关系表
	 */
	private void initTable() {
		int size = arrTerminals.size();
		table = new PrecedenceType[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				table[i][j] = PrecedenceType.NULL;// 置无效位
			}
		}
	}

	/**
	 * 构造FirstVT和LastVT
	 */
	private void buildFirstVTAndLastVt() {
		/* 迭代求值 */
		boolean update;
		do {
			update = false;
			for (RuleExp exp : arrNonTerminals) {
				for (RuleItem item : exp.rule.arrRules) {
					FirstVTSolver firstVT = new FirstVTSolver(exp);
					LastVTSolver lastVT = new LastVTSolver(exp);
					item.expression.visit(firstVT);
					item.expression.visitReverse(lastVT);
					update |= firstVT.isUpdated();
					update |= lastVT.isUpdated();
				}
			}
		} while (update);
		/* 将哈希表按ID排序并保存 */
		for (RuleExp exp : arrNonTerminals) {
			exp.rule.arrFirstVT = sortTerminal(exp.rule.setFirstVT);
			exp.rule.arrLastVT = sortTerminal(exp.rule.setLastVT);
		}
	}

	/**
	 * 建立算符优先表
	 */
	private void buildTable() {
		for (RuleExp exp : arrNonTerminals) {
			for (RuleItem item : exp.rule.arrRules) {
				item.expression.visit(this);
			}
		}
	}

	@Override
	protected void setCell(int x, int y, PrecedenceType type) {
		if (table[x][y] == PrecedenceType.NULL) {
			table[x][y] = type;
		} else if (table[x][y] != type) {
			System.err.println(String.format("项目冲突：[%s] -> [%s]，'%s' -> '%s'",
					arrTerminals.get(x), arrTerminals.get(y),
					table[x][y].getName(), type.getName()));
		}
	}

	/**
	 * 给指定终结符集合按ID排序
	 *
	 * @param colletion 要排序的集合
	 * @return 排序后的结果
     */
	private ArrayList<TokenExp> sortTerminal(Collection<TokenExp> colletion) {
		ArrayList<TokenExp> list = new ArrayList<>(colletion);
		Collections.sort(list, (o1, o2) -> o1.id > o2.id ? 1 : (o1.id == o2.id ? 0 : -1));
		return list;
	}

	/**
	 * 得到当前终结符ID
	 * 
	 * @return 终结符ID
	 */
	private int getTokenId() {
		Token token = iter.ex().token();
		if (token.kToken == TokenType.EOF) {
			return -2;
		}
		for (TokenExp exp : arrTerminals) {
			if (exp.kType == token.kToken
					&& (exp.object == null || exp.object.equals(token.object))) {
				return exp.id;
			}
		}
		return -1;
	}

	private void println() {
		if (debug)
			System.out.println();
	}

	private void println(String str) {
		if (debug)
			System.out.println(str);
	}

	/**
	 * 进行分析
	 *
	 * @return 计算后的值
	 * @throws GrammarException 语法错误
	 * 
	 */
	public Object run() throws GrammarException {
		/* 指令堆栈 */
		Stack<PredictionInstruction> spi = new Stack<>();
		/* 数据堆栈 */
		Stack<FixedData> sobj = new Stack<>();
		/* 结束符号进栈 */
		spi.push(new PredictionInstruction(PredictType.EPSILON, -1));
		sobj.push(new FixedData());
		/* 执行步骤顺序 */
		int index = 0;
		/* 输入字符索引 */
		int input = getTokenId();// #为-2
		/* 栈顶的终结符索引 */
		int top = -1;
		/* 栈顶的终结符位置 */
		int topIndex = -1;
		while (!(spi.size() == 2 && input == -2)) {// 栈层为2且输入为#，退出
			index++;
			println("步骤[" + index + "]");
			println("\t----------------");
			if (input == -1) {// 没有找到，非法字符
				err(GrammarError.UNDECLARED);
			}
			if (input == -2 && spi.size() == 1) {// 栈为#，输入为#，报错
				err(GrammarError.NULL);
			}
			Token token = iter.ex().token();
			println("\t输入：" + "[" + token + "]");
			if (top != -1 && input != -2
					&& table[top][input] == PrecedenceType.NULL) {
				err(GrammarError.MISS_PRECEDENCE);
			}
			if (top == -1
					|| (input != -2 && table[top][input] != PrecedenceType.GT)) {
				/* 栈顶为#，或者top<=input，则直接移进 */
				println("\t移进：[" + token + "]");
				/* 1.指令进栈 */
				spi.push(new PredictionInstruction(PredictType.TERMINAL, input));
				/* 2.数据进栈 */
				sobj.push(new FixedData(token));
				/* 3.保存单词 */
				iter.ex().saveToken();
				/* 4.取下一个单词 */
				iter.scan();
				/* 5.刷新当前输入字符索引 */
				input = getTokenId();
			} else {
				/* 不是移进就是归约 */
				/* 1.从栈顶向下寻找第一个出现LT的终结符 */
				int head;
				int comp_top = top;
				int comp_top_index = topIndex;
				for (head = topIndex - 1; head >= 0; head--) {
					if (spi.get(head).type == PredictType.EPSILON) {
						// 找到底部#
						comp_top_index = head + 1;
						break;
					}
					if (spi.get(head).type == PredictType.TERMINAL) {
						if (table[spi.get(head).inst][comp_top] == PrecedenceType.LT) {
							// 找到第一个优先级LT的
							comp_top_index = head + 1;
							break;
						} else if (table[spi.get(head).inst][comp_top] == PrecedenceType.EQ) {
							// 素短语内部优先级相同
							comp_top = spi.get(head).inst;
							comp_top_index = head;
						}
					}
				}
				// head原来为最左素短语的头，从head+1到栈顶为可归约子串
				int primePhraseCount = spi.size() - comp_top_index;
				/* 2.保存最左素短语 */
				ArrayList<PredictionInstruction> primeInstList = new ArrayList<>();
				ArrayList<FixedData> primeDataList = new ArrayList<>();
				for (int i = 0; i < primePhraseCount; i++) {
					primeInstList.add(0, spi.pop());
					primeDataList.add(0, sobj.pop());
				}
				println("\t----==== 最左素短语模式 ====----");
				String pattern = getPattern(primeInstList);
				println("\t" + pattern + ": "
						+ pattern.replace("0", "[op]").replace("1", "[tok]"));
				println("\t----==== 最左素短语 ====----");
				for (int i = 0; i < primePhraseCount; i++) {
					println("\t" + primeDataList.get(i));
				}
				/* 3.新建指令集和数据集（用于用户级回调） */
				ArrayList<Token> tempTokenList = new ArrayList<>();
				ArrayList<Object> tempObjectList = new ArrayList<>();
				for (int i = 0; i < primePhraseCount; i++) {
					PredictType pt = primeInstList.get(i).type;
					if (pt == PredictType.TERMINAL) {
						tempTokenList.add(primeDataList.get(i).token);
					} else if (pt == PredictType.NONTERMINAL) {
						tempObjectList.add(primeDataList.get(i).obj);
					}
				}
				/* 4.寻找定义过的有效的模式，进行归约 */
				IPatternHandler handler = mapPattern.get(pattern);
				if (handler == null) {
					System.err.println("缺少处理模式：" + pattern + ": "
							+ pattern.replace("0", "[op]").replace("1", "[tok]"));
					err(GrammarError.MISS_HANDLER);
				}
				println("\t----==== 处理模式名称 ====----");
				println("\t" + handler.getPatternName());
				/* 5.归约处理 */
				Object result = handler.handle(tempTokenList, tempObjectList);
				println("\t----==== 处理结果 ====----");
				println("\t" + result);
				/* 将结果压栈 */
				/* 6.指令进栈（非终结符进栈） */
				spi.push(new PredictionInstruction(PredictType.NONTERMINAL, -1));
				/* 7.数据进栈（结果进栈） */
				sobj.push(new FixedData(result));
			}
			println("\t----==== 指令堆栈 ====----");
			for (int i = spi.size() - 1; i >= 0; i--) {
				PredictionInstruction pi = spi.get(i);
				switch (pi.type) {
				case NONTERMINAL:
					println("\t" + i + ": [数据]");
					break;
				case TERMINAL:
					println("\t" + i + ": ["
							+ arrTerminals.get(pi.inst).toString() + "]");
					break;
				case EPSILON:
					println("\t" + i + ": ["
							+ TokenType.EOF.getName() + "]");
					break;
				default:
					break;
				}
			}
			println("\t----==== 数据堆栈 ====----");
			for (int i = sobj.size() - 1; i >= 0; i--) {
				println("\t" + i + ": [" + sobj.get(i) + "]");
			}
			println();
			/* 更新栈顶终结符索引 */
			if (spi.peek().type == PredictType.TERMINAL) {
				top = spi.peek().inst;
				topIndex = spi.size() - 1;
			} else {// 若栈顶为非终结符，则第二顶必为终结符
				top = spi.elementAt(spi.size() - 2).inst;
				topIndex = spi.size() - 2;
			}
		}
		println();
		if (sobj.peek().obj == null)
			return sobj.peek().token.object;
		return sobj.peek().obj;
	}

	/**
	 * 获取素短语模式
	 * 
	 * @param spi
	 *            素短语
	 * @return 模式字符串
	 */
	private static String getPattern(Collection<PredictionInstruction> spi) {
		StringBuilder sb = new StringBuilder();
		for (PredictionInstruction pi : spi) {
			sb.append(pi.type == PredictType.TERMINAL ? "1" : "0");
		}
		return sb.toString();
	}

	/**
	 * 获得矩阵描述
	 * @return 矩阵描述
	 */
	public String getMatrixString() {
		StringBuilder sb = new StringBuilder();
		int size = arrTerminals.size();
		sb.append("#### 算符优先关系矩阵 ####");
		sb.append(System.lineSeparator());
		sb.append("\t");
		for (int i = 0; i < size; i++) {
			sb.append(i).append("\t");
		}
		sb.append(System.lineSeparator());
		for (int i = 0; i < size; i++) {
			sb.append(i).append("\t");
			for (int j = 0; j < size; j++) {
				sb.append(table[i][j].getName()).append("\t");
			}
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return getMatrixString();
	}
}
