package com.bajdcc.LALR1.grammar.tree;

import com.bajdcc.LALR1.grammar.codegen.ICodegen;
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import com.bajdcc.util.lexer.token.KeywordType;
import com.bajdcc.util.lexer.token.OperatorType;
import com.bajdcc.util.lexer.token.Token;

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
		return prefix.toString() +
				(imported ? KeywordType.IMPORT.getName() : KeywordType.EXPORT
						.getName()) +
				" " + name.toRealString() +
				OperatorType.SEMI.getName();
	}

	@Override
	public void addClosure(IClosureScope scope) {

	}
}