package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.CodegenBlock;
import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInstUnary;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.util.lexer.token.KeywordType;
import priv.bajdcc.util.lexer.token.OperatorType;

/**
 * 【语义分析】循环语句
 *
 * @author bajdcc
 */
public class StmtFor implements IStmt {

	/**
	 * 初始化
	 */
	private IExp var = null;

	/**
	 * 条件
	 */
	private IExp cond = null;

	/**
	 * 控制
	 */
	private IExp ctrl = null;

	/**
	 * 块
	 */
	private Block block = null;

	public IExp getVar() {
		return var;
	}

	public void setVar(IExp var) {
		this.var = var;
	}

	public IExp getCond() {
		return cond;
	}

	public void setCond(IExp cond) {
		this.cond = cond;
	}

	public IExp getCtrl() {
		return ctrl;
	}

	public void setCtrl(IExp ctrl) {
		this.ctrl = ctrl;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {
		if (var != null) {
			var.analysis(recorder);
		}
		if (cond != null) {
			cond.analysis(recorder);
		}
		if (ctrl != null) {
			ctrl.analysis(recorder);
		}
		block.analysis(recorder);
	}

	@Override
	public void genCode(ICodegen codegen) {
		if (var != null) {
			var.genCode(codegen);
		}
		CodegenBlock cb = new CodegenBlock();
		RuntimeInstUnary start = codegen.genCode(RuntimeInst.ijmp, -1);
		cb.breakId = codegen.getCodeIndex();
		RuntimeInstUnary breakJmp = codegen.genCode(RuntimeInst.ijmp, -1);
		cb.continueId = codegen.getCodeIndex();
		RuntimeInstUnary continueJmp = codegen.genCode(RuntimeInst.ijmp, -1);
		start.op1 = cb.continueId;
		int content = codegen.getCodeIndex();
		if (cond != null) {
			cond.genCode(codegen);
			RuntimeInstUnary falseCond = codegen.genCode(RuntimeInst.ijf, -1);
			codegen.genCode(RuntimeInst.ipop);
			RuntimeInstUnary trueCond = codegen.genCode(RuntimeInst.ijmp, -1);
			falseCond.op1 = codegen.getCodeIndex();
			codegen.genCode(RuntimeInst.ipop);
			codegen.genCode(RuntimeInst.ijmp, cb.breakId);
			trueCond.op1 = codegen.getCodeIndex();
		}
		codegen.getBlockService().enterBlockEntry(cb);
		block.genCode(codegen);
		codegen.getBlockService().leaveBlockEntry();
		continueJmp.op1 = codegen.getCodeIndex();
		if (ctrl != null) {
			ctrl.genCode(codegen);
			codegen.genCode(RuntimeInst.ipop);
		}
		codegen.genCode(RuntimeInst.ijmp, content);
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
		sb.append(KeywordType.FOR.getName());
		sb.append(" ( ");
		if (var != null) {
			sb.append(var.print(prefix));
			sb.append(OperatorType.SEMI.getName());
		}
		if (cond != null) {
			sb.append(" ");
			sb.append(cond.print(prefix));
			sb.append(OperatorType.SEMI.getName());
		}
		if (ctrl != null) {
			sb.append(" ");
			sb.append(ctrl.print(prefix));
		}
		sb.append(" ) ");
		sb.append(block.print(prefix));
		return sb.toString();
	}
}