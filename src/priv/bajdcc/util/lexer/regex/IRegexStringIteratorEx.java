package priv.bajdcc.util.lexer.regex;

import java.util.ArrayList;

import priv.bajdcc.util.Position;
import priv.bajdcc.util.lexer.token.Token;

/**
 * 字符串迭代器附加接口
 * 
 * @author bajdcc
 *
 */
public interface IRegexStringIteratorEx {

	/**
	 * 是否到末尾
	 * 
	 * @see priv.bajdcc.util.lexer.token.TokenType
	 */
	boolean isEOF();

	/**
	 * 保存单词
	 */
	void saveToken();
	
	/**
	 * 返回之前的位置
	 */
	Position lastPosition();

	/**
	 * 获取当前单词
	 */
	Token token();
	
	/**
	 * 获取所有单词
	 */
	ArrayList<Token> tokenList();
	
	/**
	 * 获取错误现场
	 */
	String getErrorSnapshot(Position position);
}