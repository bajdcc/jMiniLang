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

	void visitBegin(TokenExp node, VisitBag bag);

	void visitBegin(RuleExp node, VisitBag bag);

	void visitBegin(SequenceExp node, VisitBag bag);

	void visitBegin(BranchExp node, VisitBag bag);

	void visitEnd(TokenExp node);

	void visitEnd(RuleExp node);

	void visitEnd(SequenceExp node);

	void visitEnd(BranchExp node);

}
