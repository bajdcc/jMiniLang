package com.bajdcc.LALR1.syntax.solver;

import com.bajdcc.LALR1.syntax.ISyntaxComponent;
import com.bajdcc.LALR1.syntax.ISyntaxComponentVisitor;
import com.bajdcc.LALR1.syntax.exp.*;
import com.bajdcc.LALR1.syntax.rule.RuleItem;
import com.bajdcc.util.VisitBag;

import java.util.HashSet;

/**
 * 求解一个产生式的First集合
 *
 * @author bajdcc
 */
public class FirstsetSolver implements ISyntaxComponentVisitor {

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
		if (bZero) {
			return false;
		}
		target.setFirstSetTokens = new HashSet<>(setTokens);
		target.setFirstSetRules = new HashSet<>(setRules);
		return true;
	}

	@Override
	public void visitBegin(TokenExp node, VisitBag bag) {
		bag.setVisitChildren(false);
		bag.setVisitEnd(false);
		setTokens.add(node);
		if (bZero) {
			bZero = false;
		}
	}

	@Override
	public void visitBegin(RuleExp node, VisitBag bag) {
		bag.setVisitChildren(false);
		bag.setVisitEnd(false);
		setRules.add(node);
		if (bZero) {
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
	public void visitBegin(OptionExp node, VisitBag bag) {
		bag.setVisitChildren(false);
		bag.setVisitEnd(false);
		node.expression.visit(this);
		bZero = true;
	}

	@Override
	public void visitBegin(PropertyExp node, VisitBag bag) {
		bag.setVisitChildren(false);
		bag.setVisitEnd(false);
		node.expression.visit(this);
		bZero = false;
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

	@Override
	public void visitEnd(OptionExp node) {

	}

	@Override
	public void visitEnd(PropertyExp node) {

	}
}
