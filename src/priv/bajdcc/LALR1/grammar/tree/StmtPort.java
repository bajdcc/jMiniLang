package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
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
	 * 是否为导入
	 */
	private boolean imported = true;

	public Token getName() {
		return name;
	}

	public void setName(Token name) {
		this.name = name;
	}

	public boolean isImported() {
		return imported;
	}

	public void setImported(boolean imported) {
		this.imported = imported;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {

	}

	@Override
	public void genCode(ICodegen codegen) {
		if (imported) {
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
		sb.append(imported ? KeywordType.IMPORT.getName() : KeywordType.EXPORT
				.getName());
		sb.append(" " + name.toRealString());
		sb.append(OperatorType.SEMI.getName());
		return sb.toString();
	}

	@Override
	public void addClosure(IClosureScope scope) {
		
	}
}