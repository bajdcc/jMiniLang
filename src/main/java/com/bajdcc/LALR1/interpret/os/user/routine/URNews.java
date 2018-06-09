package com.bajdcc.LALR1.interpret.os.user.routine;

import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.util.ResourceLoader;

/**
 * 【用户态】新闻
 *
 * @author bajdcc
 */
public class URNews implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/news";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
