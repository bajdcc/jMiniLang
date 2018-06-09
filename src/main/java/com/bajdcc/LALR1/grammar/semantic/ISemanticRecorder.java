package com.bajdcc.LALR1.grammar.semantic;

import com.bajdcc.LALR1.grammar.error.SemanticException;
import com.bajdcc.LALR1.grammar.error.SemanticException.SemanticError;
import com.bajdcc.util.lexer.token.Token;

import java.util.List;

/**
 * 【语义分析】语义错误记录接口
 *
 * @author bajdcc
 */
public interface ISemanticRecorder {

	/**
	 * 记录一个错误
	 *
	 * @param error 语义分析错误类型
	 * @param token 单词
	 */
	void add(SemanticError error, Token token);

	/**
	 * 获取错误列表
	 *
	 * @return 错误列表
	 */
	List<SemanticException> getErrorList();

	/**
	 * 是否没有任何错误
	 *
	 * @return 没有错误则为真
	 */
	boolean isCorrect();
}
