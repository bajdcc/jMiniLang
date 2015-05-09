package priv.bajdcc.LALR1.grammar.semantic;

import java.util.ArrayList;
import java.util.List;

import priv.bajdcc.LALR1.grammar.error.SemanticException;
import priv.bajdcc.LALR1.grammar.error.SemanticException.SemanticError;
import priv.bajdcc.util.lexer.token.Token;

/**
 * 【语义分析】语义错误记录
 *
 * @author bajdcc
 */
public class SemanticRecorder implements ISemanticRecorder {

	private ArrayList<SemanticException> errors = new ArrayList<SemanticException>();

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

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("#### 语义错误列表 ####");
		sb.append(System.getProperty("line.separator"));
		for (SemanticException error : errors) {
			sb.append(error.toString());
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}
}
