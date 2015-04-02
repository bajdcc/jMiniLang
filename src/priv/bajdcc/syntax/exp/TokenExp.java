package priv.bajdcc.syntax.exp;

import priv.bajdcc.lexer.token.TokenType;
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
	public TokenType m_kType = null;

	/**
	 * 终结符对应的正则表达式解析组件（用于语义分析中的单词流解析）
	 */
	public Object m_Object = null;

	public TokenExp(int id, String name, TokenType type, Object obj) {
		m_iID = id;
		m_strName = name;
		m_kType = type;
		m_Object = obj;
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
		return String.format("%d: `%s`,%s,%s", m_iID, m_strName,
				m_kType.getName(),
				m_Object == null ? "(null)" : m_Object.toString());
	}
}
