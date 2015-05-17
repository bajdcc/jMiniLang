package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInstUnary;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.util.lexer.token.KeywordType;

/**
 * 【语义分析】条件语句
 *
 * @author bajdcc
 */
public class StmtIf implements IStmt {

	/**
	 * 表达式
	 */
	private IExp exp = null;

	/**
	 * 真块
	 */
	private Block trueBlock = null;

	/**
	 * 假块
	 */
	private Block falseBlock = null;

	public IExp getExp() {
		return exp;
	}

	public void setExp(IExp exp) {
		this.exp = exp;
	}

	public Block getTrueBlock() {
		return trueBlock;
	}

	public void setTrueBlock(Block trueBlock) {
		this.trueBlock = trueBlock;
	}

	public Block getFalseBlock() {
		return falseBlock;
	}

	public void setFalseBlock(Block falseBlock) {
		this.falseBlock = falseBlock;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {
		exp.analysis(recorder);
		trueBlock.analysis(recorder);
		if (falseBlock != null) {
			falseBlock.analysis(recorder);
		}
	}

	@Override
	public void genCode(ICodegen codegen) {
		exp.genCode(codegen);
		RuntimeInstUnary jf = codegen.genCode(RuntimeInst.ijf, -1);
		codegen.genCode(RuntimeInst.ipop);
		trueBlock.genCode(codegen);
		if (falseBlock != null) {
			RuntimeInstUnary jmp = codegen.genCode(RuntimeInst.ijmp, -1);
			jf.op1 = codegen.getCodeIndex();
			codegen.genCode(RuntimeInst.ipop);
			falseBlock.genCode(codegen);
			jmp.op1 = codegen.getCodeIndex();
		} else {
			jf.op1 = codegen.getCodeIndex();
			codegen.genCode(RuntimeInst.ipop);
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
		sb.append(KeywordType.IF.getName());
		sb.append(" (" + exp.print(prefix) + ") ");
		sb.append(trueBlock.print(prefix));
		if (falseBlock != null) {
			sb.append(" " + KeywordType.ELSE.getName() + " ");
			sb.append(falseBlock.print(prefix));
		}
		return sb.toString();
	}
}