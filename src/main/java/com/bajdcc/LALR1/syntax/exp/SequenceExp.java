package com.bajdcc.LALR1.syntax.exp;

import com.bajdcc.LALR1.syntax.ISyntaxComponent;
import com.bajdcc.LALR1.syntax.ISyntaxComponentVisitor;
import com.bajdcc.util.VisitBag;

import java.util.ArrayList;
import java.util.List;

/**
 * 文法规则（序列）
 *
 * @author bajdcc
 */
public class SequenceExp implements ISyntaxComponent, IExpCollction {

	/**
	 * 子表达式表
	 */
	public List<ISyntaxComponent> arrExpressions = new ArrayList<>();

	@Override
	public void visit(ISyntaxComponentVisitor visitor) {
		VisitBag bag = new VisitBag();
		visitor.visitBegin(this, bag);
		if (bag.getVisitChildren()) {
			for (ISyntaxComponent exp : arrExpressions) {
				exp.visit(visitor);
			}
		}
		if (bag.getVisitEnd()) {
			visitor.visitEnd(this);
		}
	}

	@Override
	public void add(ISyntaxComponent exp) {
		arrExpressions.add(exp);
	}

	@Override
	public boolean isEmpty() {
		return arrExpressions.isEmpty();
	}
}
