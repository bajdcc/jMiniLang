package priv.bajdcc.lexer.regex;

import java.util.ArrayList;

import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.utility.Position;

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
	 * @see priv.bajdcc.lexer.token.TokenType
	 */
	public boolean isEOF();

	/**
	 * 保存单词
	 */
	public void saveToken();
	
	/**
	 * 返回之前的位置
	 */
	public Position lastPosition();

	/**
	 * 获取当前单词
	 */
	public Token token();
	
	/**
	 * 获取所有单词
	 */
	public ArrayList<Token> tokenList();
}