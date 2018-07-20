package com.bajdcc.OP.syntax.solver;

import com.bajdcc.OP.syntax.ISyntaxComponentVisitor;
import com.bajdcc.OP.syntax.exp.BranchExp;
import com.bajdcc.OP.syntax.exp.RuleExp;
import com.bajdcc.OP.syntax.exp.SequenceExp;
import com.bajdcc.OP.syntax.exp.TokenExp;
import com.bajdcc.util.VisitBag;

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
		bag.setVisitChildren(false);
		bag.setVisitEnd(false);
		if (invalidName == null) {
			nonTerminalJustPast = false;
		}
	}

	@Override
	public void visitBegin(RuleExp node, VisitBag bag) {
		bag.setVisitChildren(false);
		bag.setVisitEnd(false);
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
		bag.setVisitEnd(false);
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
