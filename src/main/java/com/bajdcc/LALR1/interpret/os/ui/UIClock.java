package com.bajdcc.LALR1.interpret.os.ui;

import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.util.ResourceLoader;

/**
 * 【界面】时钟
 *
 * @author bajdcc
 */
public class UIClock implements IOSCodePage {
	@Override
	public String getName() {
		return "/ui/clock";
	}

	@Override
	public String getCode() {
		return ResourceLoader.INSTANCE.load(getClass());
	}
}
