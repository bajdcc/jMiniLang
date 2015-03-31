package priv.bajdcc.syntax.solver;

import java.util.HashSet;
import priv.bajdcc.syntax.ISyntaxComponent;
import priv.bajdcc.syntax.ISyntaxComponentVisitor;
import priv.bajdcc.syntax.RuleItem;
import priv.bajdcc.syntax.exp.BranchExp;
import priv.bajdcc.syntax.exp.OptionExp;
import priv.bajdcc.syntax.exp.PropertyExp;
import priv.bajdcc.syntax.exp.RuleExp;
import priv.bajdcc.syntax.exp.SequenceExp;
import priv.bajdcc.syntax.exp.TokenExp;
import priv.bajdcc.utility.VisitBag;

/**
 * 求解一个产生式的First集合
 *
 * @author bajdcc
 */
public class FirstsetSolver implements ISyntaxComponentVisitor {

	/**
	 * 终结符表
	 */
	private HashSet<TokenExp> m_setTokens = new HashSet<TokenExp>();

	/**
	 * 非终结符表
	 */
	private HashSet<RuleExp> m_setRules = new HashSet<RuleExp>();

	/**
	 * 产生式推导的串长度是否可能为零
	 */
	private boolean m_bZero = true;

	/**
	 * 求解
	 * 
	 * @param target
	 *            目标产生式对象
	 * @return 产生式是否合法
	 */
	public boolean solve(RuleItem target) {
		if (m_bZero) {
			return false;
		}
		target.m_arrFirstSetTokens = new HashSet<TokenExp>(m_setTokens);
		target.m_arrFirstSetRules = new HashSet<RuleExp>(m_setRules);
		return true;
	}

	@Override
	public void visitBegin(TokenExp node, VisitBag bag) {
		bag.m_bVisitChildren = false;
		bag.m_bVisitEnd = false;
		m_setTokens.add(node);
		if (m_bZero) {
			m_bZero = false;
		}
	}

	@Override
	public void visitBegin(RuleExp node, VisitBag bag) {
		bag.m_bVisitChildren = false;
		bag.m_bVisitEnd = false;
		m_setRules.add(node);
		if (m_bZero) {
			m_bZero = false;
		}
	}

	@Override
	public void visitBegin(SequenceExp node, VisitBag bag) {
		bag.m_bVisitChildren = false;
		bag.m_bVisitEnd = false;
		boolean zero = false;
		for (ISyntaxComponent exp : node.m_arrExpressions) {
			exp.visit(this);
			zero = m_bZero;
			if (!zero) {
				break;
			}
		}
		m_bZero = zero;
	}

	@Override
	public void visitBegin(BranchExp node, VisitBag bag) {
		bag.m_bVisitChildren = false;
		bag.m_bVisitEnd = false;
		boolean zero = false;
		for (ISyntaxComponent exp : node.m_arrExpressions) {
			exp.visit(this);
			if (m_bZero) {
				zero = m_bZero;
			}
		}
		m_bZero = zero;
	}

	@Override
	public void visitBegin(OptionExp node, VisitBag bag) {
		bag.m_bVisitChildren = false;
		bag.m_bVisitEnd = false;
		node.m_Expression.visit(this);
		m_bZero = true;
	}

	@Override
	public void visitBegin(PropertyExp node, VisitBag bag) {
		bag.m_bVisitChildren = false;
		bag.m_bVisitEnd = false;
		node.m_Expression.visit(this);
		m_bZero = false;
	}

	@Override
	public void visitEnd(TokenExp node) {

	}

	@Override
	public void visitEnd(RuleExp node) {

	}

	@Override
	public void visitEnd(SequenceExp node) {

	}

	@Override
	public void visitEnd(BranchExp node) {

	}

	@Override
	public void visitEnd(OptionExp node) {

	}

	@Override
	public void visitEnd(PropertyExp node) {

	}
}
