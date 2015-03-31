package priv.bajdcc.syntax;

import priv.bajdcc.syntax.exp.BranchExp;
import priv.bajdcc.syntax.exp.OptionExp;
import priv.bajdcc.syntax.exp.PropertyExp;
import priv.bajdcc.syntax.exp.RuleExp;
import priv.bajdcc.syntax.exp.SequenceExp;
import priv.bajdcc.syntax.exp.TokenExp;
import priv.bajdcc.utility.VisitBag;

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
