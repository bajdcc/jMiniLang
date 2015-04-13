package priv.bajdcc.grammar.expression;

import priv.bajdcc.grammar.codegen.ICodegen;
import priv.bajdcc.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.lexer.token.TokenType;

/**
 * 【语义分析】基本表达式
 *
 * @author bajdcc
 */
public class Expression implements IExpression {

	/**
	 * 单词
	 */
	public Token token = null;
	
	@Override
	public Token getToken() {
		return token;
	}

	@Override
	public boolean isIdentifier() {
		return token.kToken == TokenType.ID;
	}

	@Override
	public boolean isLeftValue() {
		return token.kToken == TokenType.ID;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {
		
	}

	@Override
	public void genValueCode(ICodegen codegen) {
		
	}

	@Override
	public void genRefCode(ICodegen codegen) {
		
	}
}
