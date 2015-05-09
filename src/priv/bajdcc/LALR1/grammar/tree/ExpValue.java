package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

/**
 * 【语义分析】基本操作数
 *
 * @author bajdcc
 */
public class ExpValue implements IExp {

	/**
	 * 单词
	 */
	private Token token = null;

	public Token getToken() {
		return token;
	}

	public Token setToken(Token token) {
		return this.token = token;
	}

	@Override
	public boolean isConstant() {
		return token.kToken != TokenType.ID;
	}
	
	@Override
	public IExp simplify(ISemanticRecorder recorder) {
		return this;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {

	}

	@Override
	public void genCode(ICodegen codegen) {

	}

	@Override
	public String toString() {
		return token.toRealString();
	}

	@Override
	public String print(StringBuilder prefix) {
		return toString();
	}
}
