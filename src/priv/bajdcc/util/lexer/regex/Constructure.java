package priv.bajdcc.util.lexer.regex;

import java.util.ArrayList;

/**
 * 顺序结构或分支结构
 * 
 * @author bajdcc
 */
public class Constructure implements IRegexComponent {
	/**
	 * 若是则为分支，否则为顺序
	 */
	public boolean bBranch = false;

	/**
	 * 孩子结点
	 */
	public ArrayList<IRegexComponent> arrComponents = new ArrayList<IRegexComponent>();

	public Constructure(boolean branch) {
		bBranch = branch;
	}

	@Override
	public void visit(IRegexComponentVisitor visitor) {
		visitor.visitBegin(this);
		for (IRegexComponent component : arrComponents) {
			component.visit(visitor);
		}
		visitor.visitEnd(this);
	}
}
