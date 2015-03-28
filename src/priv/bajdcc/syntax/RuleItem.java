package priv.bajdcc.syntax;

import java.util.ArrayList;

import priv.bajdcc.syntax.exp.RuleExp;
import priv.bajdcc.syntax.exp.TokenExp;

/**
 * 文法规则部件（文法推导式）
 *
 * @author bajdcc
 */
public class RuleItem {

	/**
	 * 规则表达式
	 */
	public ISyntaxComponent m_Expression = null;

	/**
	 * First集合（终结符）
	 */
	public ArrayList<TokenExp> m_arrFirstSetTokens = new ArrayList<TokenExp>();

	/**
	 * First集合（非终结符）
	 */
	public ArrayList<RuleExp> m_arrFirstSetRules = new ArrayList<RuleExp>();

	/**
	 * 父结点指针
	 */
	public Rule m_Parent = null;

	/**
	 * 是否有效
	 */
	public boolean m_bEnable = true;

	public RuleItem(ISyntaxComponent exp, Rule parent) {
		this.m_Expression = exp;
		this.m_Parent = parent;
	}
}
