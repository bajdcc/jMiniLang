package priv.bajdcc.syntax.rule;

import java.util.HashSet;

import priv.bajdcc.semantic.token.ISemanticHandler;
import priv.bajdcc.syntax.ISyntaxComponent;
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
	public ISyntaxComponent expression = null;
	
	/**
	 * 规则对应的语义分析接口
	 */
	public ISemanticHandler handler = null;

	/**
	 * First集合（终结符）
	 */
	public HashSet<TokenExp> setFirstSetTokens = new HashSet<TokenExp>();

	/**
	 * First集合（非终结符）
	 */
	public HashSet<RuleExp> setFirstSetRules = new HashSet<RuleExp>();

	/**
	 * 父结点指针
	 */
	public Rule parent = null;

	public RuleItem(ISyntaxComponent exp, Rule parent) {
		expression = exp;
		this.parent = parent;
	}
}
