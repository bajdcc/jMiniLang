package com.bajdcc.OP.syntax;

import com.bajdcc.OP.syntax.exp.*;
import com.bajdcc.OP.syntax.handler.SyntaxException;
import com.bajdcc.OP.syntax.handler.SyntaxException.SyntaxError;
import com.bajdcc.OP.syntax.lexer.SyntaxLexer;
import com.bajdcc.OP.syntax.rule.RuleItem;
import com.bajdcc.OP.syntax.solver.CheckOperatorGrammar;
import com.bajdcc.OP.syntax.stringify.SyntaxToString;
import com.bajdcc.OP.syntax.token.OperatorType;
import com.bajdcc.OP.syntax.token.Token;
import com.bajdcc.OP.syntax.token.TokenType;
import com.bajdcc.util.Position;
import com.bajdcc.util.lexer.error.RegexException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 【语法分析】文法构造器
 * <p>
 * 语法示例：
 * <p>
 * <pre>
 * Z -&gt; A | B | @abc &lt;comment&gt;
 * </pre>
 *
 * @author bajdcc
 */
public class Syntax {

	/**
	 * 终结符表
	 */
	protected ArrayList<TokenExp> arrTerminals = new ArrayList<>();

	/**
	 * 终结符映射
	 */
	protected HashMap<String, TokenExp> mapTerminals = new HashMap<>();

	/**
	 * 非终结符表
	 */
	protected ArrayList<RuleExp> arrNonTerminals = new ArrayList<>();

	/**
	 * 非终结符映射
	 */
	protected HashMap<String, RuleExp> mapNonTerminals = new HashMap<>();

	/**
	 * 文法起始符号
	 */
	protected String beginRuleName = null;

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
	 * 添加终结符
	 *
	 * @param name 终结符名称
	 * @param type 单词类型
	 * @param obj  单词信息
	 * @throws SyntaxException 词法错误
	 */
	public void addTerminal(String name,
	                        com.bajdcc.util.lexer.token.TokenType type, Object obj)
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
	 * @param name 非终结符名称
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
	 * @param inferString 文法推导式
	 * @throws SyntaxException 词法错误
	 */
	public void infer(String inferString) throws SyntaxException {
		syntaxLexer.setContext(inferString);
		compile();
	}

	/**
	 * 抛出异常
	 *
	 * @param error 错误类型
	 * @throws SyntaxException 词法错误
	 */
	protected void err(SyntaxError error) throws SyntaxException {
		throw new SyntaxException(error, syntaxLexer.position(), token.object);
	}

	/**
	 * 抛出异常
	 *
	 * @param error 错误类型
	 * @param obj   错误信息
	 * @throws SyntaxException 词法错误
	 */
	private void err(SyntaxError error, Object obj) throws SyntaxException {
		throw new SyntaxException(error, new Position(), obj);
	}

	/**
	 * 匹配符号
	 *
	 * @param type  匹配类型
	 * @param error 错误类型
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
	 * @param type  匹配类型
	 * @param error 匹配失败时抛出的异常
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
			err(SyntaxError.UNDECLARED);
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
	 * @param item 推导式
	 */
	protected void onAddRuleItem(RuleItem item) {

	}

	/**
	 * 分析表达式
	 *
	 * @param type 结束类型
	 * @param obj  结束数据
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

		for (; ; ) {
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
	 * @param startSymbol 开始符号
	 * @throws SyntaxException 词法错误
	 */
	public void initialize(String startSymbol) throws SyntaxException {
		beginRuleName = startSymbol;
		checkStartSymbol();
		checkValid();
	}

	/**
	 * 检测起始符号合法性
	 *
	 * @throws SyntaxException 词法错误
	 */
	private void checkStartSymbol() throws SyntaxException {
		if (!mapNonTerminals.containsKey(beginRuleName)) {
			err(SyntaxError.UNDECLARED);
		}
	}

	/**
	 * 检查产生式的合法性
	 *
	 * @throws SyntaxException 词法错误
	 */
	private void checkValid() throws SyntaxException {
		for (RuleExp exp : arrNonTerminals) {
			for (RuleItem item : exp.rule.arrRules) {
				CheckOperatorGrammar check = new CheckOperatorGrammar();
				item.expression.visit(check);
				if (!check.isValid()) {
					err(SyntaxError.CONSEQUENT_NONTERMINAL,
							check.getInvalidName()
									+ ": "
									+ getSingleString(exp.name, item.expression));
				}
			}
		}
	}

	/**
	 * 获得单一产生式描述
	 *
	 * @param name    非终结符名称
	 * @param exp     表达式树
	 * @param focused 焦点
	 * @param front   前向
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
	 * @param name 非终结符名称
	 * @param exp  表达式树
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
	 *
	 * @return 段落式描述
	 */
	public String getParagraphString() {
		StringBuilder sb = new StringBuilder();
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
				/* FirstVT集合 */
				sb.append("\t--== FirstVT ==--");
				sb.append(System.lineSeparator());
				for (TokenExp token : exp.rule.arrFirstVT) {
					sb.append("\t\t").append(token.toString());
					sb.append(System.lineSeparator());
				}
				/* LastVT集合 */
				sb.append("\t--== LastVT ==--");
				sb.append(System.lineSeparator());
				for (TokenExp token : exp.rule.arrLastVT) {
					sb.append("\t\t").append(token.toString());
					sb.append(System.lineSeparator());
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 获得原推导式描述
	 *
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

	@Override
	public String toString() {
		return getParagraphString();
	}
}
