package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import priv.bajdcc.LALR1.grammar.type.TokenTools;
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
	public boolean isEnumerable() {
		return false;
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
		codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(token.object));
		if (token.kToken != TokenType.ID) {
			codegen.genCode(RuntimeInst.iload);
		} else {
			if (TokenTools.isExternalName(token)) {
				codegen.genCode(RuntimeInst.iloadx);
			} else {
				codegen.genCode(RuntimeInst.iloadv);
			}
		}
	}

	@Override
	public String toString() {
		return token.toRealString();
	}

	@Override
	public String print(StringBuilder prefix) {
		return toString();
	}

	@Override
	public void addClosure(IClosureScope scope) {
		if (token.kToken == TokenType.ID) {
			scope.addRef(token.object);
		}
	}

	@Override
	public void setYield() {

	}
}
