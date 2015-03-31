package priv.bajdcc.syntax.exp;

import priv.bajdcc.syntax.ISyntaxComponent;

/**
 * 可以添加孩子结点的表达式接口
 *
 * @author bajdcc
 */
public interface IExpCollction {
	/**
	 * 添加孩子结点
	 * 
	 * @param exp
	 *            子表达式
	 */
	public void add(ISyntaxComponent exp);
	
	/**
	 * 集合是否为空
	 */
	public boolean isEmpty();
}
