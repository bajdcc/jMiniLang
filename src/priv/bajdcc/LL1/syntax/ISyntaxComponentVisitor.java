package priv.bajdcc.LL1.syntax;

import priv.bajdcc.LL1.syntax.exp.BranchExp;
import priv.bajdcc.LL1.syntax.exp.RuleExp;
import priv.bajdcc.LL1.syntax.exp.SequenceExp;
import priv.bajdcc.LL1.syntax.exp.TokenExp;
import priv.bajdcc.util.VisitBag;

/**
 * 基于文法组件的访问接口（Visitor模式）
 * 
 * @author bajdcc
 */
public interface ISyntaxComponentVisitor {

	public void visitBegin(TokenExp node, VisitBag bag);

	public void visitBegin(RuleExp node, VisitBag bag);

	public void visitBegin(SequenceExp node, VisitBag bag);

	public void visitBegin(BranchExp node, VisitBag bag);

	public void visitEnd(TokenExp node);

	public void visitEnd(RuleExp node);

	public void visitEnd(SequenceExp node);

	public void visitEnd(BranchExp node);

}
