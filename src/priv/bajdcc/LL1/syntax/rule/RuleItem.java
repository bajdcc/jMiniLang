package priv.bajdcc.LL1.syntax.rule;

import java.util.ArrayList;
import java.util.HashSet;

import priv.bajdcc.LL1.syntax.ISyntaxComponent;
import priv.bajdcc.LL1.syntax.exp.RuleExp;
import priv.bajdcc.LL1.syntax.exp.TokenExp;

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
	 * First集合（终结符）
	 */
	public HashSet<TokenExp> setFirstSetTokens = new HashSet<TokenExp>();

	/**
	 * First集合（终结符）
	 */
	public ArrayList<TokenExp> arrFirstSetTokens = null;

	/**
	 * First集合（非终结符）
	 */
	public HashSet<RuleExp> setFirstSetRules = new HashSet<RuleExp>();

	/**
	 * 父结点指针
	 */
	public Rule parent = null;

	/**
	 * 是否产生空串
	 */
	public boolean epsilon = false;

	public RuleItem(ISyntaxComponent exp, Rule parent) {
		expression = exp;
		this.parent = parent;
	}
}
