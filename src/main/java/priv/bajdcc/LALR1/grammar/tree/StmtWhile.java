package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.CodegenBlock;
import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInstUnary;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import priv.bajdcc.util.lexer.token.KeywordType;

/**
 * 【语义分析】循环语句
 *
 * @author bajdcc
 */
public class StmtWhile implements IStmt {

	/**
	 * 条件
	 */
	private IExp cond = null;

	/**
	 * 块
	 */
	private Block block = null;

	public IExp getCond() {
		return cond;
	}

	public void setCond(IExp cond) {
		this.cond = cond;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {
		if (cond != null) {
			cond.analysis(recorder);
		}
		block.analysis(recorder);
	}

	@Override
	public void genCode(ICodegen codegen) {
		CodegenBlock cb = new CodegenBlock();
		RuntimeInstUnary start = codegen.genCode(RuntimeInst.ijmp, -1);
		cb.breakId = codegen.getCodeIndex();
		RuntimeInstUnary breakJmp = codegen.genCode(RuntimeInst.ijmp, -1);
		cb.continueId = codegen.getCodeIndex();
		RuntimeInstUnary continueJmp = codegen.genCode(RuntimeInst.ijmp, -1);
		start.op1 = codegen.getCodeIndex();
		cond.genCode(codegen);
		codegen.genCode(RuntimeInst.ijf, cb.breakId);
		codegen.getBlockService().enterBlockEntry(cb);
		block.genCode(codegen);
		codegen.getBlockService().leaveBlockEntry();
		continueJmp.op1 = codegen.getCodeIndex();
		codegen.genCode(RuntimeInst.ijmp, start.op1);
		breakJmp.op1 = codegen.getCodeIndex();
	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		StringBuilder sb = new StringBuilder();
		sb.append(prefix.toString());
		sb.append(KeywordType.WHILE.getName());
		sb.append(" ( ");
		sb.append(cond.print(prefix));
		sb.append(" ) ");
		sb.append(block.print(prefix));
		return sb.toString();
	}

	@Override
	public void addClosure(IClosureScope scope) {
		cond.addClosure(scope);
		block.addClosure(scope);
	}
}