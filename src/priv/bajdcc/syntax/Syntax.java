package priv.bajdcc.syntax;

import java.util.ArrayList;
import java.util.HashMap;

import priv.bajdcc.lexer.error.RegexException;
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
import priv.bajdcc.syntax.exp.stringify.SyntaxToString;
import priv.bajdcc.syntax.lexer.SyntaxLexer;
import priv.bajdcc.syntax.token.OperatorType;
import priv.bajdcc.syntax.token.Token;
import priv.bajdcc.syntax.token.TokenType;

/**
 * 文法构造器
 *
 * @author bajdcc
 */
public class Syntax {

	/**
	 * 终结符表
	 */
	private ArrayList<TokenExp> m_arrTerminals = new ArrayList<TokenExp>();

	/**
	 * 终结符表
	 */
	private HashMap<String, TokenExp> m_mapTerminals = new HashMap<String, TokenExp>();

	/**
	 * 非终结符表
	 */
	private ArrayList<RuleExp> m_arrNonTerminals = new ArrayList<RuleExp>();

	/**
	 * 非终结符表
	 */
	private HashMap<String, RuleExp> m_mapNonTerminals = new HashMap<String, RuleExp>();

	/**
	 * 属性表
	 */
	private ArrayList<PropertyExp> m_arrProperties = new ArrayList<PropertyExp>();
	/**
	 * 属性表
	 */
	private HashMap<String, PropertyExp> m_mapProperties = new HashMap<String, PropertyExp>();

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
	 * @param regex
	 *            终结符对应的正则表达式
	 */
	public void addTerminal(String name, String regex) {
		TokenExp exp = new TokenExp(m_arrTerminals.size(), name, regex);
		if (m_mapTerminals.put(name, exp) == null) {
			m_arrTerminals.add(exp);
		}
	}

	/**
	 * 添加非终结符
	 * 
	 * @param token
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
		PropertyExp exp = new PropertyExp(m_mapProperties.size(), handler);
		if (m_mapProperties.put(name, exp) == null) {
			m_arrProperties.add(exp);
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
	 * 匹配属性表
	 * 
	 * @throws SyntaxException
	 */
	private PropertyExp matchProperty() throws SyntaxException {
		match(TokenType.HANDLER, SyntaxError.SYNTAX);
		if (!m_mapProperties.containsKey(m_Token.m_Object.toString())) {
			err(SyntaxError.UNDECLARED);
		}
		return m_mapProperties.get(m_Token.m_Object.toString());
	}

	/**
	 * 取下一个单词
	 */
	private Token next() {
		m_Token = m_SyntaxLexer.scan();
		return m_Token == null ? next() : m_Token;
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
				break;
			case NONTERMINAL:
				RuleExp rule = matchNonTerminal();
				next();
				if (m_Token.m_kToken == TokenType.HANDLER) {
					PropertyExp property = matchProperty();
					property.m_Expression = rule;
					exp = property;
					next();
				} else {
					exp = rule;
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

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (RuleExp exp : m_arrNonTerminals) {
			for (RuleItem item : exp.m_Rule.m_arrRules) {
				sb.append(exp.m_strName);
				sb.append(" -> ");
				SyntaxToString alg = new SyntaxToString();
				item.m_Expression.visit(alg);
				sb.append(alg.toString());
				sb.append(System.getProperty("line.separator"));
			}
		}
		return sb.toString();
	}
}
