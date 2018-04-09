package priv.bajdcc.LALR1.grammar.runtime.service;

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
	 * @return 管道句柄
	 */
	int create(String name, int mode, String encoding);

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
	 * @return
	 */
	boolean writeString(int handle, String str);
}
