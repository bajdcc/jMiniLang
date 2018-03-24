package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInstUnary;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import priv.bajdcc.util.lexer.token.KeywordType;
import priv.bajdcc.util.lexer.token.Token;

/**
 * 【语义分析】异常处理语句
 *
 * @author bajdcc
 */
public class StmtTry implements IStmt {

	/**
	 * 异常名
	 */
	private Token token = null;

	/**
	 * try块
	 */
	private Block tryBlock = null;

	/**
	 * catch块
	 */
	private Block catchBlock = null;


	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public Block getTryBlock() {
		return tryBlock;
	}

	public void setTryBlock(Block tryBlock) {
		this.tryBlock = tryBlock;
	}

	public Block getCatchBlock() {
		return catchBlock;
	}

	public void setCatchBlock(Block catchBlock) {
		this.catchBlock = catchBlock;
	}


	@Override
	public void analysis(ISemanticRecorder recorder) {
		tryBlock.analysis(recorder);
		catchBlock.analysis(recorder);
	}

	@Override
	public void genCode(ICodegen codegen) {
		RuntimeInstUnary t = codegen.genCode(RuntimeInst.itry, -1);
		codegen.genCode(RuntimeInst.iscpi);
		tryBlock.genCode(codegen);
		RuntimeInstUnary jmp = codegen.genCode(RuntimeInst.ijmp, -1);
		t.op1 = codegen.getCodeIndex();
		if (token != null) {
			// 'throw' push exp to stack top
			codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(token.object));
			codegen.genCode(RuntimeInst.ialloc);
		}
		catchBlock.genCode(codegen);
		jmp.op1 = codegen.getCodeIndex();
		codegen.genCode(RuntimeInst.ipop);
		codegen.genCode(RuntimeInst.iscpo);
		codegen.genCode(RuntimeInst.itry, -1);
	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		StringBuilder sb = new StringBuilder();
		sb.append(prefix.toString());
		sb.append(KeywordType.TRY.getName());
		sb.append(" ");
		sb.append(tryBlock.print(prefix));
		sb.append(" ");
		sb.append(KeywordType.CATCH.getName());
		sb.append(" ");
		if (token != null) {
			sb.append("( ");
			sb.append(token.toRealString());
			sb.append(" ) ");
		}
		sb.append(catchBlock.print(prefix));
		return sb.toString();
	}

	@Override
	public void addClosure(IClosureScope scope) {
		scope.addRef(token.object);
		tryBlock.addClosure(scope);
		catchBlock.addClosure(scope);
	}
}