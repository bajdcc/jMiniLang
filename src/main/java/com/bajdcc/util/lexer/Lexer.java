package com.bajdcc.util.lexer;

import com.bajdcc.util.Position;
import com.bajdcc.util.lexer.algorithm.ITokenAlgorithm;
import com.bajdcc.util.lexer.algorithm.TokenAlgorithmCollection;
import com.bajdcc.util.lexer.algorithm.impl.*;
import com.bajdcc.util.lexer.error.RegexException;
import com.bajdcc.util.lexer.regex.IRegexStringFilterHost;
import com.bajdcc.util.lexer.regex.IRegexStringIteratorEx;
import com.bajdcc.util.lexer.regex.RegexStringIterator;
import com.bajdcc.util.lexer.token.MetaType;
import com.bajdcc.util.lexer.token.Token;
import com.bajdcc.util.lexer.token.TokenType;

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
		if (setDiscardToken.contains(token.getType())) {// 需要丢弃
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
		lastPosition = token.getPosition();
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
			MetaType type = tokenAlgorithm.getMetaHash().get(getData().getCurrent());
			if (type != null)
				getData().setMeta(type);
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
		return token.getType() == TokenType.EOF;
	}

	@Override
	public void saveToken() {

	}

	@Override
	public Position lastPosition() {
		return lastPosition;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
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
		if (position.getLine() < 0 || position.getLine() >= getArrLinesNo().size()) {
			return null;
		}
		String str;
		int start = getArrLinesNo().get(position.getLine()) + 1;
		if (position.getLine() == getArrLinesNo().size() - 1) {
			if (start < getContext().length())
				str = getContext().substring(start, Math.min(getContext().length(), start + position.getColumn()));
			else
				str = "";
		} else {
			str = getContext().substring(start, getArrLinesNo().get(position.getLine() + 1));
		}
		if (position.getColumn() < 0 || position.getColumn() >= str.length()) {
			StringBuilder sb = new StringBuilder();
			sb.append(str);
			sb.append(System.lineSeparator());
			for (int i = 0; i < str.length(); i++) {
				sb.append('^');
			}
			return sb.toString();
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(str);
			sb.append(System.lineSeparator());
			for (int i = 0; i < position.getColumn() - 1; i++) {
				sb.append('^');
			}
			sb.append('^');
			return sb.toString();
		}
	}
}