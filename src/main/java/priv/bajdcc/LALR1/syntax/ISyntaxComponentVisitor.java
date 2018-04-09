package priv.bajdcc.LALR1.syntax;

import priv.bajdcc.LALR1.syntax.exp.*;
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

	void visitBegin(OptionExp node, VisitBag bag);

	void visitBegin(PropertyExp node, VisitBag bag);

	void visitEnd(TokenExp node);

	void visitEnd(RuleExp node);

	void visitEnd(SequenceExp node);

	void visitEnd(BranchExp node);

	void visitEnd(OptionExp node);

	void visitEnd(PropertyExp node);
}
