package priv.bajdcc.LL1.syntax.stringify;

import java.util.ArrayList;
import java.util.Stack;

import priv.bajdcc.LL1.syntax.ISyntaxComponent;
import priv.bajdcc.LL1.syntax.ISyntaxComponentVisitor;
import priv.bajdcc.LL1.syntax.exp.BranchExp;
import priv.bajdcc.LL1.syntax.exp.RuleExp;
import priv.bajdcc.LL1.syntax.exp.SequenceExp;
import priv.bajdcc.LL1.syntax.exp.TokenExp;
import priv.bajdcc.util.VisitBag;

public class SyntaxToString implements ISyntaxComponentVisitor {

	/**
	 * 文法推导式描述
	 */
	private StringBuilder context = new StringBuilder();

	/**
	 * 存放结果的栈
	 */
	private Stack<ArrayList<String>> stkStringList = new Stack<>();

	/**
	 * 当前描述表
	 */
	private ArrayList<String> arrData = new ArrayList<>();

	/**
	 * 焦点
	 */
	private ISyntaxComponent focusedExp = null;

	/**
	 * LR项目符号'*'，是否移进当前符号
	 */
	private boolean bFront = true;

	public SyntaxToString() {

	}

	public SyntaxToString(ISyntaxComponent exp, boolean front) {
		focusedExp = exp;
		bFront = front;
	}

	/**
	 * 开始遍历子结点
	 */
	private void beginChilren() {
		arrData = null;
		stkStringList.push(new ArrayList<>());
	}

	/**
	 * 结束遍历子结点
	 */
	private void endChilren() {
		arrData = stkStringList.pop();
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
		if (focusedExp == exp) {
			/* 添加LR项目集符号 */
			if (bFront) {
				string = "*" + string;
			} else {
				string += " *";
			}
		}
		if (stkStringList.isEmpty()) {
			context.append(string);
		} else {
			stkStringList.peek().add(string);
		}
	}

	@Override
	public void visitBegin(TokenExp node, VisitBag bag) {
		bag.bVisitEnd = false;
		store(node, " `" + node.name + "` ");
	}

	@Override
	public void visitBegin(RuleExp node, VisitBag bag) {
		bag.bVisitEnd = false;
		store(node, " " + node.name + " ");
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
	public void visitEnd(TokenExp node) {

	}

	@Override
	public void visitEnd(RuleExp node) {

	}

	@Override
	public void visitEnd(SequenceExp node) {
		endChilren();
		StringBuilder sb = new StringBuilder();
		arrData.forEach(sb::append);
		store(node, sb.toString());
	}

	@Override
	public void visitEnd(BranchExp node) {
		endChilren();
		StringBuilder sb = new StringBuilder();
		sb.append(" (");
		for (String string : arrData) {
			sb.append(string);
			sb.append('|');
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(") ");
		store(node, sb.toString());
	}

	@Override
	public String toString() {
		return context.toString();
	}
}
