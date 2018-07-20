package com.bajdcc.LALR1.interpret.os.task;

import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.util.ResourceLoader;

/**
 * 【服务】网络
 *
 * @author bajdcc
 */
public class TKNet implements IOSCodePage {
	@Override
	public String getName() {
		return "/task/net";
	}

	@Override
	public String getCode() {
		return ResourceLoader.INSTANCE.load(getClass());
	}
}
