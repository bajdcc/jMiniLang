package priv.bajdcc.LL1.syntax;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import priv.bajdcc.LL1.syntax.exp.BranchExp;
import priv.bajdcc.LL1.syntax.exp.IExpCollction;
import priv.bajdcc.LL1.syntax.exp.RuleExp;
import priv.bajdcc.LL1.syntax.exp.SequenceExp;
import priv.bajdcc.LL1.syntax.exp.TokenExp;
import priv.bajdcc.LL1.syntax.handler.SyntaxException;
import priv.bajdcc.LL1.syntax.handler.SyntaxException.SyntaxError;
import priv.bajdcc.LL1.syntax.lexer.SyntaxLexer;
import priv.bajdcc.LL1.syntax.rule.Rule;
import priv.bajdcc.LL1.syntax.rule.RuleItem;
import priv.bajdcc.LL1.syntax.solver.FirstSetSolver;
import priv.bajdcc.LL1.syntax.solver.FollowSetSolver;
import priv.bajdcc.LL1.syntax.stringify.SyntaxToString;
import priv.bajdcc.LL1.syntax.token.OperatorType;
import priv.bajdcc.LL1.syntax.token.Token;
import priv.bajdcc.LL1.syntax.token.TokenType;
import priv.bajdcc.util.BitVector2;
import priv.bajdcc.util.Position;
import priv.bajdcc.util.lexer.error.RegexException;

/**
 * 【语法分析】文法构造器
 * 
 * 语法示例：
 * 
 * <pre>
 * Z -> A | B | @abc &lt;comment&gt;
 * </pre>
 *
 * @author bajdcc
 */
public class Syntax {

	/**
	 * 终结符表
	 */
	protected ArrayList<TokenExp> arrTerminals = new ArrayList<TokenExp>();

	/**
	 * 终结符映射
	 */
	protected HashMap<String, TokenExp> mapTerminals = new HashMap<String, TokenExp>();

	/**
	 * 非终结符表
	 */
	protected ArrayList<RuleExp> arrNonTerminals = new ArrayList<RuleExp>();

	/**
	 * 非终结符映射
	 */
	protected HashMap<String, RuleExp> mapNonTerminals = new HashMap<String, RuleExp>();

	/**
	 * 文法起始符号
	 */
	protected String beginRuleName = null;

	/**
	 * 空串符号
	 */
	protected String epsilonName = null;

	/**
	 * 面向文法的词法分析器
	 */
	private SyntaxLexer syntaxLexer = new SyntaxLexer();

	/**
	 * 当前字符
	 */
	private Token token = null;

	/**
	 * 当前解析的文法规则
	 */
	private RuleExp rule = null;

	public Syntax() throws RegexException {
		this(true);
	}

	public Syntax(boolean ignoreLexError) throws RegexException {
		syntaxLexer.discard(TokenType.COMMENT);
		syntaxLexer.discard(TokenType.WHITSPACE);
		if (ignoreLexError) {
			syntaxLexer.discard(TokenType.ERROR);
		}
	}

	/**
	 * 定义空串名
	 * 
	 * @param name
	 *            空串名称
	 * @throws SyntaxException
	 */
	public void setEpsilonName(String name) throws SyntaxException {
		TokenExp exp = new TokenExp(arrTerminals.size(), name,
				priv.bajdcc.util.lexer.token.TokenType.EOF, null);
		if (!mapTerminals.containsKey(name)) {
			epsilonName = name;
			mapTerminals.put(name, exp);
			arrTerminals.add(exp);
		} else {
			err(SyntaxError.REDECLARATION);
		}
	}

	/**
	 * 添加终结符
	 * 
	 * @param name
	 *            终结符名称
	 * @param type
	 *            单词类型
	 * @param obj
	 *            单词信息
	 * @throws SyntaxException
	 */
	public void addTerminal(String name,
			priv.bajdcc.util.lexer.token.TokenType type, Object obj)
			throws SyntaxException {
		TokenExp exp = new TokenExp(arrTerminals.size(), name, type, obj);
		if (!mapTerminals.containsKey(name)) {
			mapTerminals.put(name, exp);
			arrTerminals.add(exp);
		} else {
			err(SyntaxError.REDECLARATION);
		}
	}

	/**
	 * 添加非终结符
	 * 
	 * @param name
	 *            非终结符名称
	 * @throws SyntaxException
	 */
	public void addNonTerminal(String name) throws SyntaxException {
		RuleExp exp = new RuleExp(arrNonTerminals.size(), name);
		if (!mapNonTerminals.containsKey(name)) {
			mapNonTerminals.put(name, exp);
			arrNonTerminals.add(exp);
		} else {
			err(SyntaxError.REDECLARATION);
		}
	}

	/**
	 * @param inferString
	 *            文法推导式
	 * @throws SyntaxException
	 */
	public void infer(String inferString) throws SyntaxException {
		syntaxLexer.setContext(inferString);
		compile();
	}

	/**
	 * 抛出异常
	 * 
	 * @param error
	 *            错误类型
	 * @throws SyntaxException
	 */
	private void err(SyntaxError error) throws SyntaxException {
		throw new SyntaxException(error, syntaxLexer.position(), token.object);
	}

	/**
	 * 抛出异常
	 * 
	 * @param error
	 *            错误类型
	 * @param obj
	 *            错误信息
	 * @throws SyntaxException
	 */
	private void err(SyntaxError error, Object obj) throws SyntaxException {
		throw new SyntaxException(error, new Position(), obj);
	}

	/**
	 * 匹配符号
	 * 
	 * @param type
	 *            匹配类型
	 * @param error
	 *            错误类型
	 * @throws SyntaxException
	 */
	private void expect(TokenType type, SyntaxError error)
			throws SyntaxException {
		if (token.kToken == type) {
			next();
		} else {
			err(error);
		}
	}

	/**
	 * 正确匹配当前字符
	 * 
	 * @param type
	 *            匹配类型
	 * @param error
	 *            匹配失败时抛出的异常
	 * @throws SyntaxException
	 */
	private void match(TokenType type, SyntaxError error)
			throws SyntaxException {
		if (token.kToken != type) {
			err(error);
		}
	}

	/**
	 * 匹配非终结符
	 * 
	 * @throws SyntaxException
	 */
	private RuleExp matchNonTerminal() throws SyntaxException {
		match(TokenType.NONTERMINAL, SyntaxError.SYNTAX);
		if (!mapNonTerminals.containsKey(token.object.toString())) {
			err(SyntaxError.UNDECLARED);
		}
		return mapNonTerminals.get(token.object.toString());
	}

	/**
	 * 匹配终结符
	 * 
	 * @throws SyntaxException
	 */
	private TokenExp matchTerminal() throws SyntaxException {
		match(TokenType.TERMINAL, SyntaxError.SYNTAX);
		if (!mapTerminals.containsKey(token.object.toString())) {
			err(SyntaxError.UNDECLARED);
		}
		return mapTerminals.get(token.object.toString());
	}

	/**
	 * 取下一个单词
	 */
	private Token next() {
		do {
			token = syntaxLexer.nextToken();
		} while (token == null);
		return token;
	}

	/**
	 * 编译推导式（将文本表达式转换成文法树）
	 * 
	 * @throws SyntaxException
	 */
	private void compile() throws SyntaxException {
		/* 处理左端非终结符 */
		doHead();
		/* 处理右端表达式 */
		doTail();
	}

	/**
	 * 处理左端非终结符
	 * 
	 * @throws SyntaxException
	 */
	private void doHead() throws SyntaxException {
		/* 匹配推导式左端的非终结符 */
		next();
		matchNonTerminal();
		/* 设定本次需要推导的非终结符 */
		rule = mapNonTerminals.get(token.object.toString());
		/* 匹配推导符号"->" */
		next();
		expect(TokenType.OPERATOR, SyntaxError.SYNTAX);
	}

	/**
	 * 处理右端表达式
	 * 
	 * @throws SyntaxException
	 */
	private void doTail() throws SyntaxException {
		/* 获得分析后的表达式根结点 */
		ISyntaxComponent exp = doAnalysis(TokenType.EOF, null);
		/* 将根结点添加进对应规则 */
		RuleItem item = new RuleItem(exp, rule.rule);
		onAddRuleItem(item);
		rule.rule.arrRules.add(item);
	}

	/**
	 * 创建推导式之后的回调函数
	 * 
	 * @param item
	 *            推导式
	 */
	protected void onAddRuleItem(RuleItem item) {

	}

	/**
	 * 分析表达式
	 * 
	 * @param type
	 *            结束类型
	 * @param obj
	 *            结束数据
	 * @return 表达式树根结点
	 * @throws SyntaxException
	 */
	private ISyntaxComponent doAnalysis(TokenType type, Object obj)
			throws SyntaxException {

		/* 新建序列用于存放结果 */
		SequenceExp sequence = new SequenceExp();
		/* 可能会使用的分支 */
		BranchExp branch = null;
		/* 添加子结点接口 */
		IExpCollction collection = sequence;
		/* 表达式通用接口 */
		ISyntaxComponent result = sequence;

		for (;;) {
			if ((token.kToken == type && (token.object == null || token.object
					.equals(obj)))) {// 结束字符
				if (syntaxLexer.index() == 0) {// 表达式为空
					err(SyntaxError.NULL);
				} else if (collection.isEmpty()) {// 部件为空
					err(SyntaxError.INCOMPLETE);
				} else {
					next();
					break;// 正常终止
				}
			} else if (token.kToken == TokenType.EOF) {
				err(SyntaxError.INCOMPLETE);
			}
			ISyntaxComponent exp = null;// 当前待赋值的表达式
			switch (token.kToken) {
			case OPERATOR:
				OperatorType op = (OperatorType) token.object;
				next();
				switch (op) {
				case ALTERNATIVE:
					if (collection.isEmpty())// 在此之前没有存储表达式 (|...)
					{
						err(SyntaxError.INCOMPLETE);
					} else {
						if (branch == null) {// 分支为空，则建立分支
							branch = new BranchExp();
							branch.add(sequence);// 用新建的分支包含并替代当前序列
							result = branch;
						}
						sequence = new SequenceExp();// 新建一个序列
						branch.add(sequence);
						continue;
					}
					break;
				default:
					err(SyntaxError.SYNTAX);
					break;
				}
				break;
			case EOF:
				return result;
			case TERMINAL:
				exp = matchTerminal();
				next();
				break;
			case NONTERMINAL:
				exp = matchNonTerminal();
				next();
				break;
			default:
				err(SyntaxError.SYNTAX);
				break;
			}

			if (exp != null) {
				sequence.add(exp);
			}
		}
		return result;
	}

	/**
	 * 初始化
	 * 
	 * @param startSymbol
	 *            开始符号
	 * @throws SyntaxException
	 */
	public void initialize(String startSymbol) throws SyntaxException {
		beginRuleName = startSymbol;
		checkStartSymbol();
		buildFirstAndFollow();
	}

	/**
	 * 检测起始符号合法性
	 * 
	 * @throws SyntaxException
	 */
	private void checkStartSymbol() throws SyntaxException {
		if (!mapNonTerminals.containsKey(beginRuleName)) {
			err(SyntaxError.UNDECLARED);
		}
	}

	/**
	 * 构造First集和Follow集
	 * 
	 * @throws SyntaxException
	 */
	private void buildFirstAndFollow() throws SyntaxException {
		/* 非终结符数量 */
		int size = arrNonTerminals.size();
		/* 计算规则的First集合 */
		boolean update;
		do {
			update = false;
			for (RuleExp exp : arrNonTerminals) {
				for (RuleItem item : exp.rule.arrRules) {
					FirstSetSolver solver = new FirstSetSolver();
					item.expression.visit(solver);// 计算规则的First集合
					item.epsilon = solver.solve(item);
					if (item.epsilon && !exp.rule.epsilon) {
						exp.rule.epsilon = true;
						update = true;
					}
				}
			}
		} while (update);
		/* 建立连通矩阵 */
		BitVector2 firstsetDependency = new BitVector2(size, size);// First集依赖矩阵
		firstsetDependency.clear();
		/* 计算非终结符First集合包含关系的布尔连通矩阵 */
		{
			int i = 0;
			for (RuleExp exp : arrNonTerminals) {
				for (RuleItem item : exp.rule.arrRules) {
					for (RuleExp rule : item.setFirstSetRules) {
						firstsetDependency.set(i, rule.id);
					}
				}
				i++;
			}
		}
		/* 检查间接左递归 */
		{
			/* 标记直接左递归 */
			for (int i = 0; i < size; i++) {
				if (firstsetDependency.test(i, i)) {// 出现直接左递归
					err(SyntaxError.DIRECT_RECURSION,
							arrNonTerminals.get(i).name);
				}
			}
			/* 获得拷贝 */
			BitVector2 a = (BitVector2) firstsetDependency.clone();
			BitVector2 b = (BitVector2) firstsetDependency.clone();
			BitVector2 r = new BitVector2(size, size);
			/* 检查是否出现环 */
			for (int level = 2; level < size; level++) {// Warshell算法：求有向图的传递闭包
				/* 进行布尔连通矩阵乘法，即r=aXb */
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						r.clear(i, j);
						for (int k = 0; k < size; k++) {
							boolean value = r.test(i, j)
									|| (a.test(i, k) && b.test(k, j));
							r.set(i, j, value);
						}
					}
				}
				/* 检查当前结果是否出现环 */
				{
					int i = 0;
					for (RuleExp exp : arrNonTerminals) {
						if (r.test(i, i)) {
							if (exp.rule.iRecursiveLevel < 2) {
								exp.rule.iRecursiveLevel = level;
							}
						}
						i++;
					}
				}
				/* 保存结果 */
				a = (BitVector2) r.clone();
			}
			/* 检查是否存在环并报告错误 */
			for (RuleExp exp : arrNonTerminals) {
				if (exp.rule.iRecursiveLevel > 1) {
					err(SyntaxError.INDIRECT_RECURSION, exp.name + " level:"
							+ exp.rule.iRecursiveLevel);
				}
			}
		}
		/* 建立规则的依赖关系 */
		ArrayList<Integer> nodependencyList = new ArrayList<Integer>();
		{
			/* 建立处理标记表 */
			BitSet processed = new BitSet(size);
			processed.clear();
			/* 寻找First集的依赖关系 */
			for (int i = 0; i < size; i++) {
				/* 找出一条无最左依赖的规则 */
				int nodependencyRule = -1;// 最左依赖的规则索引
				for (int j = 0; j < size; j++) {
					if (!processed.get(j)) {
						boolean empty = true;
						for (int k = 0; k < size; k++) {
							if (firstsetDependency.test(j, k)) {
								empty = false;
								break;
							}
						}
						if (empty) {// 找到
							nodependencyRule = j;
							break;
						}
					}
				}
				if (nodependencyRule == -1) {
					err(SyntaxError.MISS_NODEPENDENCY_RULE,
							arrNonTerminals.get(i).name);
				}
				/* 清除该规则 */
				processed.set(nodependencyRule);
				for (int j = 0; j < size; j++) {
					firstsetDependency.clear(j, nodependencyRule);
				}
				nodependencyList.add(nodependencyRule);
			}
		}
		for (int nodependencyRule : nodependencyList) {
			/* 计算该规则的终结符First集合 */
			Rule rule = arrNonTerminals.get(nodependencyRule).rule;
			/* 计算规则的终结符First集合 */
			for (RuleItem item : rule.arrRules) {
				for (RuleExp exp : item.setFirstSetRules) {
					item.setFirstSetTokens.addAll(exp.rule.arrFirsts);
				}
			}
			/* 计算非终结符的终结符First集合 */
			for (RuleItem item : rule.arrRules) {
				rule.arrFirsts.addAll(item.setFirstSetTokens);
			}
		}
		/* 搜索不能产生字符串的规则 */
		for (RuleExp exp : arrNonTerminals) {
			for (RuleItem item : exp.rule.arrRules) {
				if (item.setFirstSetTokens.isEmpty()) {
					err(SyntaxError.FAILED,
							getSingleString(exp.name, item.expression));
				}
			}
		}
		/* 求Follow集 */
		mapNonTerminals.get(beginRuleName).rule.setFollows.add(mapTerminals
				.get(epsilonName));
		do {
			update = false;
			for (RuleExp origin : arrNonTerminals) {
				for (RuleItem item : origin.rule.arrRules) {
					for (RuleExp target : arrNonTerminals) {
						FollowSetSolver solver = new FollowSetSolver(origin,
								target);
						if (origin.name.equals("E1") && target.name.equals("T")) {
							update = !!update;
						}
						item.expression.visit(solver);
						update |= solver.isUpdated();
					}
				}
			}
		} while (update);
		/* 将哈希表按ID排序并保存 */
		for (RuleExp exp : arrNonTerminals) {
			for (RuleItem item : exp.rule.arrRules) {
				item.arrFirstSetTokens = sortTerminal(item.setFirstSetTokens);
				item.parent.arrFollows = sortTerminal(item.parent.setFollows);
			}
		}
	}

	/**
	 * 给指定终结符集合按ID排序
	 * 
	 * @return
	 */
	private ArrayList<TokenExp> sortTerminal(Collection<TokenExp> colletion) {
		ArrayList<TokenExp> list = new ArrayList<TokenExp>(colletion);
		Collections.sort(list, new Comparator<TokenExp>() {
			@Override
			public int compare(TokenExp o1, TokenExp o2) {
				return o1.id > o2.id ? 1 : (o1.id == o2.id ? 0 : -1);
			}
		});
		return list;
	}

	/**
	 * 获得单一产生式描述
	 * 
	 * @param name
	 *            非终结符名称
	 * @param exp
	 *            表达式树
	 * @param focused
	 *            焦点
	 * @param front
	 *            前向
	 * @return 原产生式描述
	 */
	public static String getSingleString(String name, ISyntaxComponent exp,
			ISyntaxComponent focused, boolean front) {
		StringBuffer sb = new StringBuffer();
		sb.append(name);
		sb.append(" -> ");
		SyntaxToString alg = new SyntaxToString(focused, front);
		exp.visit(alg);
		sb.append(alg.toString());
		return sb.toString();
	}

	/**
	 * 获得单一产生式描述
	 * 
	 * @param name
	 *            非终结符名称
	 * @param exp
	 *            表达式树
	 * @return 原产生式描述
	 */
	public static String getSingleString(String name, ISyntaxComponent exp) {
		StringBuffer sb = new StringBuffer();
		sb.append(name);
		sb.append(" -> ");
		SyntaxToString alg = new SyntaxToString();
		exp.visit(alg);
		sb.append(alg.toString());
		return sb.toString();
	}

	/**
	 * 获得段落式描述
	 */
	public String getParagraphString() {
		StringBuffer sb = new StringBuffer();
		/* 起始符号 */
		sb.append("#### 起始符号 ####");
		sb.append(System.lineSeparator());
		sb.append(beginRuleName);
		sb.append(System.lineSeparator());
		/* 终结符 */
		sb.append("#### 终结符 ####");
		sb.append(System.lineSeparator());
		for (TokenExp exp : arrTerminals) {
			sb.append(exp.toString());
			sb.append(System.lineSeparator());
		}
		/* 非终结符 */
		sb.append("#### 非终结符 ####");
		sb.append(System.lineSeparator());
		for (RuleExp exp : arrNonTerminals) {
			sb.append(exp.toString());
			sb.append(System.lineSeparator());
		}
		/* 推导规则 */
		sb.append("#### 文法产生式 ####");
		sb.append(System.lineSeparator());
		for (RuleExp exp : arrNonTerminals) {
			for (RuleItem item : exp.rule.arrRules) {
				/* 规则正文 */
				sb.append(getSingleString(exp.name, item.expression));
				sb.append(System.lineSeparator());
				/* First集合 */
				sb.append("\t--== First ==--");
				sb.append(System.lineSeparator());
				for (TokenExp token : item.arrFirstSetTokens) {
					sb.append("\t\t" + token.toString());
					sb.append(System.lineSeparator());
				}
				sb.append("\t--== Follow ==--");
				sb.append(System.lineSeparator());
				for (TokenExp token : item.parent.arrFollows) {
					sb.append("\t\t" + token.toString());
					sb.append(System.lineSeparator());
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 获得原推导式描述
	 */
	public String getOriginalString() {
		StringBuffer sb = new StringBuffer();
		for (RuleExp exp : arrNonTerminals) {
			for (RuleItem item : exp.rule.arrRules) {
				sb.append(getSingleString(exp.name, item.expression));
				sb.append(System.lineSeparator());
			}
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return getParagraphString();
	}
}
