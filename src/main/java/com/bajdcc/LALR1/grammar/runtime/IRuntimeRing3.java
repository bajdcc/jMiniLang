package com.bajdcc.LALR1.grammar.runtime;

import com.bajdcc.LALR1.grammar.runtime.data.RuntimeFuncObject;
import com.bajdcc.LALR1.grammar.runtime.service.IRuntimeService;

import java.util.List;

/**
 * 【运行时】用户态运行时状态查询
 *
 * @author bajdcc
 */
public interface IRuntimeRing3 {

	/**
	 * 运行用户态代码
	 * @param code 代码
	 * @return PID
	 * @throws Exception 系统异常
	 */
	int exec(String code) throws Exception;

	/**
	 * 流输出
	 * @param text 内容
	 */
	void put(String text);
}
