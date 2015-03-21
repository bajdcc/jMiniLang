package priv.bajdcc.lexer.regex;

/**
 * 正则表达式部件接口（父类）
 * 
 * @author bajdcc
 */
public interface IRegexComponent {
	/**
	 * 设定扩展自身结点的方式
	 * 
	 * @param visitor
	 *            递归遍历算法
	 */
	public void visit(IRegexComponentVisitor visitor);
}
