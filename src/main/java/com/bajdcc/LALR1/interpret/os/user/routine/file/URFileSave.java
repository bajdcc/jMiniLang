package com.bajdcc.LALR1.interpret.os.user.routine.file;

import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.util.ResourceLoader;

/**
 * 【用户态】写文件
 *
 * @author bajdcc
 */
public class URFileSave implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/>";
	}

	@Override
	public String getCode() {
		return ResourceLoader.INSTANCE.load(getClass());
	}
}
