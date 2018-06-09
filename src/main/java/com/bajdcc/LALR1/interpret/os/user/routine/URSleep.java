package com.bajdcc.LALR1.interpret.os.user.routine;

import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.util.ResourceLoader;

/**
 * 【用户态】休眠
 *
 * @author bajdcc
 */
public class URSleep implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/sleep";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
