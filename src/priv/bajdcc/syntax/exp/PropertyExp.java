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
	public ISyntaxComponent expression = null;
	
	/**
	 * 存储序号
	 */
	public int iStorage = -1;
	
	/**
	 * 子表达式
	 */
	public IErrorHandler errorHandler = null;

	public PropertyExp(int id, IErrorHandler handler) {
		iStorage = id;
		errorHandler = handler;
	}
	
	@Override
	public void visit(ISyntaxComponentVisitor visitor) {
		VisitBag bag = new VisitBag();
		visitor.visitBegin(this, bag);
		if (bag.bVisitChildren) {
			expression.visit(visitor);
		}
		if (bag.bVisitEnd) {
			visitor.visitEnd(this);
		}
	}
}
