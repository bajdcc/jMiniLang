package priv.bajdcc.syntax.exp;

import priv.bajdcc.syntax.ISyntaxComponent;
import priv.bajdcc.syntax.ISyntaxComponentVisitor;
import priv.bajdcc.syntax.error.IErrorHandler;
import priv.bajdcc.utility.VisitBag;

/**
 * 文法规则（属性）
 *
 * @author bajdcc
 */
public class PropertyExp implements ISyntaxComponent {

	/**
	 * 子表达式
	 */
	public ISyntaxComponent m_Expression = null;
	
	/**
	 * 存储序号
	 */
	public int m_iStorage = -1;
	
	/**
	 * 子表达式
	 */
	public IErrorHandler m_ErrorHandler = null;

	public PropertyExp(int id, IErrorHandler handler) {
		m_iStorage = id;
		m_ErrorHandler = handler;
	}
	
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
