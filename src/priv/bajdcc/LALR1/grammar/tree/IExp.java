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
	 */
	public boolean isConstant();
	
	/**
	 * 是否可枚举
	 */
	public boolean isEnumerable();

	/**
	 * 表达式化简
	 * 
	 * @param recorder
	 *            错误记录
	 */
	public IExp simplify(ISemanticRecorder recorder);
	
	/**
	 * 设置YIELD
	 */
	public void setYield();
}
