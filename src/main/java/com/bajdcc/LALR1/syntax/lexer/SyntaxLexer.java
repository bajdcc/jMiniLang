package com.bajdcc.LALR1.syntax.lexer;

import com.bajdcc.LALR1.syntax.lexer.tokenizer.*;
import com.bajdcc.LALR1.syntax.token.Token;
import com.bajdcc.LALR1.syntax.token.TokenType;
import com.bajdcc.util.lexer.algorithm.ITokenAlgorithm;
import com.bajdcc.util.lexer.algorithm.TokenAlgorithmCollection;
import com.bajdcc.util.lexer.algorithm.impl.WhitespaceTokenizer;
import com.bajdcc.util.lexer.error.RegexException;
import com.bajdcc.util.lexer.regex.IRegexStringFilterHost;
import com.bajdcc.util.lexer.regex.RegexStringIterator;
import com.bajdcc.util.lexer.token.MetaType;

import java.util.HashSet;

/**
 * 解析文法的词法分析器
 *
 * @author bajdcc
 */
public class SyntaxLexer extends RegexStringIterator implements
		IRegexStringFilterHost {

	/**
	 * 算法集合（正则表达式匹配）
	 */
	private TokenAlgorithmCollection algorithmCollections = new TokenAlgorithmCollection(
			this, this);

	/**
	 * 字符转换算法
	 */
	private ITokenAlgorithm tokenAlgorithm = null;

	/**
	 * 丢弃的类型集合
	 */
	private HashSet<TokenType> setDiscardToken = new HashSet<>();

	public SyntaxLexer() throws RegexException {
		initialize();
	}

	/**
	 * 设置要分析的内容
	 *
	 * @param context 文法推导式
	 */
	public void setContext(String context) {
		/* 初始化 */
		this.context = context;
		position.iColumn = 0;
		position.iLine = 0;
		data.chCurrent = 0;
		data.iIndex = 0;
		data.kMeta = MetaType.END;
		stkIndex.clear();
		stkPosition.clear();
	}

	/**
	 * 获取一个单词
	 *
	 * @return 单词
	 */
	public Token nextToken() {
		Token token = Token.transfer(algorithmCollections.scan());
		if (setDiscardToken.contains(token.kToken)) {// 需要丢弃
			return null;
		}
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
	 * @throws RegexException 正则表达式错误
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
		algorithmCollections.attach(new WhitespaceTokenizer());// 空白字符解析组件
		algorithmCollections.attach(new CommentTokenizer());// 注释解析组件
		algorithmCollections.attach(new PropertyTokenizer());// 属性解析组件
		algorithmCollections.attach(new ActionTokenizer());// 语义动作解析组件
		algorithmCollections.attach(new TerminalTokenizer());// 终结符解析组件
		algorithmCollections.attach(new NonTerminalTokenizer());// 非终结符解析组件
		algorithmCollections.attach(new NumberTokenizer());// 存储序号解析组件
		algorithmCollections.attach(new OperatorTokenizer());// 操作符解析组件
	}
}
