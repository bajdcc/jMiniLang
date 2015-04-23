package priv.bajdcc.OP.syntax;

import priv.bajdcc.OP.syntax.exp.BranchExp;
import priv.bajdcc.OP.syntax.exp.RuleExp;
import priv.bajdcc.OP.syntax.exp.SequenceExp;
import priv.bajdcc.OP.syntax.exp.TokenExp;
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
