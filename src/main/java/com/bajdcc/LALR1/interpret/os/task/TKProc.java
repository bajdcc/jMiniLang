package com.bajdcc.LALR1.interpret.os.task;

import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.util.ResourceLoader;

/**
 * 【服务】进程管理
 *
 * @author bajdcc
 */
public class TKProc implements IOSCodePage {
	@Override
	public String getName() {
		return "/task/proc";
	}

	@Override
	public String getCode() {
		return ResourceLoader.INSTANCE.load(getClass());
	}
}
