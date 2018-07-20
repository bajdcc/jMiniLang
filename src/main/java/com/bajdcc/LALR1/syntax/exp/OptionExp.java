package com.bajdcc.LALR1.syntax.exp;

import com.bajdcc.LALR1.syntax.ISyntaxComponent;
import com.bajdcc.LALR1.syntax.ISyntaxComponentVisitor;
import com.bajdcc.util.VisitBag;

/**
 * 文法规则（可选）
 *
 * @author bajdcc
 */
public class OptionExp implements ISyntaxComponent {

	/**
	 * 子表达式
	 */
	public ISyntaxComponent expression = null;

	@Override
	public void visit(ISyntaxComponentVisitor visitor) {
		VisitBag bag = new VisitBag();
		visitor.visitBegin(this, bag);
		if (bag.getVisitChildren()) {
			expression.visit(visitor);
		}
		if (bag.getVisitEnd()) {
			visitor.visitEnd(this);
		}
	}
}
