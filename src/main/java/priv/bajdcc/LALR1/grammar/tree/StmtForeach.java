package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.CodegenBlock;
import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInstUnary;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import priv.bajdcc.util.lexer.token.KeywordType;
import priv.bajdcc.util.lexer.token.OperatorType;
import priv.bajdcc.util.lexer.token.Token;

/**
 * 【语义分析】迭代循环语句
 *
 * @author bajdcc
 */
public class StmtForeach implements IStmt {

	/**
	 * 变量
	 */
	private Token var = null;

	/**
	 * 迭代表达式
	 */
	private IExp enumerator = null;

	/**
	 * 块
	 */
	private Block block = null;

	public Token getVar() {
		return var;
	}

	public void setVar(Token var) {
		this.var = var;
	}

	public IExp getEnumerator() {
		return enumerator;
	}

	public void setEnumerator(IExp enumerator) {
		this.enumerator = enumerator;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {
		enumerator.analysis(recorder);
		block.analysis(recorder);
	}

	@Override
	public void genCode(ICodegen codegen) {
		codegen.genCode(RuntimeInst.ipushx);
		codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(var.object));
		codegen.genCode(RuntimeInst.ialloc);
		codegen.genCode(RuntimeInst.ipop);
		CodegenBlock cb = new CodegenBlock();
		RuntimeInstUnary start = codegen.genCode(RuntimeInst.ijmp, -1);
		int exit = codegen.getCodeIndex();
		codegen.genCode(RuntimeInst.ipop);
		cb.breakId = codegen.getCodeIndex();
		codegen.genCode(RuntimeInst.iyldx);
		RuntimeInstUnary breakJmp = codegen.genCode(RuntimeInst.ijmp, -1);
		cb.continueId = codegen.getCodeIndex();
		RuntimeInstUnary continueJmp = codegen.genCode(RuntimeInst.ijmp, -1);
		start.op1 = cb.continueId;
		int content = codegen.getCodeIndex();
		enumerator.genCode(codegen);
		codegen.genCode(RuntimeInst.ijnan, exit);
		codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(var.object));
		codegen.genCode(RuntimeInst.istore);
		codegen.genCode(RuntimeInst.ipop);
		codegen.getBlockService().enterBlockEntry(cb);
		block.genCode(codegen);
		codegen.getBlockService().leaveBlockEntry();
		continueJmp.op1 = codegen.getCodeIndex();
		codegen.genCode(RuntimeInst.ijmp, content);
		breakJmp.op1 = codegen.getCodeIndex();
	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		return prefix.toString() +
				KeywordType.FOREACH.getName() +
				" ( " +
				KeywordType.VARIABLE.getName() +
				" " +
				var.toRealString() +
				" " +
				OperatorType.COLON.getName() +
				" " +
				enumerator.print(prefix) +
				" ) " +
				block.print(prefix);
	}

	@Override
	public void addClosure(IClosureScope scope) {

	}
}