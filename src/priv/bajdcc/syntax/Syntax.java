package priv.bajdcc.syntax;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.syntax.automata.npa.NPA;
import priv.bajdcc.syntax.error.IErrorHandler;
import priv.bajdcc.syntax.error.SyntaxException;
import priv.bajdcc.syntax.error.SyntaxException.SyntaxError;
import priv.bajdcc.syntax.exp.BranchExp;
import priv.bajdcc.syntax.exp.IExpCollction;
import priv.bajdcc.syntax.exp.OptionExp;
import priv.bajdcc.syntax.exp.PropertyExp;
import priv.bajdcc.syntax.exp.RuleExp;
import priv.bajdcc.syntax.exp.SequenceExp;
import priv.bajdcc.syntax.exp.TokenExp;
import priv.bajdcc.syntax.lexer.SyntaxLexer;
import priv.bajdcc.syntax.solver.FirstsetSolver;
import priv.bajdcc.syntax.stringify.SyntaxToString;
import priv.bajdcc.syntax.token.OperatorType;
import priv.bajdcc.syntax.token.Token;
import priv.bajdcc.syntax.token.TokenType;
import priv.bajdcc.utility.BitVector2;
import priv.bajdcc.utility.Position;

/**
 * 文法构造器
 * 
 * 语法示例： Z -> A | B[1] | `abc` | ( A[0] | `c`<terminal comment text> |
 * C[1]<comment> | C[storage id]{Error handler name})
 *
 * @author bajdcc
 */
public class Syntax {

	/**
	 * 终结符表
	 */
	protected ArrayList<TokenExp> m_arrTerminals = new ArrayList<TokenExp>();

	/**
	 * 终结符映射
	 */
	protected HashMap<String, TokenExp> m_mapTerminals = new HashMap<String, TokenExp>();

	/**
	 * 非终结符表
	 */
	protected ArrayList<RuleExp> m_arrNonTerminals = new ArrayList<RuleExp>();

	/**
	 * 非终结符映射
	 */
	protected HashMap<String, RuleExp> m_mapNonTerminals = new HashMap<String, RuleExp>();

	/**
	 * 错误处理表
	 */
	protected ArrayList<IErrorHandler> m_arrHandlers = new ArrayList<IErrorHandler>();
	/**
	 * 错误处理映射
	 */
	protected HashMap<String, IErrorHandler> m_mapHandlers = new HashMap<String, IErrorHandler>();

	/**
	 * 文法起始符号
	 */
	private String m_strBeginRuleName = "";

	/**
	 * 面向文法的词法分析器
	 */
	private SyntaxLexer m_SyntaxLexer = new SyntaxLexer();

	/**
	 * 当前字符
	 */
	private Token m_Token = null;

	/**
	 * 当前解析的文法规则
	 */
	private RuleExp m_Rule = null;

	/**
	 * 非确定性下推自动机
	 */
	protected NPA m_NPA = null;

	public Syntax() throws RegexException {
		this(true);
	}

	public Syntax(boolean ignoreLexError) throws RegexException {
		m_SyntaxLexer.discard(TokenType.COMMENT);
		m_SyntaxLexer.discard(TokenType.WHITSPACE);
		if (ignoreLexError) {
			m_SyntaxLexer.discard(TokenType.ERROR);
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
	 */
	public void addTerminal(String name,
			priv.bajdcc.lexer.token.TokenType type, Object obj) {
		TokenExp exp = new TokenExp(m_arrTerminals.size(), name, type, obj);
		if (m_mapTerminals.put(name, exp) == null) {
			m_arrTerminals.add(exp);
		}
	}

	/**
	 * 添加非终结符
	 * 
	 * @param name
	 *            非终结符名称
	 */
	public void addNonTerminal(String name) {
		RuleExp exp = new RuleExp(m_arrNonTerminals.size(), name);
		if (m_mapNonTerminals.put(name, exp) == null) {
			m_arrNonTerminals.add(exp);
		}
	}

	/**
	 * 添加错误处理器
	 * 
	 * @param name
	 *            处理器名
	 * @param handler
	 *            处理接口
	 */
	public void addErrorHandler(String name, IErrorHandler handler) {
		if (m_mapHandlers.put(name, handler) == null) {
			m_arrHandlers.add(handler);
		}
	}

	/**
	 * @param inferString
	 *            文法推导式
	 * @throws SyntaxException
	 */
	public void infer(String inferString) throws SyntaxException {
		m_SyntaxLexer.setContext(inferString);
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
		throw new SyntaxException(error, m_SyntaxLexer.position(),
				m_Token.m_Object);
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
		if (m_Token.m_kToken == type) {
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
		if (m_Token.m_kToken != type) {
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
		if (!m_mapNonTerminals.containsKey(m_Token.m_Object.toString())) {
			err(SyntaxError.UNDECLARED);
		}
		return m_mapNonTerminals.get(m_Token.m_Object.toString());
	}

	/**
	 * 匹配终结符
	 * 
	 * @throws SyntaxException
	 */
	private TokenExp matchTerminal() throws SyntaxException {
		match(TokenType.TERMINAL, SyntaxError.SYNTAX);
		if (!m_mapTerminals.containsKey(m_Token.m_Object.toString())) {
			err(SyntaxError.UNDECLARED);
		}
		return m_mapTerminals.get(m_Token.m_Object.toString());
	}

	/**
	 * 匹配错误处理器
	 * 
	 * @throws SyntaxException
	 */
	private IErrorHandler matchHandler() throws SyntaxException {
		match(TokenType.HANDLER, SyntaxError.SYNTAX);
		if (!m_mapHandlers.containsKey(m_Token.m_Object.toString())) {
			err(SyntaxError.UNDECLARED);
		}
		return m_mapHandlers.get(m_Token.m_Object.toString());
	}

	/**
	 * 匹配存储序号
	 * 
	 * @param exp
	 *            表达式
	 * @param storage
	 *            存储序号
	 * @return 属性对象
	 * @throws SyntaxException
	 */
	private PropertyExp matchStorage(ISyntaxComponent exp, Object storage)
			throws SyntaxException {
		PropertyExp property;
		if (m_Token.m_kToken == TokenType.STORAGE) {
			property = new PropertyExp((int) storage, null);
			next();
		} else {
			property = new PropertyExp(-1, null);
		}
		property.m_Expression = exp;
		return property;
	}

	/**
	 * 取下一个单词
	 */
	private Token next() {
		do {
			m_Token = m_SyntaxLexer.nextToken();
		} while (m_Token == null);
		return m_Token;
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
		m_Rule = m_mapNonTerminals.get(m_Token.m_Object.toString());
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
		m_Rule.m_Rule.m_arrRules.add(new RuleItem(exp, m_Rule.m_Rule));
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
			if ((m_Token.m_kToken == type && (m_Token.m_Object == null || m_Token.m_Object
					.equals(obj)))) {// 结束字符
				if (m_SyntaxLexer.index() == 0) {// 表达式为空
					err(SyntaxError.NULL);
				} else if (collection.isEmpty()) {// 部件为空
					err(SyntaxError.INCOMPLETE);
				} else {
					next();
					break;// 正常终止
				}
			} else if (m_Token.m_kToken == TokenType.EOF) {
				err(SyntaxError.INCOMPLETE);
			}
			ISyntaxComponent exp = null;// 当前待赋值的表达式
			switch (m_Token.m_kToken) {
			case OPERATOR:
				OperatorType op = (OperatorType) m_Token.m_Object;
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
					option.m_Expression = exp;
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
				PropertyExp property1 = matchStorage(exp, m_Token.m_Object);
				exp = property1;
				if (m_Token.m_kToken == TokenType.HANDLER) {
					property1.m_ErrorHandler = matchHandler();
					next();
				}
				break;
			case NONTERMINAL:
				exp = matchNonTerminal();
				next();
				PropertyExp property2 = matchStorage(exp, m_Token.m_Object);
				exp = property2;
				if (m_Token.m_kToken == TokenType.HANDLER) {
					property2.m_ErrorHandler = matchHandler();
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
	 * @throws SyntaxException
	 */
	public void initialize(String startSymbol) throws SyntaxException {
		m_strBeginRuleName = startSymbol;
		checkStartSymbol();
		semanticAnalysis();
		generateNGA();
	}

	/**
	 * 检测起始符号合法性
	 * 
	 * @throws SyntaxException
	 */
	private void checkStartSymbol() throws SyntaxException {
		if (!m_mapNonTerminals.containsKey(m_strBeginRuleName)) {
			err(SyntaxError.UNDECLARED);
		}
	}

	/**
	 * 进行语义分析
	 * 
	 * @throws SyntaxException
	 */
	private void semanticAnalysis() throws SyntaxException {
		/* 非终结符数量 */
		int size = m_arrNonTerminals.size();
		/* 计算规则的First集合 */
		for (RuleExp exp : m_arrNonTerminals) {
			for (RuleItem item : exp.m_Rule.m_arrRules) {
				FirstsetSolver solver = new FirstsetSolver();
				item.m_Expression.visit(solver);// 计算规则的First集合
				if (!solver.solve(item)) {// 若串长度可能为零，即产生空串
					err(SyntaxError.EPSILON,
							getSingleString(exp.m_strName, item.m_Expression));
				}
			}
		}
		/* 建立连通矩阵 */
		BitVector2 firstsetDependency = new BitVector2(size, size);// First集依赖矩阵
		firstsetDependency.clear();
		/* 计算非终结符First集合包含关系的布尔连通矩阵 */
		{
			int i = 0;
			for (RuleExp exp : m_arrNonTerminals) {
				for (RuleItem item : exp.m_Rule.m_arrRules) {
					for (RuleExp rule : item.m_setFirstSetRules) {
						firstsetDependency.set(i, rule.m_iID);
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
					m_arrNonTerminals.get(i).m_Rule.m_iRecursiveLevel = 1;// 直接左递归
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
					for (RuleExp exp : m_arrNonTerminals) {
						if (r.test(i, i)) {
							if (exp.m_Rule.m_iRecursiveLevel < 2) {
								exp.m_Rule.m_iRecursiveLevel = level;
							}
						}
						i++;
					}
				}
				/* 保存结果 */
				a = (BitVector2) r.clone();
			}
			/* 检查是否存在环并报告错误 */
			for (RuleExp exp : m_arrNonTerminals) {
				if (exp.m_Rule.m_iRecursiveLevel > 1) {
					err(SyntaxError.INDIRECT_RECURSION, exp.m_strName
							+ " level:" + exp.m_Rule.m_iRecursiveLevel);
				}
			}
		}
		/* 计算完整的First集合 */
		{
			/* 建立处理标记表 */
			BitSet processed = new BitSet(size);
			processed.clear();
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
							m_arrNonTerminals.get(i).m_strName);
				}
				/* 计算该规则的终结符First集合 */
				{
					Rule rule = m_arrNonTerminals.get(nodependencyRule).m_Rule;
					/* 计算规则的终结符First集合 */
					for (RuleItem item : rule.m_arrRules) {
						for (RuleExp exp : item.m_setFirstSetRules) {
							item.m_setFirstSetTokens
									.addAll(exp.m_Rule.m_arrTokens);
						}
					}
					/* 计算非终结符的终结符First集合 */
					for (RuleItem item : rule.m_arrRules) {
						rule.m_arrTokens.addAll(item.m_setFirstSetTokens);
					}
					/* 修正左递归规则的终结符First集合 */
					for (RuleItem item : rule.m_arrRules) {
						if (item.m_setFirstSetRules.contains(m_arrNonTerminals
								.get(nodependencyRule))) {
							item.m_setFirstSetTokens.addAll(rule.m_arrTokens);
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
		for (RuleExp exp : m_arrNonTerminals) {
			for (RuleItem item : exp.m_Rule.m_arrRules) {
				if (item.m_setFirstSetTokens.isEmpty()) {
					err(SyntaxError.FAILED,
							getSingleString(exp.m_strName, item.m_Expression));
				}
			}
		}
	}

	/**
	 * 生成非确定性下推自动机
	 */
	private void generateNGA() {
		m_NPA = new NPA(m_arrNonTerminals, m_arrTerminals,
				m_mapNonTerminals.get(m_strBeginRuleName).m_Rule);
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
		sb.append(System.getProperty("line.separator"));
		sb.append(m_strBeginRuleName);
		sb.append(System.getProperty("line.separator"));
		/* 终结符 */
		sb.append("#### 终结符 ####");
		sb.append(System.getProperty("line.separator"));
		for (TokenExp exp : m_arrTerminals) {
			sb.append(exp.toString());
			sb.append(System.getProperty("line.separator"));
		}
		/* 非终结符 */
		sb.append("#### 非终结符 ####");
		sb.append(System.getProperty("line.separator"));
		for (RuleExp exp : m_arrNonTerminals) {
			sb.append(exp.toString());
			sb.append(System.getProperty("line.separator"));
		}
		/* 推导规则 */
		sb.append("#### 文法产生式 ####");
		sb.append(System.getProperty("line.separator"));
		for (RuleExp exp : m_arrNonTerminals) {
			for (RuleItem item : exp.m_Rule.m_arrRules) {
				/* 规则正文 */
				sb.append(getSingleString(exp.m_strName, item.m_Expression));
				sb.append(System.getProperty("line.separator"));
				/* First集合 */
				sb.append("\t--== 终结符First集合 ==--");
				sb.append(System.getProperty("line.separator"));
				for (TokenExp token : item.m_setFirstSetTokens) {
					sb.append("\t\t" + token.m_strName);
					sb.append(System.getProperty("line.separator"));
				}
				sb.append("\t--== 非终结符First集合 ==--");
				sb.append(System.getProperty("line.separator"));
				for (RuleExp rule : item.m_setFirstSetRules) {
					sb.append("\t\t" + rule.m_strName);
					sb.append(System.getProperty("line.separator"));
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
		for (RuleExp exp : m_arrNonTerminals) {
			for (RuleItem item : exp.m_Rule.m_arrRules) {
				sb.append(getSingleString(exp.m_strName, item.m_Expression));
				sb.append(System.getProperty("line.separator"));
			}
		}
		return sb.toString();
	}

	/**
	 * 获得非确定性文法自动机描述
	 */
	public String getNGAString() {
		return m_NPA.getNGAString();
	}

	/**
	 * 获得非确定性下推自动机描述
	 */
	public String getNPAString() {
		return m_NPA.getNPAString();
	}

	@Override
	public String toString() {
		return getParagraphString();
	}
}
