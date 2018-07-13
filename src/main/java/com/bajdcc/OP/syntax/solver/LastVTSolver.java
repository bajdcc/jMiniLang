package com.bajdcc.OP.syntax.solver;

import com.bajdcc.OP.syntax.ISyntaxComponentVisitor;
import com.bajdcc.OP.syntax.exp.BranchExp;
import com.bajdcc.OP.syntax.exp.RuleExp;
import com.bajdcc.OP.syntax.exp.SequenceExp;
import com.bajdcc.OP.syntax.exp.TokenExp;
import com.bajdcc.util.VisitBag;

/**
 * 求解非终结符的LastVT集合
 *
 * @author bajdcc
 */
public class LastVTSolver implements ISyntaxComponentVisitor {

	/**
	 * 是否更新，作为算法收敛的依据
	 */
	private boolean update = false;

	/**
	 * 当前规则中止
	 */
	private boolean stop = false;

	/**
	 * 产生式左部
	 */
	private RuleExp origin;

	public LastVTSolver(RuleExp exp) {
		origin = exp;
	}

	/**
	 * 是否更改
	 *
	 * @return 经过此次计算，集合是否更新
	 */
	public boolean isUpdated() {
		return update;
	}

	@Override
	public void visitBegin(TokenExp node, VisitBag bag) {
		bag.bVisitChildren = false;
		bag.bVisitEnd = false;
		if (!stop) {
			update = origin.rule.setLastVT.add(node);// 直接加入终结符，并中止遍历
			stop = true;
		}
	}

	@Override
	public void visitBegin(RuleExp node, VisitBag bag) {
		bag.bVisitChildren = false;
		bag.bVisitEnd = false;
		if (!stop) {
			update = origin.rule.setLastVT.addAll(node.rule.setLastVT);// 加入当前非终结符的LastVT
		}
	}

	@Override
	public void visitBegin(SequenceExp node, VisitBag bag) {
		bag.bVisitEnd = false;
		stop = false;
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

	}

	@Override
	public void visitEnd(BranchExp node) {

	}
}
