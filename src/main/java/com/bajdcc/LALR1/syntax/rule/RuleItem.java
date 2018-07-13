package com.bajdcc.LALR1.syntax.rule;

import com.bajdcc.LALR1.semantic.token.ISemanticAnalyzer;
import com.bajdcc.LALR1.syntax.ISyntaxComponent;
import com.bajdcc.LALR1.syntax.exp.RuleExp;
import com.bajdcc.LALR1.syntax.exp.TokenExp;

import java.util.HashSet;

/**
 * 文法规则部件（文法推导式）
 *
 * @author bajdcc
 */
public class RuleItem {

	/**
	 * 规则表达式
	 */
	public ISyntaxComponent expression;

	/**
	 * 规则对应的语义分析接口
	 */
	public ISemanticAnalyzer handler = null;

	/**
	 * First集合（终结符）
	 */
	public HashSet<TokenExp> setFirstSetTokens = new HashSet<>();

	/**
	 * First集合（非终结符）
	 */
	public HashSet<RuleExp> setFirstSetRules = new HashSet<>();

	/**
	 * 父结点指针
	 */
	public Rule parent;

	public RuleItem(ISyntaxComponent exp, Rule parent) {
		expression = exp;
		this.parent = parent;
	}
}
