package com.bajdcc.OP.syntax.solver;

import com.bajdcc.OP.syntax.ISyntaxComponentVisitor;
import com.bajdcc.OP.syntax.exp.BranchExp;
import com.bajdcc.OP.syntax.exp.RuleExp;
import com.bajdcc.OP.syntax.exp.SequenceExp;
import com.bajdcc.OP.syntax.exp.TokenExp;
import com.bajdcc.OP.syntax.token.PrecedenceType;
import com.bajdcc.util.VisitBag;

/**
 * 求解算符优先表
 *
 * @author bajdcc
 */
public abstract class OPTableSolver implements ISyntaxComponentVisitor {

	/**
	 * 当前符号在产生式中的位置
	 */
	private int index = -1;

	/**
	 * 上一个终结符
	 */
	private TokenExp lastToken = null;

	/**
	 * 上上个终结符
	 */
	private TokenExp lastlastToken = null;

	/**
	 * 上一个非终结符
	 */
	private RuleExp lastRule = null;

	public OPTableSolver() {

	}

	/**
	 * 填写算符优先分析表中的某一项
	 *
	 * @param x    行
	 * @param y    列
	 * @param type 类型（LT，GT，EQ）
	 */
	protected abstract void setCell(int x, int y, PrecedenceType type);

	@Override
	public void visitBegin(TokenExp node, VisitBag bag) {
		bag.bVisitChildren = false;
		if (index > 0) {
			if (lastToken != null) {
				// [b=node,a=lastToken]
				// A->...ab...直接得出a=b
				// lastToken=node
				setCell(lastToken.id, node.id, PrecedenceType.EQ);
			} else {
				// [b=node,a=token=LastVT(R),R=lastRule]
				// A->...Rb...且a属于LastVT(R)才能得出a>b
				// token>node
				for (TokenExp token : lastRule.rule.arrLastVT) {
					setCell(token.id, node.id, PrecedenceType.GT);
				}
				if (index > 1) {
					// [b=node,a=lastlastToken,R=lastRule]
					// A->...aRb...直接得出a=b
					// lastlastToken>node
					setCell(lastlastToken.id, node.id, PrecedenceType.EQ);
				}
			}
		}
	}

	@Override
	public void visitBegin(RuleExp node, VisitBag bag) {
		bag.bVisitChildren = false;
		if (index > 0) {
			// [a=lastToken,b=token=FirstVT(R),R=node]
			// A->...aR...且b属于FirstVT(R)才能得出a<b
			// lastToken<token
			for (TokenExp token : node.rule.arrFirstVT) {
				setCell(lastToken.id, token.id, PrecedenceType.LT);
			}
		}
	}

	@Override
	public void visitBegin(SequenceExp node, VisitBag bag) {
		bag.bVisitEnd = false;
		index = 0;
	}

	@Override
	public void visitBegin(BranchExp node, VisitBag bag) {
		bag.bVisitEnd = false;
	}

	@Override
	public void visitEnd(TokenExp node) {
		index++;
		lastlastToken = lastToken;
		lastToken = node;
		lastRule = null;
	}

	@Override
	public void visitEnd(RuleExp node) {
		index++;
		lastlastToken = lastToken;
		lastToken = null;
		lastRule = node;
	}

	@Override
	public void visitEnd(SequenceExp node) {

	}

	@Override
	public void visitEnd(BranchExp node) {

	}
}
