package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import priv.bajdcc.util.lexer.token.OperatorType;

/**
 * 【语义分析】字典
 *
 * @author bajdcc
 */
public class ExpMap implements IExp {

	@Override
	public boolean isConstant() {
		return true;
	}

	@Override
	public boolean isEnumerable() {
		return false;
	}

	@Override
	public IExp simplify(ISemanticRecorder recorder) {
		return this;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {

	}

	@Override
	public void genCode(ICodegen codegen) {
		codegen.genCode(RuntimeInst.imap);
	}

	@Override
	public String toString() {
        return OperatorType.LBRACE.getName() + OperatorType.RBRACE.getName();
    }

	@Override
	public String print(StringBuilder prefix) {
		return toString();
	}

	@Override
	public void addClosure(IClosureScope scope) {

	}

	@Override
	public void setYield() {

	}
}
