package com.bajdcc.LALR1.interpret.os.kern;

import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.util.ResourceLoader;

/**
 * 【内核】入口
 *
 * @author bajdcc
 */
public class OSEntry implements IOSCodePage {

	@Override
	public String getName() {
		return "/kern/entry";
	}

	@Override
	public String getCode() {
		return ResourceLoader.INSTANCE.load(getClass());
	}
}
