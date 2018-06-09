package com.bajdcc.LALR1.interpret.os.user.routine;

import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.util.ResourceLoader;

/**
 * 【用户态】辅助功能
 *
 * @author bajdcc
 */
public class URUtil implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/util";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
