package com.bajdcc.LALR1.grammar.runtime;

import java.util.Set;

/**
 * 【运行时】用户态运行时状态查询
 *
 * @author bajdcc
 */
public interface IRuntimeRing3 {

	/**
	 * 是否是RING3程序
	 * @return 是否是RING3程序
	 */
	boolean isRing3();

	/**
	 * 运行用户态代码
	 * @param code 代码
	 * @return PID
	 * @throws Exception 系统异常
	 */
	int exec(String code) throws Exception;

	/**
	 * 运行用户态代码（文件名）
	 * @param filename 文件名
	 * @param code 代码
	 * @return PID
	 * @throws Exception 系统异常
	 */
	int exec_file(String filename, String code) throws Exception;

	/**
	 * 流输出
	 * @param text 内容
	 */
	void put(String text);

	/**
	 * 设置布尔值
	 * @param option 选项类型
	 * @param flag 值
	 */
	void setOptionsBool(RuntimeMachine.Ring3Option option, boolean flag);

	/**
	 * 查看设置
	 * @param option 选项类型
	 * @return 返回设置的值
	 */
	boolean isOptionsBool(RuntimeMachine.Ring3Option option);

	/**
	 * 添加用户句柄
	 * @param id 用户句柄
	 */
	void addHandle(int id);

	/**
	 * 删除用户句柄
	 * @param id 用户句柄
	 */
	void removeHandle(int id);

	/**
	 * 获取用户句柄列表
	 * @return 用户句柄列表
	 */
	Set<Integer> getHandles();
}
