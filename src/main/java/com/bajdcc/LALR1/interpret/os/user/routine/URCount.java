package com.bajdcc.LALR1.interpret.os.user.routine;

import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.util.ResourceLoader;

/**
 * 【用户态】计数
 *
 * @author bajdcc
 */
public class URCount implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/count";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
