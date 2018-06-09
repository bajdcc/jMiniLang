package com.bajdcc.LALR1.grammar.semantic;

import com.bajdcc.LALR1.grammar.error.SemanticException;
import com.bajdcc.LALR1.grammar.error.SemanticException.SemanticError;
import com.bajdcc.util.lexer.regex.IRegexStringIterator;
import com.bajdcc.util.lexer.token.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * 【语义分析】语义错误记录
 *
 * @author bajdcc
 */
public class SemanticRecorder implements ISemanticRecorder {

	private ArrayList<SemanticException> errors = new ArrayList<>();

	@Override
	public void add(SemanticError error, Token token) {
		errors.add(new SemanticException(error, token));
	}

	@Override
	public List<SemanticException> getErrorList() {
		return errors;
	}

	@Override
	public boolean isCorrect() {
		return errors.isEmpty();
	}

	public String toString(IRegexStringIterator iter) {
		StringBuilder sb = new StringBuilder();
		sb.append("#### 语义错误列表 ####");
		sb.append(System.lineSeparator());
		for (SemanticException error : errors) {
			sb.append(error.toString(iter));
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("#### 语义错误列表 ####");
		sb.append(System.lineSeparator());
		for (SemanticException error : errors) {
			sb.append(error.toString());
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
}
