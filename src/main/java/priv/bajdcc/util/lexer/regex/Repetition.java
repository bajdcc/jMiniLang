package priv.bajdcc.util.lexer.regex;

/**
 * 循环功能
 *
 * @author bajdcc
 */
public class Repetition implements IRegexComponent {
	/**
	 * 循环部件表达式
	 */
	public IRegexComponent component = null;

	/**
	 * 循环次数下限
	 */
	public int iLowerBound = 0;

	/**
	 * 循环次数上限
	 */
	public int iUpperBound = 0;

	public Repetition() {

	}

	public Repetition(IRegexComponent component, int begin, int end) {
		this.component = component;
		iLowerBound = begin;
		iUpperBound = end;
	}

	@Override
	public void visit(IRegexComponentVisitor visitor) {
		visitor.visitBegin(this);
		component.visit(visitor);
		visitor.visitEnd(this);
	}
}
