package priv.bajdcc.LALR1.grammar.error;

import priv.bajdcc.util.lexer.token.Token;

/**
 * 【语义分析】语义错误结构
 *
 * @author bajdcc
 */
public class SemanticError {

	/**
	 * 对应单词
	 */
	public Token token = null;
	
	/**
	 * 错误信息
	 */
	public String message = "";
}
