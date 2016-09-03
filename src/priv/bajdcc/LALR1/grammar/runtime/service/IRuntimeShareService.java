package priv.bajdcc.LALR1.grammar.runtime.service;

import priv.bajdcc.LALR1.grammar.runtime.RuntimeObject;

/**
 * 【运行时】运行时共享服务接口
 *
 * @author bajdcc
 */
public interface IRuntimeShareService {

	/**
	 * 创建共享
	 * @param name 共享名称
	 * @param obj 变量
	 * @return 操作状态
	 */
	int startSharing(String name, RuntimeObject obj);

	/**
	 * 查询共享
	 * @param name 变量名称
	 * @param reference 是否引用
	 * @return 共享变量
	 */
	RuntimeObject getSharing(String name, boolean reference);

	/**
	 * 停止共享
	 * @param name 变量名称
	 * @return 操作状态
	 */
	int stopSharing(String name);

	/**
	 * 是否锁定
	 * @param name 变量名称
	 * @return 是否锁定
	 */
	boolean isLocked(String name);

	/**
	 * 设置锁定
	 * @param name 变量名称
	 * @param lock 是否锁定
	 */
	void setLocked(String name, boolean lock);
}
