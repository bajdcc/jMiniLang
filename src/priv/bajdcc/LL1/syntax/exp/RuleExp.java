package priv.bajdcc.LL1.syntax.exp;

import priv.bajdcc.LL1.syntax.ISyntaxComponent;
import priv.bajdcc.LL1.syntax.ISyntaxComponentVisitor;
import priv.bajdcc.LL1.syntax.rule.Rule;
import priv.bajdcc.util.VisitBag;

/**
 * 文法规则（非终结符）
 *
 * @author bajdcc
 */
public class RuleExp implements ISyntaxComponent {

	/**
	 * 非终结符ID
	 */
	public int id = -1;

	/**
	 * 非终结符名称
	 */
	public String name = null;

	/**
	 * 规则
	 */
	public Rule rule = new Rule(this);

	public RuleExp(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public void visit(ISyntaxComponentVisitor visitor) {
		VisitBag bag = new VisitBag();
		visitor.visitBegin(this, bag);
		if (bag.bVisitEnd) {
			visitor.visitEnd(this);
		}
	}
	
	@Override
	public String toString() {
		return id + "： " + name;
	}
}
