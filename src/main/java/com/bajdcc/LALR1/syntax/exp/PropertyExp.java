package com.bajdcc.LALR1.syntax.exp;

import com.bajdcc.LALR1.semantic.token.ISemanticAction;
import com.bajdcc.LALR1.syntax.ISyntaxComponent;
import com.bajdcc.LALR1.syntax.ISyntaxComponentVisitor;
import com.bajdcc.LALR1.syntax.handler.IErrorHandler;
import com.bajdcc.util.VisitBag;

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
	public int iStorage;

	/**
	 * 子表达式
	 */
	public IErrorHandler errorHandler;

	/**
	 * 动作名称
	 */
	public ISemanticAction actionHandler = null;

	public PropertyExp(int id, IErrorHandler handler) {
		iStorage = id;
		errorHandler = handler;
	}

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
