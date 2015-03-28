package priv.bajdcc.syntax.exp;

import priv.bajdcc.syntax.ISyntaxComponent;
import priv.bajdcc.syntax.ISyntaxComponentVisitor;
import priv.bajdcc.syntax.Rule;
import priv.bajdcc.utility.VisitBag;

/**
 * 文法规则（非终结符）
 *
 * @author bajdcc
 */
public class RuleExp implements ISyntaxComponent {

	/**
	 * 非终结符ID
	 */
	public int m_iID = -1;

	/**
	 * 非终结符名称
	 */
	public String m_strName = null;

	/**
	 * 规则
	 */
	public Rule m_Rule = new Rule(this);

	public RuleExp(int id, String name) {
		m_iID = id;
		m_strName = name;
	}

	@Override
	public void visit(ISyntaxComponentVisitor visitor) {
		VisitBag bag = new VisitBag();
		visitor.visitBegin(this, bag);
		if (bag.m_bVisitEnd) {
			visitor.visitEnd(this);
		}
	}
}
