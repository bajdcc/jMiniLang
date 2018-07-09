package com.bajdcc.LALR1.grammar.runtime.service;

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray;

/**
 * 【运行时】运行时用户服务接口
 *
 * @author bajdcc
 */
public interface IRuntimeUserService {

	/**
	 * 创建用户服务句柄
	 *
	 * @param name 用户服务名称
	 * @param page 创建时的页面
	 * @return 用户服务句柄
	 */
	int create(String name, String page);

	/**
	 * 读取用户服务
	 * @param id 管道句柄
	 * @return 读取的对象
	 */
	RuntimeObject read(int id);

	/**
	 * 写入用户服务
	 * @param id 管道句柄
	 * @param obj 写入的对象
	 * @return 是否成功
	 */
	boolean write(int id, RuntimeObject obj);

	/**
	 * 销毁用户服务
	 */
	void destroy();

	/**
	 * 销毁用户服务句柄
	 * @param id 用户服务句柄
	 * @return 是否成功
	 */
	boolean destroy(int id);

	/**
	 * 获取列表
	 *
	 * @param api 是否API调用
	 * @return 列表
	 */
	RuntimeArray stat(boolean api);
}
