package priv.bajdcc.syntax.exp;

import java.util.ArrayList;

import priv.bajdcc.syntax.ISyntaxComponent;
import priv.bajdcc.syntax.ISyntaxComponentVisitor;
import priv.bajdcc.utility.VisitBag;

/**
 * 文法规则（序列）
 *
 * @author bajdcc
 */
public class SequenceExp implements ISyntaxComponent, IExpCollction {

	/**
	 * 子表达式表
	 */
	public ArrayList<ISyntaxComponent> m_arrExpressions = new ArrayList<ISyntaxComponent>();

	@Override
	public void visit(ISyntaxComponentVisitor visitor) {
		VisitBag bag = new VisitBag();
		visitor.visitBegin(this, bag);
		if (bag.m_bVisitChildren) {
			for (ISyntaxComponent exp : m_arrExpressions){
				exp.visit(visitor);
			}
		}
		if (bag.m_bVisitEnd) {
			visitor.visitEnd(this);
		}
	}

	@Override
	public void add(ISyntaxComponent exp) {
		m_arrExpressions.add(exp);
	}
	
	@Override
	public boolean isEmpty() {
		return m_arrExpressions.isEmpty();
	}
}
