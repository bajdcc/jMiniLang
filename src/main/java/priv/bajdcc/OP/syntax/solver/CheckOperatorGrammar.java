package priv.bajdcc.OP.syntax.solver;

import priv.bajdcc.OP.syntax.ISyntaxComponentVisitor;
import priv.bajdcc.OP.syntax.exp.BranchExp;
import priv.bajdcc.OP.syntax.exp.RuleExp;
import priv.bajdcc.OP.syntax.exp.SequenceExp;
import priv.bajdcc.OP.syntax.exp.TokenExp;
import priv.bajdcc.util.VisitBag;

/**
 * 检查一个文法是否属于算符文法
 *
 * @author bajdcc
 */
public class CheckOperatorGrammar implements ISyntaxComponentVisitor {

	/**
	 * 是否刚刚遍历过非终结符
	 */
	private boolean nonTerminalJustPast = false;

	/**
	 * 重复的非终结符名称（假如存在）
	 */
	private String invalidName = null;

	/**
	 * 检查是否合法
	 *
	 * @return 名称是否合法
	 */
	public boolean isValid() {
		return invalidName == null;
	}

	/**
	 * 重复的非终结符名称
	 *
	 * @return 重复的非终结符名称
	 */
	public String getInvalidName() {
		return invalidName;
	}

	@Override
	public void visitBegin(TokenExp node, VisitBag bag) {
		bag.bVisitChildren = false;
		bag.bVisitEnd = false;
		if (invalidName == null) {
			nonTerminalJustPast = false;
		}
	}

	@Override
	public void visitBegin(RuleExp node, VisitBag bag) {
		bag.bVisitChildren = false;
		bag.bVisitEnd = false;
		if (nonTerminalJustPast) {
			invalidName = node.name;
		} else {
			nonTerminalJustPast = true;
		}
	}

	@Override
	public void visitBegin(SequenceExp node, VisitBag bag) {

	}

	@Override
	public void visitBegin(BranchExp node, VisitBag bag) {
		bag.bVisitEnd = false;
	}

	@Override
	public void visitEnd(TokenExp node) {

	}

	@Override
	public void visitEnd(RuleExp node) {

	}

	@Override
	public void visitEnd(SequenceExp node) {
		nonTerminalJustPast = false;
	}

	@Override
	public void visitEnd(BranchExp node) {

	}
}
