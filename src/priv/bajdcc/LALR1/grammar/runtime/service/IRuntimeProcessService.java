package priv.bajdcc.LALR1.grammar.runtime.service;

import priv.bajdcc.LALR1.grammar.runtime.RuntimeObject;

/**
 * 【运行时】运行时进程服务接口
 *
 * @author bajdcc
 */
public interface IRuntimeProcessService {

	/**
	 * 进程休眠
	 * @param pid 进程ID
	 * @param turn 休眠趟数
	 * @return 总休眠趟数
	 */
	int sleep(int pid, int turn) ;

	/**
	 * 进程等待
	 * @param joined 等待的进程ID
	 * @param pid 当前进程ID
	 * @param turn 休眠趟数
	 * @return 总休眠趟数
	 */
	int join(int joined, int pid, int turn);

	/**
	 * 添加代码页
	 * @param name 页名
	 * @param code 代码
	 * @return 是否成功
	 */
	boolean addCodePage(String name, String code);
}
