package priv.bajdcc.LL1.syntax.exp;

import priv.bajdcc.LL1.syntax.ISyntaxComponent;

/**
 * 可以添加孩子结点的表达式接口
 *
 * @author bajdcc
 */
public interface IExpCollction {
	/**
	 * 添加孩子结点
	 *
	 * @param exp 子表达式
	 */
	void add(ISyntaxComponent exp);

	/**
	 * 集合是否为空
	 *
	 * @return 集合是否为空
	 */
	boolean isEmpty();
}
