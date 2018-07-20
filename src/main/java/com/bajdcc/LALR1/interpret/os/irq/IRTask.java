package com.bajdcc.LALR1.interpret.os.irq;

import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.util.ResourceLoader;

/**
 * 【中断】服务
 *
 * @author bajdcc
 */
public class IRTask implements IOSCodePage {
	@Override
	public String getName() {
		return "/irq/task";
	}

	@Override
	public String getCode() {
		return ResourceLoader.INSTANCE.load(getClass());
	}
}
