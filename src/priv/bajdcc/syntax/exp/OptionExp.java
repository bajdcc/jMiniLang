package priv.bajdcc.syntax.exp;

import priv.bajdcc.syntax.ISyntaxComponent;
import priv.bajdcc.syntax.ISyntaxComponentVisitor;
import priv.bajdcc.utility.VisitBag;

/**
 * 文法规则（可选）
 *
 * @author bajdcc
 */
public class OptionExp implements ISyntaxComponent {

	/**
	 * 子表达式
	 */
	public ISyntaxComponent m_Expression = null;

	@Override
	public void visit(ISyntaxComponentVisitor visitor) {
		VisitBag bag = new VisitBag();
		visitor.visitBegin(this, bag);
		if (bag.m_bVisitChildren) {
			m_Expression.visit(visitor);
		}
		if (bag.m_bVisitEnd) {
			visitor.visitEnd(this);
		}
	}
}
