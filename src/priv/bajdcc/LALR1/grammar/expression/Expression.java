package priv.bajdcc.LALR1.grammar.expression;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

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
