package priv.bajdcc.LALR1.syntax;

import priv.bajdcc.LALR1.syntax.exp.BranchExp;
import priv.bajdcc.LALR1.syntax.exp.OptionExp;
import priv.bajdcc.LALR1.syntax.exp.PropertyExp;
import priv.bajdcc.LALR1.syntax.exp.RuleExp;
import priv.bajdcc.LALR1.syntax.exp.SequenceExp;
import priv.bajdcc.LALR1.syntax.exp.TokenExp;
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
	
	public void visitBegin(OptionExp node, VisitBag bag);
	
	public void visitBegin(PropertyExp node, VisitBag bag);
	
	public void visitEnd(TokenExp node);
	
	public void visitEnd(RuleExp node);
	
	public void visitEnd(SequenceExp node);
	
	public void visitEnd(BranchExp node);
	
	public void visitEnd(OptionExp node);
	
	public void visitEnd(PropertyExp node);
}
