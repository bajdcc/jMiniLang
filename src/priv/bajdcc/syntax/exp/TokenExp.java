package priv.bajdcc.syntax.exp;

import priv.bajdcc.syntax.ISyntaxComponent;
import priv.bajdcc.syntax.ISyntaxComponentVisitor;
import priv.bajdcc.utility.VisitBag;

/**
 * 文法规则（终结符）
 *
 * @author bajdcc
 */
public class TokenExp implements ISyntaxComponent {

	/**
	 * 终结符ID
	 */
	public int m_iID = -1;

	/**
	 * 终结符名称
	 */
	public String m_strName = null;

	/**
	 * 终结符对应的正则表达式
	 */
	public String m_strRegex = null;

	public TokenExp(int id, String name, String regex) {
		m_iID = id;
		m_strName = name;
		m_strRegex = regex;
	}

	@Override
	public void visit(ISyntaxComponentVisitor visitor) {
		VisitBag bag = new VisitBag();
		visitor.visitBegin(this, bag);
		if (bag.m_bVisitEnd) {
			visitor.visitEnd(this);
		}
	}

	@Override
	public String toString() {
		return m_iID + ": `" + m_strName + "`,'" + m_strRegex + "'";
	}
}
