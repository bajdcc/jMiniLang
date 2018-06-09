package com.bajdcc.LALR1.interpret.os.ui;

import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.util.ResourceLoader;

/**
 * 【界面】一言
 *
 * @author bajdcc
 */
public class UIHitokoto implements IOSCodePage {
	@Override
	public String getName() {
		return "/ui/hitokoto";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
