package priv.bajdcc.LALR1.grammar.tree;

import java.util.ArrayList;
import java.util.List;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.closure.IClosureScope;

/**
 * 【语义分析】块
 *
 * @author bajdcc
 */
public class Block implements ICommon {

	/**
	 * 语句集合
	 */
	private List<IStmt> stmts = null;

	public Block() {
		stmts = new ArrayList<IStmt>();
	}

	public Block(List<IStmt> stmts) {
		this.stmts = stmts;
	}

	public List<IStmt> getStmts() {
		return stmts;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {
		for (IStmt stmt : stmts) {
			stmt.analysis(recorder);
		}
	}

	@Override
	public void genCode(ICodegen codegen) {
		codegen.genCode(RuntimeInst.iscpi);
		for (IStmt stmt : stmts) {
			stmt.genCode(codegen);
			if (stmt instanceof StmtReturn) {
				break;
			}
		}
		codegen.genCode(RuntimeInst.iscpo);
	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append(System.lineSeparator());
		prefix.append("    ");
		for (IStmt stmt : stmts) {
			sb.append(stmt.print(prefix));
			sb.append(System.lineSeparator());
		}
		prefix.delete(0, 4);
		sb.append(prefix.toString() + "}");
		return sb.toString();
	}

	@Override
	public void addClosure(IClosureScope scope) {
		for (IStmt stmt : stmts) {
			stmt.addClosure(scope);
			if (stmt instanceof StmtReturn) {
				break;
			}
		}
	}
}
