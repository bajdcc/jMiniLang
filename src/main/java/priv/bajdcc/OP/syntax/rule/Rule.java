package priv.bajdcc.OP.syntax.rule;

import priv.bajdcc.OP.syntax.exp.RuleExp;
import priv.bajdcc.OP.syntax.exp.TokenExp;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * 文法规则（文法推导式表）
 *
 * @author bajdcc
 */
public class Rule {

	/**
	 * 规则表达式列表
	 */
	public ArrayList<RuleItem> arrRules = new ArrayList<>();

	/**
	 * 规则起始非终结符
	 */
	public RuleExp nonTerminal = null;

	public ArrayList<TokenExp> arrFirstVT = new ArrayList<>();

	public ArrayList<TokenExp> arrLastVT = new ArrayList<>();

	public HashSet<TokenExp> setFirstVT = new HashSet<>();

	public HashSet<TokenExp> setLastVT = new HashSet<>();

	public Rule(RuleExp exp) {
		nonTerminal = exp;
	}
}
