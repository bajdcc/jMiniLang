package priv.bajdcc.LALR1.grammar.semantic;

import java.util.List;

import priv.bajdcc.LALR1.grammar.error.SemanticException;
import priv.bajdcc.LALR1.grammar.error.SemanticException.SemanticError;
import priv.bajdcc.util.lexer.token.Token;

/**
 * 【语义分析】语义错误记录接口
 *
 * @author bajdcc
 */
public interface ISemanticRecorder {

	/**
	 * 记录一个错误
	 * 
	 * @param error
	 *            语义分析错误类型
	 * @param token
	 *            单词
	 */
	public void add(SemanticError error, Token token);

	/**
	 * 获取错误列表
	 * 
	 * @return 错误列表
	 */
	public List<SemanticException> getErrorList();

	/**
	 * 是否没有任何错误
	 * 
	 * @return 没有错误则为真
	 */
	public boolean isCorrect();
}
