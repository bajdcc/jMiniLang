package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.closure.IClosureScope;

import java.util.ArrayList;
import java.util.List;

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
		stmts = new ArrayList<>();
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
		int start, end;
		for (IStmt stmt : stmts) {
			start = codegen.getCodeIndex();
			stmt.genCode(codegen);
			end = codegen.getCodeIndex() - 1;
			if (start <= end) {
				codegen.genDebugInfo(start, end, stmt.toString());
			}
			if (stmt instanceof StmtReturn) {
				StmtReturn ret = (StmtReturn) stmt;
				if (!ret.isYield())
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
		sb.append(prefix.toString()).append("}");
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
