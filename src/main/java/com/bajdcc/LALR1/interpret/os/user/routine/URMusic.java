package com.bajdcc.LALR1.interpret.os.user.routine;

import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.util.ResourceLoader;

/**
 * 【用户态】音乐
 *
 * @author bajdcc
 */
public class URMusic implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/music";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
