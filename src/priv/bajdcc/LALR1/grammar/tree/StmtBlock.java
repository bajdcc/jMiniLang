package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.closure.IClosureScope;

/**
 * 【语义分析】块语句
 *
 * @author bajdcc
 */
public class StmtBlock implements IStmt {

	private Block block = null;

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {
		block.analysis(recorder);
	}

	@Override
	public void genCode(ICodegen codegen) {
		block.genCode(codegen);
	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		StringBuilder sb = new StringBuilder();
		sb.append(prefix.toString());
		sb.append(block.print(prefix));
		return sb.toString();
	}

	@Override
	public void addClosure(IClosureScope scope) {

	}
}