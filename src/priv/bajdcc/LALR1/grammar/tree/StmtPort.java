package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.util.lexer.token.KeywordType;
import priv.bajdcc.util.lexer.token.OperatorType;
import priv.bajdcc.util.lexer.token.Token;

/**
 * 【语义分析】导入/导出语句
 *
 * @author bajdcc
 */
public class StmtPort implements IStmt {

	/**
	 * 名称
	 */
	private Token name = null;

	/**
	 * 限定符
	 */
	private Token spec = null;

	public Token getName() {
		return name;
	}

	public void setName(Token name) {
		this.name = name;
	}

	public Token getSpec() {
		return spec;
	}

	public void setSpec(Token spec) {
		this.spec = spec;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {

	}

	@Override
	public void genCode(ICodegen codegen) {
		KeywordType keyword = (KeywordType) spec.object;
		if (keyword == KeywordType.IMPORT) {
			codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(name.object));
			codegen.genCode(RuntimeInst.iimp);
		}
	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		StringBuilder sb = new StringBuilder();
		sb.append(prefix.toString());
		sb.append(spec.toRealString());
		sb.append(" " + name.toRealString());
		sb.append(OperatorType.SEMI.getName());
		return sb.toString();
	}
}