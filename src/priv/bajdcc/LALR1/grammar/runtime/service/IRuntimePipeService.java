package priv.bajdcc.LALR1.grammar.runtime.service;

import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeArray;

/**
 * 【运行时】运行时管道服务接口
 *
 * @author bajdcc
 */
public interface IRuntimePipeService {

	/**
	 * 创建管道
	 * @param name 管道名称
	 * @return 管道句柄
	 */
	int create(String name);

	/**
	 * 销毁管道
	 * @param handle 管道句柄
	 * @return 是否成功
	 */
	boolean destroy(int handle);

	/**
	 * 销毁管道
	 *
	 * @param pid 进程ID
	 * @param name 管道名称
	 * @return 是否成功
	 */
	boolean destroyByName(int pid, String name);

	/**
	 * 管道读
	 *
	 * @param pid 进程ID
	 * @param handle 管道句柄
	 * @return 读取的字符
	 */
	char read(int pid, int handle);

	/**
	 * 管道写
	 *
	 * @param handle 管道句柄
	 * @param ch 字符
	 * @return 是否成功
	 */
	boolean write(int handle, char ch);

	/**
	 * 管道是否为空
	 * @param handle 管道句柄
	 * @return 是否为空
	 */
	boolean isEmpty(int handle);

	/**
	 * 查询管道是否存在
	 * @param name 管理名
	 * @return 是否存在
	 */
	boolean query(String name);

	/**
	 * 获取管道数量
	 *
	 * @return 管道数量
	 */
	long size();

	/**
	 * 获取列表
	 *
	 * @return 列表
	 */
	RuntimeArray stat();
}
