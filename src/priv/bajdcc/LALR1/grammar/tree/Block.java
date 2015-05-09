package priv.bajdcc.LALR1.grammar.tree;

import java.util.ArrayList;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;

/**
 * 【语义分析】块
 *
 * @author bajdcc
 */
public class Block implements ICommon {

	/**
	 * 语句集合
	 */
	private ArrayList<IStmt> stmts = new ArrayList<IStmt>();

	public ArrayList<IStmt> getStmts() {
		return stmts;
	}

	public void setStmts(ArrayList<IStmt> stmts) {
		this.stmts = stmts;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void genCode(ICodegen codegen) {
		// TODO 自动生成的方法存根

	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append(System.getProperty("line.separator"));
		prefix.append("    ");
		for (IStmt stmt : stmts) {
			sb.append(stmt.print(prefix));
			sb.append(System.getProperty("line.separator"));
		}
		prefix.delete(0, 4);
		sb.append(prefix.toString() + "}");
		return sb.toString();
	}
}
