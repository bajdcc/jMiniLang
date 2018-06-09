package com.bajdcc.OP.syntax.rule;

import com.bajdcc.OP.syntax.ISyntaxComponent;

/**
 * 文法规则部件（文法推导式）
 *
 * @author bajdcc
 */
public class RuleItem {

	/**
	 * 规则表达式
	 */
	public ISyntaxComponent expression = null;

	/**
	 * 父结点指针
	 */
	public Rule parent = null;

	public RuleItem(ISyntaxComponent exp, Rule parent) {
		expression = exp;
		this.parent = parent;
	}
}
