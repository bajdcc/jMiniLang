package priv.bajdcc.LALR1.grammar.runtime;

import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeFuncObject;
import priv.bajdcc.LALR1.grammar.runtime.service.IRuntimeService;

import java.util.List;

/**
 * 【运行时】运行时状态查询
 *
 * @author bajdcc
 */
public interface IRuntimeStatus {

	/**
	 * 得到过程的文档
	 *
	 * @param name 过程名
	 * @return 文档
	 */
	String getHelpString(String name);

	/**
	 * 得到过程的地址
	 *
	 * @param name 过程名
	 * @return 地址
	 * @throws RuntimeException 运行时错误
	 */
	int getFuncAddr(String name) throws RuntimeException;

	/**
	 * 载入代码并运行
	 *
	 * @param name 文件名
	 * @throws RuntimeException 运行时错误
	 */
	void runPage(String name) throws Exception;

	/**
	 * 载入代码并运行于新进程
	 *
	 * @param name 文件名
	 * @return 进程ID
	 * @throws RuntimeException 运行时错误
	 */
	int runProcess(String name) throws Exception;

	/**
	 * 载入代码并运行于新进程
	 *
	 * @param name 页名
	 * @return 进程ID
	 * @throws RuntimeException 运行时错误
	 */
	int runProcessX(String name) throws Exception;

	/**
	 * 载入代码并运行于新用户态进程
	 *
	 * @param name 文件名
	 * @return 进程ID
	 * @throws RuntimeException 运行时错误
	 */
	int runUsrProcess(String name) throws Exception;

	/**
	 * 载入代码并运行于新用户态进程
	 *
	 * @param name 页名
	 * @return 进程ID
	 * @throws RuntimeException 运行时错误
	 */
	int runUsrProcessX(String name) throws Exception;

	/**
	 * 运行时错误
	 *
	 * @param type 错误类型
	 * @throws RuntimeException 运行时异常
	 */
	void err(RuntimeException.RuntimeError type) throws RuntimeException;

	/**
	 * 运行时错误
	 *
	 * @param type    错误类型
	 * @param message 补充信息
	 * @throws RuntimeException 运行时异常
	 */
	void err(RuntimeException.RuntimeError type, String message) throws RuntimeException;

	/**
	 * 运行时警告
	 *
	 * @param type    错误类型
	 * @param message 补充信息
	 * @throws RuntimeException 运行时异常
	 */
	void warn(RuntimeException.RuntimeError type, String message);

	/**
	 * 创建进程
	 *
	 * @param func 函数
	 * @return 进程ID
	 * @throws Exception 运行时异常
	 */
	int createProcess(RuntimeFuncObject func) throws Exception;

	/**
	 * 创建进程
	 *
	 * @param func 函数
	 * @param obj  参数
	 * @return 进程ID
	 * @throws Exception 运行时异常
	 */
	int createProcess(RuntimeFuncObject func, RuntimeObject obj) throws Exception;

	/**
	 * 创建用户态进程
	 *
	 * @param func 函数
	 * @return 进程ID
	 * @throws Exception 运行时异常
	 */
	int createUsrProcess(RuntimeFuncObject func) throws Exception;

	/**
	 * 创建用户态进程
	 *
	 * @param func 函数
	 * @param obj  参数
	 * @return 进程ID
	 * @throws Exception 运行时异常
	 */
	int createUsrProcess(RuntimeFuncObject func, RuntimeObject obj) throws Exception;

	/**
	 * 获取页引用
	 *
	 * @param page 页名
	 */
	List<RuntimeCodePage> getPageRefers(String page);

	/**
	 * 得到进程ID
	 *
	 * @return PID
	 */
	int getPid();

	/**
	 * 得到父进程ID
	 *
	 * @return PID
	 */
	int getParentPid();

	/**
	 * 得到进程优先级
	 *
	 * @return 优先级
	 */
	int getPriority();

	/**
	 * 设置进程优先级
	 *
	 * @param priority 优先级
	 * @return 是否设置成功
	 */
	boolean setPriority(int priority);

	/**
	 * 得到服务接口
	 *
	 * @return 运行时服务接口
	 */
	IRuntimeService getService();

	/**
	 * 进程休眠
	 *
	 * @return 进程ID
	 */
	int sleep();

	/**
	 * 获取用户态进程列表
	 *
	 * @return 进程ID列表
	 */
	List<Integer> getUsrProcs();

	/**
	 * 获取内核态进程列表
	 *
	 * @return 进程ID列表
	 */
	List<Integer> getSysProcs();

	/**
	 * 获取进程列表
	 *
	 * @return 进程ID列表
	 */
	List<Integer> getAllProcs();

	/**
	 * 获取进程信息
	 *
	 * @param id 进程ID
	 * @return 进程信息
	 */
	Object[] getProcInfoById(int id);

	/**
	 * 获取当前进程信息
	 *
	 * @return 进程信息
	 */
	Object[] getProcInfo();

	/**
	 * 设置进程说明
	 *
	 * @param desc 说明信息
	 */
	void setProcDesc(String desc);
}
