package priv.bajdcc.syntax.stringify;

import java.util.ArrayList;
import java.util.Stack;

import priv.bajdcc.syntax.ISyntaxComponent;
import priv.bajdcc.syntax.ISyntaxComponentVisitor;
import priv.bajdcc.syntax.exp.BranchExp;
import priv.bajdcc.syntax.exp.OptionExp;
import priv.bajdcc.syntax.exp.PropertyExp;
import priv.bajdcc.syntax.exp.RuleExp;
import priv.bajdcc.syntax.exp.SequenceExp;
import priv.bajdcc.syntax.exp.TokenExp;
import priv.bajdcc.utility.VisitBag;

public class SyntaxToString implements ISyntaxComponentVisitor {

	/**
	 * 文法推导式描述
	 */
	private StringBuilder m_Context = new StringBuilder();

	/**
	 * 存放结果的栈
	 */
	private Stack<ArrayList<String>> m_stkStringList = new Stack<ArrayList<String>>();

	/**
	 * 当前描述表
	 */
	private ArrayList<String> m_arrData = new ArrayList<String>();

	/**
	 * 焦点
	 */
	private ISyntaxComponent m_FocusedExp = null;

	/**
	 * LR项目符号'*'，是否移进当前符号
	 */
	private boolean m_bFront = true;

	public SyntaxToString() {

	}

	public SyntaxToString(ISyntaxComponent exp, boolean front) {
		m_FocusedExp = exp;
		m_bFront = front;
	}

	/**
	 * 开始遍历子结点
	 */
	private void beginChilren() {
		m_arrData = null;
		m_stkStringList.push(new ArrayList<String>());
	}

	/**
	 * 结束遍历子结点
	 */
	private void endChilren() {
		m_arrData = m_stkStringList.pop();
	}

	/**
	 * 保存结果
	 * 
	 * @param exp
	 *            当前表达式结点
	 * @param string
	 *            描述
	 */
	private void store(ISyntaxComponent exp, String string) {
		if (m_FocusedExp == exp) {
			/* 添加LR项目集符号 */
			if (m_bFront) {
				string = "*" + string;
			} else {
				string += " *";
			}
		}
		if (m_stkStringList.isEmpty()) {
			m_Context.append(string);
		} else {
			m_stkStringList.peek().add(string);
		}
	}

	@Override
	public void visitBegin(TokenExp node, VisitBag bag) {
		bag.m_bVisitEnd = false;
		store(node, " `" + node.m_strName + "`");
	}

	@Override
	public void visitBegin(RuleExp node, VisitBag bag) {
		bag.m_bVisitEnd = false;
		store(node, " " + node.m_strName);
	}

	@Override
	public void visitBegin(SequenceExp node, VisitBag bag) {
		beginChilren();
	}

	@Override
	public void visitBegin(BranchExp node, VisitBag bag) {
		beginChilren();
	}

	@Override
	public void visitBegin(OptionExp node, VisitBag bag) {
		beginChilren();
	}

	@Override
	public void visitBegin(PropertyExp node, VisitBag bag) {
		beginChilren();
	}

	@Override
	public void visitEnd(TokenExp node) {

	}

	@Override
	public void visitEnd(RuleExp node) {

	}

	@Override
	public void visitEnd(SequenceExp node) {
		endChilren();
		StringBuffer sb = new StringBuffer();
		for (String string : m_arrData) {
			sb.append(string);
		}
		store(node, sb.toString());
	}

	@Override
	public void visitEnd(BranchExp node) {
		endChilren();
		StringBuffer sb = new StringBuffer();
		sb.append(" (");
		for (String string : m_arrData) {
			sb.append(string);
			sb.append('|');
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(") ");
		store(node, sb.toString());
	}

	@Override
	public void visitEnd(OptionExp node) {
		endChilren();
		store(node, " [" + m_arrData.get(0) + "] ");
	}

	@Override
	public void visitEnd(PropertyExp node) {
		endChilren();
		store(node, m_arrData.get(0)
				+ (node.m_iStorage == -1 ? "" : "[" + node.m_iStorage + "] "));
	}

	@Override
	public String toString() {
		return m_Context.toString();
	}
}
