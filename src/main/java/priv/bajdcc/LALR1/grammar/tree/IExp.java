package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;

/**
 * 【语义类型】基本表达式接口
 *
 * @author bajdcc
 */
public interface IExp extends ICommon {

	/**
	 * 是否是左值（即不可修改的常量）
	 *
	 * @return 是否为常量
	 */
	boolean isConstant();

	/**
	 * 是否可枚举
	 *
	 * @return 是否可枚举
	 */
	boolean isEnumerable();

	/**
	 * 表达式化简
	 *
	 * @param recorder 错误记录
	 * @return 简化后的表达式
	 */
	IExp simplify(ISemanticRecorder recorder);

	/**
	 * 设置Yield
	 */
	void setYield();
}
