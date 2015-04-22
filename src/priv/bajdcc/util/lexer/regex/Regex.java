package priv.bajdcc.util.lexer.regex;

import java.util.HashMap;
import java.util.HashSet;

import priv.bajdcc.util.lexer.automata.dfa.DFA;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.error.RegexException.RegexError;
import priv.bajdcc.util.lexer.stringify.RegexToString;
import priv.bajdcc.util.lexer.token.MetaType;
import priv.bajdcc.util.lexer.token.TokenUtility;

/**
 * 【词法分析】## 正则表达式分析工具 ##<br/>
 * 用于生成语法树<br/>
 * 语法同一般的正则表达式，只有贪婪模式，没有前/后向匹配， 没有捕获功能，仅用于匹配。
 * 
 * @author bajdcc
 */
public class Regex extends RegexStringIterator {

	/**
	 * 是否为调试模式（打印信息）
	 */
	private boolean bDebug = false;

	/**
	 * 表达式树根结点
	 */
	private IRegexComponent expression = null;

	/**
	 * DFA
	 */
	private DFA dfa = null;

	/**
	 * DFA状态转换表
	 */
	private int[][] transition = null;

	/**
	 * 终态表
	 */
	private HashSet<Integer> setFinalStatus = new HashSet<Integer>();

	/**
	 * 字符区间表
	 */
	private CharacterMap chMap = null;

	/**
	 * 字符串过滤接口
	 */
	private IRegexStringFilter filter = null;

	public void setFilter(IRegexStringFilter filter) {
		this.filter = filter;
	}

	private static HashMap<Character, MetaType> g_mapMeta = new HashMap<Character, MetaType>();

	static {
		MetaType[] metaTypes = new MetaType[] { MetaType.LPARAN,
				MetaType.RPARAN, MetaType.STAR, MetaType.PLUS, MetaType.QUERY,
				MetaType.CARET, MetaType.LSQUARE, MetaType.RSQUARE,
				MetaType.BAR, MetaType.ESCAPE, MetaType.DASH, MetaType.LBRACE,
				MetaType.RBRACE, MetaType.COMMA, MetaType.DOT,
				MetaType.NEW_LINE, MetaType.CARRIAGE_RETURN, MetaType.BACKSPACE };
		for (MetaType meta : metaTypes) {
			g_mapMeta.put(meta.getChar(), meta);
		}
	}

	public Regex(String pattern) throws RegexException {
		this(pattern, false);
	}

	public Regex(String pattern, boolean debug) throws RegexException {
		super(pattern);
		bDebug = debug;
		compile();
	}

	/**
	 * ## 编译表达式 ##<br/>
	 * 
	 * @throws RegexException
	 */
	private void compile() throws RegexException {
		translate();
		/* String->AST */
		expression = analysis(MetaType.END.getChar(), MetaType.END);
		if (bDebug) {
			System.out.println("#### 正则表达式语法树 ####");
			System.out.println(toString());
		}
		/* AST->ENFA->NFA->DFA */
		dfa = new DFA(expression, bDebug);
		/* DFA Transfer Table */
		buildTransition();
	}

	/**
	 * 建立DFA状态转换表
	 */
	private void buildTransition() {
		/* 字符区间映射表 */
		chMap = dfa.getCharacterMap();
		/* DFA状态转移表 */
		transition = dfa.buildTransition(setFinalStatus);
	}

	/**
	 * 匹配（默认为贪婪匹配）
	 * 
	 * @param string
	 *            被匹配的字符串
	 * @return 匹配结果（若不成功则返回空）
	 */
	public String match(String string) {
		return match(string, true);
	}

	/**
	 * 匹配
	 * 
	 * @param string
	 *            被匹配的字符串
	 * @return 匹配结果（若不成功则返回空）
	 */
	public String match(String string, boolean greed) {
		String matchString = null;
		final Boolean isgreed = greed;
		IRegexStringAttribute attr = new IRegexStringAttribute() {
			public String result;

			@Override
			public void setResult(String result) {
				this.result = result;
			}

			@Override
			public String getResult() {
				return result;
			}

			@Override
			public boolean getGreedMode() {
				return isgreed;
			}
		};
		if (match(new RegexStringIterator(string), attr)) {
			matchString = attr.getResult();
		}
		return matchString;
	}

	/**
	 * 匹配算法（DFA状态表）
	 * 
	 * @param iterator
	 *            字符串遍历接口
	 * @param attr
	 *            输出的匹配字符串
	 * @return 是否匹配成功
	 */
	public boolean match(IRegexStringIterator iterator,
			IRegexStringAttribute attr) {
		/* 使用全局字符映射表 */
		int[] charMap = chMap.getStatus();
		/* 保存当前位置 */
		iterator.snapshot();
		/* 当前状态 */
		int status = 0;
		/* 上次经过的终态 */
		int lastFinalStatus = -1;
		/* 上次经过的终态位置 */
		int lastIndex = -1;
		/* 是否为贪婪模式 */
		boolean greed = attr.getGreedMode();
		/* 存放匹配字符串 */
		StringBuilder sb = new StringBuilder();
		/* 是否允许通过终态结束识别 */
		boolean allowFinal = false;
		for (;;) {
			if (setFinalStatus.contains(status)) {// 经过终态
				if (greed) {// 贪婪模式
					if (lastFinalStatus == -1) {
						iterator.snapshot();// 保存位置
					} else {
						iterator.cover();// 覆盖位置
					}
					lastFinalStatus = status;// 记录上次状态
					lastIndex = sb.length();
				} else if (!allowFinal) {// 非贪婪模式，则匹配完成
					iterator.discard();// 匹配成功，丢弃位置
					attr.setResult(sb.toString());
					return true;
				}
			}
			char local = 0;
			boolean skipStore = false;// 取消存储当前字符
			/* 获得当前字符 */
			if (filter != null) {
				RegexStringIteratorData data = filter.filter(iterator);// 过滤
				local = data.chCurrent;
				skipStore = data.kMeta == MetaType.NULL;
				allowFinal = data.kMeta == MetaType.MUST_SAVE;// 强制跳过终态
			} else {
				if (!iterator.available()) {
					local = 0;
				} else {
					local = iterator.current();
					iterator.next();
				}
			}
			/* 存储字符 */
			if (!skipStore) {
				sb.append(local);
			}
			/* 获得字符区间索引 */
			int charClass = charMap[local];
			/* 状态转移 */
			int refer = -1;
			if (charClass != -1) {// 区间有效，尝试转移
				refer = transition[status][charClass];
			}
			if (refer == -1) {// 失败
				iterator.restore();
				if (lastFinalStatus == -1) {// 匹配失败
					return false;
				} else {// 使用上次经过的终态匹配结果
					iterator.discard();// 匹配成功，丢弃位置
					attr.setResult(sb.substring(0, lastIndex));
					return true;
				}
			} else {
				status = refer;// 更新状态
			}
		}
	}

	private IRegexComponent analysis(char terminal, MetaType meta)
			throws RegexException {
		Constructure sequence = new Constructure(false);// 建立序列以存储表达式
		Constructure branch = null;// 建立分支以存储'|'型表达式，是否是分支有待预测
		Constructure result = sequence;

		for (;;) {
			if ((data.kMeta == meta && data.chCurrent == terminal)) {// 结束字符
				if (data.iIndex == 0) {// 表达式为空
					err(RegexError.NULL);
				} else if (sequence.arrComponents.isEmpty()) {// 部件为空
					err(RegexError.INCOMPLETE);
				} else {
					next();
					break;// 正常终止
				}
			} else if (data.kMeta == MetaType.END) {
				err(RegexError.INCOMPLETE);
			}
			IRegexComponent expression = null;// 当前待赋值的表达式
			switch (data.kMeta) {
			case BAR:// '|'
				next();
				if (sequence.arrComponents.isEmpty())// 在此之前没有存储表达式 (|...)
				{
					err(RegexError.INCOMPLETE);
				} else {
					if (branch == null) {// 分支为空，则建立分支
						branch = new Constructure(true);
						branch.arrComponents.add(sequence);// 用新建的分支包含并替代当前序列
						result = branch;
					}
					sequence = new Constructure(false);// 新建一个序列
					branch.arrComponents.add(sequence);
					continue;
				}
				break;
			case LPARAN:// '('
				next();
				expression = analysis(MetaType.RPARAN.getChar(),
						MetaType.RPARAN);// 递归分析
				break;
			default:
				break;
			}

			if (expression == null) {// 当前不是表达式，则作为字符
				Charset charset = new Charset();// 当前待分析的字符集
				expression = charset;
				switch (data.kMeta) {
				case ESCAPE:// '\\'
					next();
					escape(charset, true);// 处理转义
					break;
				case DOT:// '.'
					data.kMeta = MetaType.CHARACTER;
					escape(charset, true);
					break;
				case LSQUARE: // '['
					next();
					range(charset);
					break;
				case END: // '\0'
					return result;
				default:
					if (!charset.addChar(data.chCurrent)) {
						err(RegexError.RANGE);
					}
					next();
					break;
				}
			}

			Repetition rep = null;// 循环
			switch (data.kMeta) {
			case QUERY:// '?'
				next();
				rep = new Repetition(expression, 0, 1);
				sequence.arrComponents.add(rep);
				break;
			case PLUS:// '+'
				next();
				rep = new Repetition(expression, 1, -1);
				sequence.arrComponents.add(rep);
				break;
			case STAR:// '*'
				next();
				rep = new Repetition(expression, 0, -1);
				sequence.arrComponents.add(rep);
				break;
			case LBRACE: // '{'
				next();
				rep = new Repetition(expression, 0, -1);
				quantity(rep);
				sequence.arrComponents.add(rep);
				break;
			default:
				sequence.arrComponents.add(expression);
				break;
			}
		}

		return result;
	}

	/**
	 * 处理转义字符
	 * 
	 * @param charset
	 *            字符集
	 * @param extend
	 *            是否支持扩展如\d \w等
	 * @throws RegexException
	 */
	private void escape(Charset charset, boolean extend) throws RegexException {
		char ch = data.chCurrent;
		if (data.kMeta == MetaType.CHARACTER) {// 字符
			next();
			if (extend) {
				if (TokenUtility.isUpperLetter(ch) || ch == '.') {
					charset.bReverse = true;// 大写则取反
				}
				char cl = Character.toLowerCase(ch);
				switch (cl) {
				case 'd':// 数字
					charset.addRange('0', '9');
					return;
				case 'a':// 字母
					charset.addRange('a', 'z');
					charset.addRange('A', 'Z');
					return;
				case 'w':// 标识符
					charset.addRange('a', 'z');
					charset.addRange('A', 'Z');
					charset.addRange('0', '9');
					charset.addChar('_');
					return;
				case 's':// 空白字符
					charset.addChar('\r');
					charset.addChar('\n');
					charset.addChar('\t');
					charset.addChar('\b');
					charset.addChar('\f');
					charset.addChar(' ');
					return;
				default:
					break;
				}
			}
			if (TokenUtility.isLetter(ch)) {// 如果为字母
				ch = utility.fromEscape(ch, RegexError.ESCAPE);
				if (!charset.addChar(ch)) {
					err(RegexError.RANGE);
				}
			}
		} else if (data.kMeta == MetaType.END) {
			err(RegexError.INCOMPLETE);
		} else {// 功能字符则转义
			next();
			if (!charset.addChar(ch)) {
				err(RegexError.RANGE);
			}
		}
	}

	/**
	 * 处理字符集合
	 * 
	 * @param charset
	 *            字符集
	 * @throws RegexException
	 */
	private void range(Charset charset) throws RegexException {
		if (data.kMeta == MetaType.CARET) {// '^'取反
			next();
			charset.bReverse = true;
		}
		while (data.kMeta != MetaType.RSQUARE) {// ']'
			if (data.kMeta == MetaType.CHARACTER) {
				character(charset);
				char lower = data.chCurrent; // lower bound
				next();
				if (data.kMeta == MetaType.DASH) {// '-'
					next();
					character(charset);
					char upper = data.chCurrent; // upper bound
					next();
					if (lower > upper) {// check bound
						err(RegexError.RANGE);
					}
					if (!charset.addRange(lower, upper)) {
						err(RegexError.RANGE);
					}
				} else {
					if (!charset.addChar(lower)) {
						err(RegexError.RANGE);
					}
				}
			} else if (data.kMeta == MetaType.ESCAPE) {
				next();
				escape(charset, false);
			} else if (data.kMeta == MetaType.END) {
				err(RegexError.INCOMPLETE);
			} else {
				charset.addChar(data.chCurrent);
				next();
			}
		}
		next();
	}

	/**
	 * 处理字符
	 * 
	 * @param charset
	 *            字符集
	 * @throws RegexException
	 */
	private void character(Charset charset) throws RegexException {
		if (data.kMeta == MetaType.ESCAPE) {// '\\'
			next();
			escape(charset, false);
		} else if (data.kMeta == MetaType.END) {// '\0'
			err(RegexError.INCOMPLETE);
		} else if (data.kMeta != MetaType.CHARACTER
				&& data.kMeta != MetaType.DASH) {
			err(RegexError.CTYPE);
		}
	}

	/**
	 * 处理量词
	 * 
	 * @throws RegexException
	 */
	private void quantity(Repetition rep) throws RegexException {
		int lower, upper;
		lower = upper = digit();// 循环下界
		if (lower == -1) {
			err(RegexError.BRACE);
		}
		if (data.kMeta == MetaType.COMMA) {// ','
			next();
			if (data.kMeta == MetaType.RBRACE) {// '}'
				upper = -1;// 上界为无穷大
			} else {
				upper = digit();// 得到循环上界
				if (upper == -1) {
					err(RegexError.BRACE);
				}
			}
		}
		if (upper != -1 && upper < lower) {
			err(RegexError.RANGE);
		}
		expect(MetaType.RBRACE, RegexError.BRACE);
		rep.iLowerBound = lower;
		rep.iUpperBound = upper;
	}

	/**
	 * 十进制数字转换
	 * 
	 * @return 数字
	 */
	private int digit() {
		int index = data.iIndex;
		while (Character.isDigit(data.chCurrent)) {
			next();
		}
		try {
			return Integer.valueOf(
					context.substring(index, data.iIndex), 10);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	@Override
	protected void transform() {
		if (g_mapMeta.containsKey(data.chCurrent)) {
			data.kMeta = g_mapMeta.get(data.chCurrent);// 功能字符
		} else {
			data.kMeta = MetaType.CHARACTER;// 一般字符
		}
	}

	@Override
	public String toString() {
		RegexToString alg = new RegexToString();// 表达式树序列化算法初始化
		expression.visit(alg);// 遍历树
		return alg.toString();
	}

	/**
	 * 获取字符区间描述
	 */
	public String getStatusString() {
		return dfa.getStatusString();
	}

	/**
	 * 获取NFA描述
	 */
	public String getNFAString() {
		return dfa.getNFAString();
	}

	/**
	 * 获取DFA描述
	 */
	public String getDFAString() {
		return dfa.getDFAString();
	}

	/**
	 * 获取DFATable描述
	 */
	public String getDFATableString() {
		return dfa.getDFATableString();
	}
}
