package com.bajdcc.LALR1.grammar.runtime.service;

import java.util.List;

/**
 * 【运行时】运行时进程服务接口
 *
 * @author bajdcc
 */
public interface IRuntimeProcessService {

	/**
	 * 进程阻塞
	 *
	 * @param pid 进程ID
	 * @return 成功与否
	 */
	boolean block(int pid);

	/**
	 * 进程唤醒
	 *
	 * @param pid 进程ID
	 * @return 成功与否
	 */
	boolean wakeup(int pid);

	/**
	 * 进程休眠
	 *
	 * @param pid  进程ID
	 * @param turn 休眠趟数
	 * @return 总休眠趟数
	 */
	int sleep(int pid, int turn);

	/**
	 * 进程等待
	 *
	 * @param joined 等待的进程ID
	 * @param pid    当前进程ID
	 * @return 是则继续等待
	 */
	boolean join(int joined, int pid);

	/**
	 * 进行存活
	 *
	 * @param pid 进程标识
	 * @return 是否存活
	 */
	boolean live(int pid);

	/**
	 * 添加代码页
	 *
	 * @param name 页名
	 * @param code 代码
	 * @return 是否成功
	 */
	boolean addCodePage(String name, String code);

	/**
	 * 让解释器等待一段时间，让UI刷新
	 */
	void waitForUI();

	/**
	 * 虚拟机运行频率
	 *
	 * @return 运行频率
	 */
	double getSpeed();

	/**
	 * 获取当前进程信息（缓存）
	 *
	 * @return 进程信息
	 */
	List<Object[]> getProcInfoCache();

	/**
	 * 设置是否输出调试信息（堆栈）
	 *
	 * @param pid   当前进程PID
	 * @param debug 是否调试
	 */
	void setDebug(int pid, boolean debug);

	/**
	 * 设置高速运行模式
	 * @param mode 是否不休眠
	 */
	void setHighSpeed(boolean mode);

	/**
	 * 强制结束用户态进程
	 * @param pid PID
	 * @param error 错误原因
	 * @return 状态码
	 */
	int ring3Kill(int pid, String error);
}
