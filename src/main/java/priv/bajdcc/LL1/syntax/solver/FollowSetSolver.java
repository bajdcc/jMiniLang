package priv.bajdcc.LL1.syntax.solver;

import priv.bajdcc.LL1.syntax.ISyntaxComponentVisitor;
import priv.bajdcc.LL1.syntax.exp.BranchExp;
import priv.bajdcc.LL1.syntax.exp.RuleExp;
import priv.bajdcc.LL1.syntax.exp.SequenceExp;
import priv.bajdcc.LL1.syntax.exp.TokenExp;
import priv.bajdcc.util.VisitBag;

/**
 * 求解一个产生式的Follow集合
 *
 * @author bajdcc
 */
public class FollowSetSolver implements ISyntaxComponentVisitor {

	/**
	 * 当前求解的非终结符
	 */
	private RuleExp target = null;

	/**
	 * 当前的产生式左部
	 */
	private RuleExp origin = null;

	/**
	 * Follow集是否更新，作为算法收敛的依据
	 */
	private boolean update = false;

	/**
	 * 上一次是否刚遍历过当前求解的非终结符
	 */
	private boolean enable = false;

	public FollowSetSolver(RuleExp origin, RuleExp target) {
		this.origin = origin;
		this.target = target;
	}

	/**
	 * Follow集是否更改
	 *
	 * @return 经过此次计算，Follow集是否更新
	 */
	public boolean isUpdated() {
		return update;
	}

	/**
	 * 添加非终结符Follow集
	 * <p>
	 * A=origin，B=target
	 * </p>
	 * <p>
	 * 添加Follow集（A->...B或A->...B(...->epsilon)）
	 * </p>
	 * <p>
	 * Follow(B) = Follow(A) U Follow(B)
	 * </p>
	 */
	private void addVnFollowToFollow() {
		setUpdate(target.rule.setFollows.addAll(origin.rule.setFollows));
	}

	/**
	 * 添加非终结符First集
	 * <p>
	 * A=origin，B=target，beta=exp=Vn
	 * </p>
	 * <p>
	 * 添加First集（A->...B beta）
	 * </p>
	 * <p>
	 * Follow(B) = Follow(A) U First(beta)
	 * </p>
	 *
	 * @param exp 非终结符
	 */
	private void addVnFirstToFollow(RuleExp exp) {
		setUpdate(target.rule.setFollows.addAll(exp.rule.arrFirsts));
	}

	/**
	 * <p>
	 * A=origin，B=target，beta=exp=Vt
	 * </p>
	 * <p>
	 * 添加终结符（A->...B beta）
	 * </p>
	 * <p>
	 * Follow(B) = Follow(A) U beta
	 * </p>
	 *
	 * @param exp 终结符
	 */
	private void addVtFirstToFollow(TokenExp exp) {
		setUpdate(target.rule.setFollows.add(exp));
	}

	/**
	 * 设置更新状态
	 *
	 * @param update 更新状态
	 */
	private void setUpdate(boolean update) {
		if (update) {
			this.update = true;
		}
	}

	@Override
	public void visitBegin(TokenExp node, VisitBag bag) {
		bag.bVisitChildren = false;
		bag.bVisitEnd = false;
		if (enable) {// 上次经过非终结符
			addVtFirstToFollow(node);
			enable = false;
		}
	}

	@Override
	public void visitBegin(RuleExp node, VisitBag bag) {
		bag.bVisitChildren = false;
		bag.bVisitEnd = false;
		if (enable) {// 上次经过非终结符
			addVnFirstToFollow(node);
			if (!node.rule.epsilon) {
				enable = false;
			}
		}
		if (node.rule == target.rule) {// 设置为经过非终结符
			enable = true;
		}
	}

	@Override
	public void visitBegin(SequenceExp node, VisitBag bag) {

	}

	@Override
	public void visitBegin(BranchExp node, VisitBag bag) {
		bag.bVisitEnd = false;
	}

	@Override
	public void visitEnd(TokenExp node) {

	}

	@Override
	public void visitEnd(RuleExp node) {

	}

	@Override
	public void visitEnd(SequenceExp node) {
		if (enable) {// 上次经过非终结符
			addVnFollowToFollow();
		}
		enable = false;
	}

	@Override
	public void visitEnd(BranchExp node) {

	}
}
