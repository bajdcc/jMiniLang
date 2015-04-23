package priv.bajdcc.OP.syntax.rule;

import java.util.ArrayList;
import java.util.HashSet;

import priv.bajdcc.OP.syntax.exp.RuleExp;
import priv.bajdcc.OP.syntax.exp.TokenExp;

/**
 * 文法规则（文法推导式表）
 *
 * @author bajdcc
 */
public class Rule {

	/**
	 * 规则表达式列表
	 */
	public ArrayList<RuleItem> arrRules = new ArrayList<RuleItem>();

	/**
	 * 规则起始非终结符
	 */
	public RuleExp nonTerminal = null;

	public ArrayList<TokenExp> arrFirstVT = new ArrayList<TokenExp>();
	
	public ArrayList<TokenExp> arrLastVT = new ArrayList<TokenExp>();
	
	public HashSet<TokenExp> setFirstVT = new HashSet<TokenExp>();
	
	public HashSet<TokenExp> setLastVT = new HashSet<TokenExp>();

	public Rule(RuleExp exp) {
		nonTerminal = exp;
	}
}
