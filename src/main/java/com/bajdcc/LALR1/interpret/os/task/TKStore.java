package com.bajdcc.LALR1.interpret.os.task;

import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.util.ResourceLoader;

/**
 * 【服务】全局存储
 *
 * @author bajdcc
 */
public class TKStore implements IOSCodePage {
	@Override
	public String getName() {
		return "/task/store";
	}

	@Override
	public String getCode() {
		return ResourceLoader.INSTANCE.load(getClass());
	}
}
