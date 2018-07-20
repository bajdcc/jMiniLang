package com.bajdcc.LALR1.interpret.os.ui;

import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.util.ResourceLoader;

/**
 * 【界面】入口
 *
 * @author bajdcc
 */
public class UIMain implements IOSCodePage {
	@Override
	public String getName() {
		return "/ui/main";
	}

	@Override
	public String getCode() {
		return ResourceLoader.INSTANCE.load(getClass());
	}
}
