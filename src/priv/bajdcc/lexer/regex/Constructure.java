package priv.bajdcc.lexer.regex;

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
	public boolean m_bBranch = false;

	/**
	 * 孩子结点
	 */
	public ArrayList<IRegexComponent> m_arrComponents = new ArrayList<IRegexComponent>();

	public Constructure(boolean branch) {
		m_bBranch = branch;
	}

	@Override
	public void visit(IRegexComponentVisitor visitor) {
		visitor.visitBegin(this);
		for (IRegexComponent component : m_arrComponents) {
			component.visit(visitor);
		}
		visitor.visitEnd(this);
	}
}
