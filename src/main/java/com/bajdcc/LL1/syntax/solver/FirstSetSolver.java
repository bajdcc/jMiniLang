package com.bajdcc.LL1.syntax.solver;

import com.bajdcc.LL1.syntax.ISyntaxComponent;
import com.bajdcc.LL1.syntax.ISyntaxComponentVisitor;
import com.bajdcc.LL1.syntax.exp.BranchExp;
import com.bajdcc.LL1.syntax.exp.RuleExp;
import com.bajdcc.LL1.syntax.exp.SequenceExp;
import com.bajdcc.LL1.syntax.exp.TokenExp;
import com.bajdcc.LL1.syntax.rule.RuleItem;
import com.bajdcc.util.VisitBag;
import com.bajdcc.util.lexer.token.TokenType;

import java.util.HashSet;

/**
 * 求解一个产生式的First集合
 *
 * @author bajdcc
 */
public class FirstSetSolver implements ISyntaxComponentVisitor {

	/**
	 * 终结符表
	 */
	private HashSet<TokenExp> setTokens = new HashSet<>();

	/**
	 * 非终结符表
	 */
	private HashSet<RuleExp> setRules = new HashSet<>();

	/**
	 * 产生式推导的串长度是否可能为零
	 */
	private boolean bZero = true;

	/**
	 * 求解
	 *
	 * @param target 目标产生式对象
	 * @return 产生式是否合法
	 */
	public boolean solve(RuleItem target) {
		target.setFirstSetTokens = new HashSet<>(setTokens);
		target.setFirstSetRules = new HashSet<>(setRules);
		return bZero;
	}

	@Override
	public void visitBegin(TokenExp node, VisitBag bag) {
		bag.setVisitChildren(false);
		bag.setVisitEnd(false);
		setTokens.add(node);
		if (node.kType == TokenType.EOF) {
			bZero = true;
		} else if (bZero) {
			bZero = false;
		}
	}

	@Override
	public void visitBegin(RuleExp node, VisitBag bag) {
		bag.setVisitChildren(false);
		bag.setVisitEnd(false);
		setRules.add(node);
		if (bZero && !node.rule.epsilon) {
			bZero = false;
		}
	}

	@Override
	public void visitBegin(SequenceExp node, VisitBag bag) {
		bag.setVisitChildren(false);
		bag.setVisitEnd(false);
		boolean zero = false;
		for (ISyntaxComponent exp : node.arrExpressions) {
			exp.visit(this);
			zero = bZero;
			if (!zero) {
				break;
			}
		}
		bZero = zero;
	}

	@Override
	public void visitBegin(BranchExp node, VisitBag bag) {
		bag.setVisitChildren(false);
		bag.setVisitEnd(false);
		boolean zero = false;
		for (ISyntaxComponent exp : node.arrExpressions) {
			exp.visit(this);
			if (bZero) {
				zero = bZero;
			}
		}
		bZero = zero;
	}

	@Override
	public void visitEnd(TokenExp node) {

	}

	@Override
	public void visitEnd(RuleExp node) {

	}

	@Override
	public void visitEnd(SequenceExp node) {

	}

	@Override
	public void visitEnd(BranchExp node) {

	}
}
