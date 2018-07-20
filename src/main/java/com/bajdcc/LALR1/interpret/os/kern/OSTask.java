package com.bajdcc.LALR1.interpret.os.kern;

import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.util.ResourceLoader;

/**
 * 【内核】服务
 *
 * @author bajdcc
 */
public class OSTask implements IOSCodePage {
	@Override
	public String getName() {
		return "/kern/task";
	}

	@Override
	public String getCode() {
		return ResourceLoader.INSTANCE.load(getClass());
	}
}
