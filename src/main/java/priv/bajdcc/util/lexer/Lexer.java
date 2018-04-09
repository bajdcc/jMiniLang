package priv.bajdcc.util.lexer;

import priv.bajdcc.util.Position;
import priv.bajdcc.util.lexer.algorithm.ITokenAlgorithm;
import priv.bajdcc.util.lexer.algorithm.TokenAlgorithmCollection;
import priv.bajdcc.util.lexer.algorithm.impl.*;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.regex.IRegexStringFilterHost;
import priv.bajdcc.util.lexer.regex.IRegexStringIteratorEx;
import priv.bajdcc.util.lexer.regex.RegexStringIterator;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * 【词法分析】词法分析器
 *
 * @author bajdcc
 */
public class Lexer extends RegexStringIterator implements
		IRegexStringFilterHost, IRegexStringIteratorEx, Cloneable {

	/**
	 * 算法集合（正则表达式匹配）
	 */
	private TokenAlgorithmCollection algorithmCollection = new TokenAlgorithmCollection(
			this, this);

	/**
	 * 字符转换算法
	 */
	private ITokenAlgorithm tokenAlgorithm = null;

	/**
	 * 丢弃的类型集合
	 */
	private HashSet<TokenType> setDiscardToken = new HashSet<>();

	/**
	 * 记录当前的单词
	 */
	protected Token token = null;

	/**
	 * 上次位置
	 */
	private Position lastPosition = new Position();

	public Lexer(String context) throws RegexException {
		super(context);
		initialize();
	}

	/**
	 * 获取一个单词
	 *
	 * @return 单词
	 */
	private Token scanInternal() {
		token = algorithmCollection.scan();
		if (setDiscardToken.contains(token.kToken)) {// 需要丢弃
			return null;
		}
		return token;
	}

	/**
	 * 获取一个单词
	 *
	 * @return 单词
	 */
	@Override
	public Token scan() {
		do {
			token = scanInternal();
		} while (token == null);
		lastPosition = token.position;
		return token;
	}

	/**
	 * 返回当前单词
	 *
	 * @return 当前单词
	 */
	@Override
	public Token token() {
		return token;
	}

	/**
	 * 设置丢弃符号
	 *
	 * @param type 要丢弃的符号类型（不建议丢弃EOF，因为需要用来判断结束）
	 */
	public void discard(TokenType type) {
		setDiscardToken.add(type);
	}

	@Override
	public void setFilter(ITokenAlgorithm alg) {
		tokenAlgorithm = alg;
	}

	@Override
	protected void transform() {
		super.transform();
		if (tokenAlgorithm != null) {
			data.kMeta = tokenAlgorithm.getMetaHash().get(data.chCurrent);
		}
	}

	/**
	 * 初始化（添加组件）
	 *
	 * @throws RegexException 正则表达式异常
	 */
	private void initialize() throws RegexException {
		//
		// ### 算法容器中装载解析组件是有一定顺序的 ###
		//
		// 组件调用原理：
		// 每个组件有自己的正则表达式匹配字符串
		// （可选）有自己的过滤方法，如字符串中的转义过滤
		//
		// 解析时，分别按序调用解析组件，若组件解析失败，则调用下一组件
		// 若某一组件解析成功，即返回匹配结果
		// 若全部解析失败，则调用出错处理（默认为前进一字符）
		//
		algorithmCollection.attach(new WhitespaceTokenizer());// 空白字符解析组件
		algorithmCollection.attach(new CommentTokenizer());// 注释解析组件
		algorithmCollection.attach(new Comment2Tokenizer());// 注释解析组件
		algorithmCollection.attach(new MacroTokenizer());// 宏解析组件
		algorithmCollection.attach(new OperatorTokenizer());// 操作符解析组件
		algorithmCollection.attach(new StringTokenizer());// 字符串解析组件
		algorithmCollection.attach(new CharacterTokenizer());// 字符解析组件
		algorithmCollection.attach(new IdentifierTokenizer());// 标识符/关键字解析组件
		algorithmCollection.attach(new NumberTokenizer());// 数字解析组件
	}

	@Override
	public IRegexStringIteratorEx ex() {
		return this;
	}

	@Override
	public boolean isEOF() {
		return token.kToken == TokenType.EOF;
	}

	@Override
	public void saveToken() {

	}

	@Override
	public Position lastPosition() {
		return lastPosition;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Lexer o = (Lexer) super.clone();
		o.algorithmCollection = algorithmCollection.copy(o, o);
		return o;
	}

	@Override
	public ArrayList<Token> tokenList() {
		return null;
	}

	@Override
	public String getErrorSnapshot(Position position) {
		if (position.iLine < 0 || position.iLine >= arrLinesNo.size()) {
			return null;
		}
		String str;
		int start = arrLinesNo.get(position.iLine) + 1;
		if (position.iLine == arrLinesNo.size() - 1) {
			if (start < context.length() - 1)
				str = context.substring(start, Math.min(context.length() - 1, start + position.iColumn));
			else
				str = "";
		} else {
			str = context.substring(start, arrLinesNo.get(position.iLine + 1));
		}
		if (position.iColumn < 0 || position.iColumn >= str.length()) {
			StringBuilder sb = new StringBuilder();
			sb.append(str);
			sb.append(System.lineSeparator());
			for (int i = 0; i < str.length(); i++) {
				sb.append('^');
			}
			sb.append('^');
			return sb.toString();
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(str);
			sb.append(System.lineSeparator());
			for (int i = 0; i < position.iColumn - 1; i++) {
				sb.append('^');
			}
			sb.append('^');
			return sb.toString();
		}
	}
}