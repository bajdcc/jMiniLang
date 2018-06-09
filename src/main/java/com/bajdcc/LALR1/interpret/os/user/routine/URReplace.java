package com.bajdcc.LALR1.interpret.os.user.routine;

import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.util.ResourceLoader;

/**
 * 【用户态】置换
 *
 * @author bajdcc
 */
public class URReplace implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/replace";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
