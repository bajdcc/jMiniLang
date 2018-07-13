package com.bajdcc.LALR1.grammar.runtime.service;

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray;

/**
 * 【运行时】运行时用户服务接口
 *
 * @author bajdcc
 */
public interface IRuntimeUserService {

	interface IRuntimeUserPipeService {
		/**
		 * 读取管道
		 * @param id 句柄
		 * @return 读取的对象
		 */
		RuntimeObject read(int id);

		/**
		 * 写入管道
		 * @param id 句柄
		 * @param obj 写入的对象
		 * @return 是否成功
		 */
		boolean write(int id, RuntimeObject obj);
	}

	interface IRuntimeUserShareService {
		/**
		 * 获取共享
		 * @param id 句柄
		 * @return 共享对象
		 */
		RuntimeObject get(int id);

		/**
		 * 设置共享
		 * @param id 句柄
		 * @param obj 共享对象
		 * @return 上次保存的内容
		 */
		RuntimeObject set(int id, RuntimeObject obj);
	}

	interface IRuntimeUserFileService {

	}

	/**
	 * 获取服务
	 * @return 管道服务
	 */
	IRuntimeUserPipeService getPipe();

	/**
	 * 获取服务
	 * @return 共享服务
	 */
	IRuntimeUserShareService getShare();

	/**
	 * 获取服务
	 * @return 文件服务
	 */
	IRuntimeUserFileService getFile();

	/**
	 * 创建用户服务句柄
	 *
	 * @param name 用户服务名称
	 * @param page 创建时的页面
	 * @return 用户服务句柄
	 */
	int create(String name, String page);

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
