package com.bajdcc.LALR1.grammar.runtime.service;

import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray;

/**
 * 【运行时】运行时文件服务接口
 *
 * @author bajdcc
 */
public interface IRuntimeFileService {

	/**
	 * 添加代码页到VFS
	 *
	 * @param name    代码路径
	 * @param content 代码内容
	 */
	void addVfs(String name, String content);

	/**
	 * 获取VFS代码页
	 *
	 * @param name 代码路径
	 * @return 代码内容
	 */
	String getVfs(String name);

	/**
	 * 创建管道
	 *
	 * @param name     管道名称
	 * @param mode     模式，1读，2写，3追加写
	 * @param encoding 编码
	 * @param page     创建时的页面
	 * @return 管道句柄
	 */
	int create(String name, int mode, String encoding, String page);

	/**
	 * 销毁管道
	 *
	 * @param handle 管道句柄
	 * @return 是否成功
	 */
	boolean destroy(int handle);

	/**
	 * 文件读
	 *
	 * @param handle 管道句柄
	 * @return 读取的字符，-1失败
	 */
	int read(int handle);

	/**
	 * 文件字串读
	 *
	 * @param handle 管道句柄
	 * @return 字串，null失败
	 */
	String readString(int handle);

	/**
	 * 文件写
	 *
	 * @param handle 管道句柄
	 * @param ch     字符
	 * @return 是否成功
	 */
	boolean write(int handle, char ch);

	/**
	 * 文件字串写
	 *
	 * @param handle 管道句柄
	 * @param str    字串
	 * @return 是否成功
	 */
	boolean writeString(int handle, String str);

	/**
	 * 文件是否存在
	 *
	 * @param filename 文件名
	 * @return 文件是否存在
	 */
	boolean exists(String filename);

	/**
	 * 一次性读文件（UTF-8），仅限VFS
	 *
	 * @param filename 文件名
	 * @return 文件内容
	 */
	String readAll(String filename);

	/**
	 * 获取文件数量
	 *
	 * @return 文件数量
	 */
	long size();

	/**
	 * 获取列表
	 *
	 * @param api 是否API调用
	 * @return 列表
	 */
	RuntimeArray stat(boolean api);

	/**
	 * 获取虚拟文件数量
	 *
	 * @return 文件数量
	 */
	long getVfsListSize();

	/**
	 * 虚拟文件列表
	 *
	 * @param api 是否API调用
	 * @return 虚拟文件列表
	 */
	RuntimeArray getVfsList(boolean api);

	/**
	 * 读取虚拟文件并自动销毁
	 * @param name 文件名
	 * @return 文件内容，不存在则返回空
	 */
	String readAndDestroy(String name);
}
