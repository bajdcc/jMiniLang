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
	 * @param name 过程名
	 * @return 文档
	 */
	String getHelpString(String name);

	/**
	 * 得到过程的地址
	 * @param name 过程名
	 * @return 地址
	 * @throws RuntimeException 运行时错误
	 */
	int getFuncAddr(String name) throws RuntimeException;
	
	/**
	 * 载入代码并运行
	 * @param name 文件名
	 * @throws RuntimeException 运行时错误
	 */
	void runPage(String name) throws Exception;

	/**
	 * 运行时错误
	 * @param type 错误类型
	 * @throws RuntimeException 运行时异常
     */
	void err(RuntimeException.RuntimeError type) throws RuntimeException;

	/**
	 * 运行时错误
	 * @param type 错误类型
	 * @param message 补充信息
	 * @throws RuntimeException 运行时异常
     */
	void err(RuntimeException.RuntimeError type, String message) throws RuntimeException;

	/**
	 * 创建进程
	 * @param func 函数
	 * @throws Exception 运行时异常
	 * @return 进程ID
	 */
	int createProcess(RuntimeFuncObject func) throws Exception;

	/**
	 * 创建进程
	 * @param func 函数
	 * @param obj 参数
	 * @throws Exception 运行时异常
	 * @return 进程ID
	 */
	int createProcess(RuntimeFuncObject func, RuntimeObject obj) throws Exception;

	/**
	 * 获取页引用
	 * @param page 页名
	 */
	List<RuntimeCodePage> getPageRefers(String page);

	/**
	 * 得到进程ID
	 * @return PID
	 */
	int getPid();

	/**
	 * 得到进程优先级
	 * @return 优先级
	 */
	int getPriority();

	/**
	 * 得到服务接口
	 * @return 运行时服务接口
	 */
	IRuntimeService getService();
}
