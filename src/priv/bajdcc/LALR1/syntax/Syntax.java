package priv.bajdcc.LALR1.syntax;

import priv.bajdcc.LALR1.semantic.token.ISemanticAction;
import priv.bajdcc.LALR1.syntax.automata.npa.NPA;
import priv.bajdcc.LALR1.syntax.exp.*;
import priv.bajdcc.LALR1.syntax.handler.IErrorHandler;
import priv.bajdcc.LALR1.syntax.handler.SyntaxException;
import priv.bajdcc.LALR1.syntax.handler.SyntaxException.SyntaxError;
import priv.bajdcc.LALR1.syntax.lexer.SyntaxLexer;
import priv.bajdcc.LALR1.syntax.rule.Rule;
import priv.bajdcc.LALR1.syntax.rule.RuleItem;
import priv.bajdcc.LALR1.syntax.solver.FirstsetSolver;
import priv.bajdcc.LALR1.syntax.stringify.SyntaxToString;
import priv.bajdcc.LALR1.syntax.token.OperatorType;
import priv.bajdcc.LALR1.syntax.token.Token;
import priv.bajdcc.LALR1.syntax.token.TokenType;
import priv.bajdcc.util.BitVector2;
import priv.bajdcc.util.Position;
import priv.bajdcc.util.lexer.error.RegexException;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

/**
 * 【语法分析】文法构造器
 * 
 * 语法示例：
 * 
 * <pre>
 * Z -&gt; A | B[1] | @abc | ( A[0] | @Terminal&lt;terminal comment text&gt; |
 * C[1]&lt;comment&gt; | C[storage id]#Action name#{Error handler name})
 * </pre>
 *
 * @author bajdcc
 */
public class Syntax {

	/**
	 * 终结符表
	 */
	static protected ArrayList<TokenExp> arrTerminals = new ArrayList<>();

	/**
	 * 终结符映射
	 */
	static protected HashMap<String, TokenExp> mapTerminals = new HashMap<>();

	/**
	 * 非终结符表
	 */
	static protected ArrayList<RuleExp> arrNonTerminals = new ArrayList<>();

	/**
	 * 非终结符映射
	 */
	static protected HashMap<String, RuleExp> mapNonTerminals = new HashMap<>();

	/**
	 * 错误处理表
	 */
	static protected ArrayList<IErrorHandler> arrHandlers = new ArrayList<>();
	/**
	 * 错误处理映射
	 */
	static protected HashMap<String, IErrorHandler> mapHandlers = new HashMap<>();

	/**
	 * 语义动作表
	 */
	static protected ArrayList<ISemanticAction> arrActions = new ArrayList<>();
	/**
	 * 语义动作映射
	 */
	static protected HashMap<String, ISemanticAction> mapActions = new HashMap<>();

	/**
	 * 文法起始符号
	 */
	static private String strBeginRuleName = "";

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

	/**
	 * 非确定性下推自动机
	 */
	static protected NPA npa = null;

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
	 * 添加终结符
	 * 
	 * @param name
	 *            终结符名称
	 * @param type
	 *            单词类型
	 * @param obj
	 *            单词信息
	 * @throws SyntaxException 词法错误
	 */
	public void addTerminal(String name,
			priv.bajdcc.util.lexer.token.TokenType type, Object obj)
			throws SyntaxException {
		TokenExp exp = new TokenExp(arrTerminals.size(), name, type, obj);
		if (!mapTerminals.containsKey(name)) {
			mapTerminals.put(name, exp);
			arrTerminals.add(exp);
		} else {
			err(SyntaxError.REDECLARATION, name);
		}
	}

	/**
	 * 添加非终结符
	 * 
	 * @param name
	 *            非终结符名称
	 * @throws SyntaxException 词法错误
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
	 * 添加错误处理器
	 * 
	 * @param name
	 *            处理器名
	 * @param handler
	 *            处理接口
	 * @throws SyntaxException 词法错误
	 */
	public void addErrorHandler(String name, IErrorHandler handler)
			throws SyntaxException {
		if (!mapHandlers.containsKey(name)) {
			mapHandlers.put(name, handler);
			arrHandlers.add(handler);
		} else {
			err(SyntaxError.REDECLARATION);
		}
	}

	/**
	 * 添加语义动作
	 * 
	 * @param name
	 *            动作名称
	 * @param handler
	 *            处理接口
	 * @throws SyntaxException 词法错误
	 */
	public void addActionHandler(String name, ISemanticAction handler)
			throws SyntaxException {
		if (!mapActions.containsKey(name)) {
			mapActions.put(name, handler);
			arrActions.add(handler);
		} else {
			err(SyntaxError.REDECLARATION);
		}
	}

	/**
	 * @param inferString
	 *            文法推导式
	 * @throws SyntaxException 词法错误
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
	 * @throws SyntaxException 词法错误
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
	 * @throws SyntaxException 词法错误
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
	 * @throws SyntaxException 词法错误
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
	 * @throws SyntaxException 词法错误
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
	 * @throws SyntaxException 词法错误
	 */
	private RuleExp matchNonTerminal() throws SyntaxException {
		match(TokenType.NONTERMINAL, SyntaxError.SYNTAX);
		if (!mapNonTerminals.containsKey(token.object.toString())) {
			err(SyntaxError.UNDECLARED, token);
		}
		return mapNonTerminals.get(token.object.toString());
	}

	/**
	 * 匹配终结符
	 * 
	 * @throws SyntaxException 词法错误
	 */
	private TokenExp matchTerminal() throws SyntaxException {
		match(TokenType.TERMINAL, SyntaxError.SYNTAX);
		if (!mapTerminals.containsKey(token.object.toString())) {
			err(SyntaxError.UNDECLARED, token);
		}
		return mapTerminals.get(token.object.toString());
	}

	/**
	 * 匹配错误处理器
	 * 
	 * @throws SyntaxException 词法错误
	 */
	private IErrorHandler matchHandler() throws SyntaxException {
		match(TokenType.HANDLER, SyntaxError.SYNTAX);
		if (!mapHandlers.containsKey(token.object.toString())) {
			err(SyntaxError.UNDECLARED, token);
		}
		return mapHandlers.get(token.object.toString());
	}

	/**
	 * 匹配语义动作
	 * 
	 * @throws SyntaxException 词法错误
	 */
	private ISemanticAction matchAction() throws SyntaxException {
		match(TokenType.ACTION, SyntaxError.SYNTAX);
		if (!mapActions.containsKey(token.object.toString())) {
			err(SyntaxError.UNDECLARED, token);
		}
		return mapActions.get(token.object.toString());
	}

	/**
	 * 匹配存储序号
	 * 
	 * @param exp
	 *            表达式
	 * @param storage
	 *            存储序号
	 * @return 属性对象
	 * @throws SyntaxException 词法错误
	 */
	private PropertyExp matchStorage(ISyntaxComponent exp, Object storage)
			throws SyntaxException {
		PropertyExp property;
		if (token.kToken == TokenType.STORAGE) {
			property = new PropertyExp((int) storage, null);
			next();
		} else {
			property = new PropertyExp(-1, null);
		}
		property.expression = exp;
		return property;
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
	 * @throws SyntaxException 词法错误
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
	 * @throws SyntaxException 词法错误
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
	 * @throws SyntaxException 词法错误
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
	 * @throws SyntaxException 词法错误
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
				case LPARAN:// '('
					exp = doAnalysis(TokenType.OPERATOR, OperatorType.RPARAN);// 递归分析
					break;
				case LSQUARE:// '['
					exp = doAnalysis(TokenType.OPERATOR, OperatorType.RSQUARE);// 递归分析
					OptionExp option = new OptionExp();// 包装
					option.expression = exp;
					exp = option;
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
				PropertyExp property1 = matchStorage(exp, token.object);
				exp = property1;
				if (token.kToken == TokenType.ACTION) {
					property1.actionHandler = matchAction();
					next();
				}
				if (token.kToken == TokenType.HANDLER) {
					property1.errorHandler = matchHandler();
					next();
				}
				break;
			case NONTERMINAL:
				exp = matchNonTerminal();
				next();
				PropertyExp property2 = matchStorage(exp, token.object);
				exp = property2;
				if (token.kToken == TokenType.ACTION) {
					property2.actionHandler = matchAction();
					next();
				}
				if (token.kToken == TokenType.HANDLER) {
					property2.errorHandler = matchHandler();
					next();
				}
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
	 * @throws SyntaxException 词法错误
	 */
	public void initialize(String startSymbol) throws SyntaxException {
		strBeginRuleName = startSymbol;
		checkStartSymbol();
		semanticAnalysis();
		generateNPA();
	}

	/**
	 * 检测起始符号合法性
	 * 
	 * @throws SyntaxException 词法错误
	 */
	private void checkStartSymbol() throws SyntaxException {
		if (!mapNonTerminals.containsKey(strBeginRuleName)) {
			err(SyntaxError.UNDECLARED);
		}
	}

	/**
	 * 进行语义分析
	 * 
	 * @throws SyntaxException 词法错误
	 */
	private void semanticAnalysis() throws SyntaxException {
		/* 非终结符数量 */
		int size = arrNonTerminals.size();
		/* 计算规则的First集合 */
		for (RuleExp exp : arrNonTerminals) {
			for (RuleItem item : exp.rule.arrRules) {
				FirstsetSolver solver = new FirstsetSolver();
				item.expression.visit(solver);// 计算规则的First集合
				if (!solver.solve(item)) {// 若串长度可能为零，即产生空串
					err(SyntaxError.EPSILON,
							getSingleString(exp.name, item.expression));
				}
			}
		}
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
		/* 计算间接左递归 */
		{
			/* 标记并清除直接左递归 */
			for (int i = 0; i < size; i++) {
				if (firstsetDependency.test(i, i)) {
					arrNonTerminals.get(i).rule.iRecursiveLevel = 1;// 直接左递归
					firstsetDependency.clear(i, i);
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
		/* 计算完整的First集合 */
		{
			/* 建立处理标记表 */
			BitSet processed = new BitSet(size);
			processed.clear();
			for (RuleExp arrNonTerminal : arrNonTerminals) {
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
							arrNonTerminal.name);
				}
				/* 计算该规则的终结符First集合 */
				{
					Rule rule = arrNonTerminals.get(nodependencyRule).rule;
					/* 计算规则的终结符First集合 */
					for (RuleItem item : rule.arrRules) {
						for (RuleExp exp : item.setFirstSetRules) {
							item.setFirstSetTokens.addAll(exp.rule.arrTokens);
						}
					}
					/* 计算非终结符的终结符First集合 */
					for (RuleItem item : rule.arrRules) {
						rule.arrTokens.addAll(item.setFirstSetTokens);
					}
					/* 修正左递归规则的终结符First集合 */
					for (RuleItem item : rule.arrRules) {
						if (item.setFirstSetRules.contains(arrNonTerminals
								.get(nodependencyRule))) {
							item.setFirstSetTokens.addAll(rule.arrTokens);
						}
					}
				}
				/* 清除该规则 */
				processed.set(nodependencyRule);
				for (int j = 0; j < size; j++) {
					firstsetDependency.clear(j, nodependencyRule);
				}
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
	}

	/**
	 * 生成非确定性下推自动机
	 */
	private void generateNPA() {
		npa = new NPA(arrNonTerminals, arrTerminals,
				mapNonTerminals.get(strBeginRuleName).rule, arrActions);
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
		StringBuilder sb = new StringBuilder();
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
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append(" -> ");
		SyntaxToString alg = new SyntaxToString();
		exp.visit(alg);
		sb.append(alg.toString());
		return sb.toString();
	}

	/**
	 * 获得段落式描述
	 * @return 段落式描述
	 */
	public String getParagraphString() {
		StringBuilder sb = new StringBuilder();
		/* 起始符号 */
		sb.append("#### 起始符号 ####");
		sb.append(System.lineSeparator());
		sb.append(strBeginRuleName);
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
				sb.append("\t--== 终结符First集合 ==--");
				sb.append(System.lineSeparator());
				for (TokenExp token : item.setFirstSetTokens) {
					sb.append("\t\t").append(token.name);
					sb.append(System.lineSeparator());
				}
				sb.append("\t--== 非终结符First集合 ==--");
				sb.append(System.lineSeparator());
				for (RuleExp rule : item.setFirstSetRules) {
					sb.append("\t\t").append(rule.name);
					sb.append(System.lineSeparator());
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 获得原推导式描述
	 * @return 原推导式描述
	 */
	public String getOriginalString() {
		StringBuilder sb = new StringBuilder();
		for (RuleExp exp : arrNonTerminals) {
			for (RuleItem item : exp.rule.arrRules) {
				sb.append(getSingleString(exp.name, item.expression));
				sb.append(System.lineSeparator());
			}
		}
		return sb.toString();
	}

	/**
	 * 获得非确定性文法自动机描述
	 * @return 非确定性文法自动机描述
	 */
	public String getNGAString() {
		return npa.getNGAString();
	}

	/**
	 * 获得非确定性下推自动机描述
	 * @return 获得非确定性下推自动机描述
	 */
	public String getNPAString() {
		return npa.getNPAString();
	}

	@Override
	public String toString() {
		return getParagraphString();
	}
}
