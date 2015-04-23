package priv.bajdcc.OP.syntax.exp;

import java.util.ArrayList;

import priv.bajdcc.OP.syntax.ISyntaxComponent;
import priv.bajdcc.OP.syntax.ISyntaxComponentVisitor;
import priv.bajdcc.util.VisitBag;

/**
 * 文法规则（分支）
 *
 * @author bajdcc
 */
public class BranchExp implements ISyntaxComponent, IExpCollction {

	/**
	 * 子表达式表
	 */
	public ArrayList<ISyntaxComponent> arrExpressions = new ArrayList<ISyntaxComponent>();

	@Override
	public void visit(ISyntaxComponentVisitor visitor) {
		VisitBag bag = new VisitBag();
		visitor.visitBegin(this, bag);
		if (bag.bVisitChildren) {
			for (ISyntaxComponent exp : arrExpressions) {
				exp.visit(visitor);
			}
		}
		if (bag.bVisitEnd) {
			visitor.visitEnd(this);
		}
	}

	@Override
	public void visitReverse(ISyntaxComponentVisitor visitor) {
		VisitBag bag = new VisitBag();
		visitor.visitBegin(this, bag);
		if (bag.bVisitChildren) {
			for (ISyntaxComponent exp : arrExpressions) {
				exp.visitReverse(visitor);
			}
		}
		if (bag.bVisitEnd) {
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
